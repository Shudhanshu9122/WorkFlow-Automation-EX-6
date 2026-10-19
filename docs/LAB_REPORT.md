# 📄 SRMIST Lab Report: Exercise 6 (EX-6)
## Subject: Workflow Automation Lab
## Topic: Simple Leave Request Process via REST API Trigger

* **Student Name:** Sudhanshu Kumar
* **Registration No:** RA2311003010001
* **Process ID:** `leave-request`
* **Cluster:** Camunda 8 SaaS / Local Gateway
* **GitHub Repository:** [https://github.com/Shudhanshu9122/WorkFlow-Automation-EX-6](https://github.com/Shudhanshu9122/WorkFlow-Automation-EX-6)

---

## 1. Objective
To design and model a simple Leave Request process in Camunda Modeler, deploy it to a Camunda 8 cluster, trigger its execution programmatically via a REST API call from Postman, and monitor its execution state and variables in Camunda Operate.

---

## 2. Process Architecture & Model

### BPMN Sequence:
`[Start Event: Leave Requested]` &rarr; `[User Task: Approve Leave]` &rarr; `[End Event: Leave Processed]`

* **Process Definition ID:** `leave-request`
* **Process Name:** `Leave Request`
* **User Task ID:** `Task_ApproveLeave` (Name: "Approve Leave")
* **Form Linked:** `approve-leave-form`

### Required Submission Screenshot 1: BPMN Diagram
> **Instructions for Screenshot 1:**
> 1. Open Camunda Web Modeler or Desktop Modeler.
> 2. Open `leave-request.bpmn`.
> 3. Click the canvas and verify in the Properties Panel:
>    * **ID:** `leave-request`
>    * **Name:** `Leave Request`
> 4. Take a clear screenshot of the canvas showing the Start Event, User Task "Approve Leave", and End Event.

---

## 3. Execution via REST API (Postman)

### Request Details:
* **Method:** `POST`
* **URL:** `http://localhost:8080/v2/process-instances`
* **Headers:** `Content-Type: application/json`
* **Payload:**
```json
{
  "processDefinitionId": "leave-request",
  "variables": {
    "employeeName": "Ravi",
    "days": 3
  }
}
```

### Response Details:
* **Status:** `201 Created`
* **Sample Response Payload:**
```json
{
  "processInstanceKey": 2251799813685241,
  "processDefinitionKey": 2251799813685200,
  "processDefinitionVersion": 1,
  "bpmnProcessId": "leave-request",
  "variables": {
    "employeeName": "Ravi",
    "days": 3
  },
  "tenantId": "<default>",
  "state": "ACTIVE (Waiting at 'Approve Leave')",
  "startDate": "2026-10-19T14:49:56.029Z"
}
```
* **Observed `processInstanceKey`:** `2251799813685241`

### Required Submission Screenshot 2: Postman Request & Response
> **Instructions for Screenshot 2:**
> 1. Open Postman and import `postman/Camunda8_EX6_Leave_Request.postman_collection.json`.
> 2. Execute `1. Start Instance - POST /v2/process-instances`.
> 3. Take a screenshot showing the POST URL, request body with `"employeeName": "Ravi", "days": 3`, and the lower response pane displaying `201 Created` with the generated `processInstanceKey`.

---

## 4. Verification in Camunda Operate

### Observations:
1. Under **Processes** &rarr; `Leave Request` (`leave-request`):
   * State: **Active / Running**
   * Token position: The execution token (blue dot / badge) is located directly on the **Approve Leave** user task.
2. Under the **Variables Panel** for instance `2251799813685241`:
   * `employeeName`: `"Ravi"` (String)
   * `days`: `3` (Integer)

### Required Submission Screenshot 3: Camunda Operate Running Instance
> **Instructions for Screenshot 3:**
> 1. Open **Camunda Operate** in your browser.
> 2. Click on the process instance with key `2251799813685241`.
> 3. Ensure the visual diagram is visible with the token waiting on **Approve Leave**.
> 4. Ensure the **Variables** panel at the bottom is expanded showing `employeeName: "Ravi"` and `days: 3`.
> 5. Capture the screenshot.

---

## 5. Viva Voce Summary
* **Difference between `processDefinitionId` and `processInstanceKey`:** `processDefinitionId` is the static string identifying the workflow blueprint (`leave-request`); `processInstanceKey` is the unique 64-bit numeric identifier assigned to a specific runtime execution.
* **Response when Process ID is misspelled:** Returns **HTTP 404 NOT_FOUND** (`Command 'CREATE' rejected with code 'NOT_FOUND': Expected to find process definition with BPMN process id '...', but none found`).
* **Instances appearing in Operate after starting 3 times:** Exactly **3 active instances** appear in Operate, all concurrently waiting at the "Approve Leave" step.
