# 🎓 Camunda 8 Lab EX-6: Viva Voce Questions & Answers
**Course:** Workflow Automation (SRMIST)  
**Lab Assignment:** Exercise 6 (EX-6) — Simple Leave Request Process  
**Author:** Sudhanshu Kumar  

---

## Question 1: What is the difference between `processDefinitionId` and `processInstanceKey`?

### Detailed Answer:

| Feature / Aspect | `processDefinitionId` (BPMN Process ID) | `processInstanceKey` |
| :--- | :--- | :--- |
| **Conceptual Definition** | The static, logical identifier representing the **blueprint / design-time model** of the workflow. | A unique 64-bit numerical identifier representing a **single, runtime execution** of that model. |
| **Analogy** | Like a **Class** in Object-Oriented Programming (or a architectural blueprint of a building). | Like an **Object / Instance** of that class (or an actual building constructed from that blueprint). |
| **Data Type** | `String` (e.g., `"leave-request"`, alphanumeric with dashes/underscores). | `Long` (int64) (e.g., `2251799813685240`). |
| **Multiplicity** | **1-to-Many:** One single `processDefinitionId` can have thousands or millions of active, completed, or terminated instances. | **Unique:** Each process instance has exactly one distinct `processInstanceKey` across the entire cluster. |
| **Where Set / Assigned** | Configured by the developer in the BPMN diagram properties panel (`id="leave-request"`). | Automatically generated and assigned by the **Zeebe engine / event log** at runtime when an instance starts. |
| **Usage in API** | Passed in the request body to specify *which* workflow blueprint to execute: `{"processDefinitionId": "leave-request"}`. | Returned by the engine to track, query, correlate messages, or cancel that specific running instance. |

### Architectural Summary:
When you deploy `leave-request.bpmn`, Zeebe stores the definition with ID `leave-request` (and assigns it an internal `processDefinitionKey`). Every time a user or system calls `POST /v2/process-instances`, Zeebe instantiates a new execution token and gives it a globally unique `processInstanceKey`.

---

## Question 2: What response do you get if the Process ID is spelled wrong?

### Detailed Answer:
If the `processDefinitionId` is misspelled in the API request (e.g., `"leave-requst"` instead of `"leave-request"`), the Zeebe gateway searches the cluster's state repository for a deployed process definition with that BPMN ID. Because no matching deployed definition exists, the engine **rejects the command** and returns an **HTTP 404 Not Found** (or gRPC `NOT_FOUND` / code `5`) response.

### Exact JSON Error Response:
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Command 'CREATE' rejected with code 'NOT_FOUND': Expected to find process definition with BPMN process id 'leave-requst', but none found",
  "details": "Process definition with BPMN ID 'leave-requst' is not deployed to the cluster."
}
```

### Why this happens:
1. Zeebe is strictly typed and validates all deployment references against its immutable append-only event log.
2. If the BPMN process ID is not present in the Zeebe state partitions, it cannot spawn an instance.
3. No instance is created in Camunda Operate, and no token is generated.

---

## Question 3: Start the process 3 times. How many instances appear in Operate?

### Detailed Answer:
**Exactly 3 instances appear in Camunda Operate.**

### Detailed Explanation:
1. Each `POST http://localhost:8080/v2/process-instances` request triggers a distinct `CreateProcessInstanceCommand`.
2. Camunda 8 Zeebe treats every request independently, allocating a unique `processInstanceKey` to each execution:
   * **Request 1:** Started &rarr; `processInstanceKey: 2251799813685241` (Employee: "Ravi", days: 3)
   * **Request 2:** Started &rarr; `processInstanceKey: 2251799813685242` (Employee: "Priya", days: 2)
   * **Request 3:** Started &rarr; `processInstanceKey: 2251799813685243` (Employee: "Amit", days: 5)
3. Because the BPMN workflow has a User Task (`Approve Leave`), the process does not terminate immediately. Instead, execution halts and waits for a human user to complete the task in **Camunda Tasklist**.
4. Consequently, in **Camunda Operate** under the process `Leave Request` (`leave-request`):
   * Under **Active Instances Filter**: Exactly **3 running instances** are displayed.
   * On the BPMN heatmap, the user task **"Approve Leave"** displays a badge with count `(3)`, indicating that 3 execution tokens are concurrently waiting at this step.
   * Each instance maintains its own isolated variable scope (`employeeName`, `days`).
