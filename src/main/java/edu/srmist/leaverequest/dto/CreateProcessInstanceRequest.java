package edu.srmist.leaverequest.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public class CreateProcessInstanceRequest {

    @NotBlank(message = "processDefinitionId is required")
    private String processDefinitionId;

    private Map<String, Object> variables = new HashMap<>();

    public CreateProcessInstanceRequest() {
    }

    public CreateProcessInstanceRequest(String processDefinitionId, Map<String, Object> variables) {
        this.processDefinitionId = processDefinitionId;
        this.variables = variables != null ? variables : new HashMap<>();
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables != null ? variables : new HashMap<>();
    }
}
