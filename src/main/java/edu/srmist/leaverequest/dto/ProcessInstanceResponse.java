package edu.srmist.leaverequest.dto;

import java.time.Instant;
import java.util.Map;

public class ProcessInstanceResponse {

    private Long processInstanceKey;
    private Long processDefinitionKey;
    private Integer processDefinitionVersion;
    private String bpmnProcessId;
    private Map<String, Object> variables;
    private String tenantId;
    private String state;
    private Instant startDate;

    public ProcessInstanceResponse() {
        this.startDate = Instant.now();
        this.tenantId = "<default>";
        this.state = "ACTIVE";
    }

    public ProcessInstanceResponse(Long processInstanceKey, Long processDefinitionKey,
                                   Integer processDefinitionVersion, String bpmnProcessId,
                                   Map<String, Object> variables, String state) {
        this.processInstanceKey = processInstanceKey;
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionVersion = processDefinitionVersion;
        this.bpmnProcessId = bpmnProcessId;
        this.variables = variables;
        this.tenantId = "<default>";
        this.state = state;
        this.startDate = Instant.now();
    }

    public Long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(Long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getBpmnProcessId() {
        return bpmnProcessId;
    }

    public void setBpmnProcessId(String bpmnProcessId) {
        this.bpmnProcessId = bpmnProcessId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }
}
