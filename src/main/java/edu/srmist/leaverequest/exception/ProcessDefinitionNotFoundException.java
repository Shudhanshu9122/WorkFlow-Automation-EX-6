package edu.srmist.leaverequest.exception;

public class ProcessDefinitionNotFoundException extends RuntimeException {

    private final String processDefinitionId;

    public ProcessDefinitionNotFoundException(String processDefinitionId) {
        super(String.format("Command 'CREATE' rejected with code 'NOT_FOUND': Expected to find process definition with BPMN process id '%s', but none found", processDefinitionId));
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }
}
