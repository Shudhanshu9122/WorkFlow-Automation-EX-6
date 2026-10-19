package edu.srmist.leaverequest.api;

import edu.srmist.leaverequest.dto.CreateProcessInstanceRequest;
import edu.srmist.leaverequest.dto.ProcessInstanceResponse;
import edu.srmist.leaverequest.service.WorkflowService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Camunda 8 Process Instance REST API Controller.
 * Provides the exact endpoint requested in Lab Exercise 6:
 * POST http://localhost:8080/v2/process-instances
 */
@RestController
@RequestMapping("/v2/process-instances")
public class ProcessInstanceController {

    private static final Logger log = LoggerFactory.getLogger(ProcessInstanceController.class);
    private final WorkflowService workflowService;

    @Autowired
    public ProcessInstanceController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    /**
     * Start a new process instance.
     * Matches Step 3 of Lab Exercise 6.
     */
    @PostMapping
    public ResponseEntity<ProcessInstanceResponse> startProcessInstance(
            @Valid @RequestBody CreateProcessInstanceRequest request) {

        log.info("[REST POST /v2/process-instances] Received request to start instance: processDefinitionId='{}'",
                request.getProcessDefinitionId());

        ProcessInstanceResponse response = workflowService.startProcessInstance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all active process instances.
     * Used for Viva Question 3 verification: "Start the process 3 times. How many instances appear in Operate?"
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllInstances() {
        List<ProcessInstanceResponse> instances = workflowService.getAllInstances();
        return ResponseEntity.ok(Map.of(
                "totalCount", instances.size(),
                "items", instances
        ));
    }

    /**
     * Get single process instance details by key.
     */
    @GetMapping("/{processInstanceKey}")
    public ResponseEntity<ProcessInstanceResponse> getInstanceByKey(@PathVariable Long processInstanceKey) {
        return workflowService.getInstanceByKey(processInstanceKey)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
