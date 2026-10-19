package edu.srmist.leaverequest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.srmist.leaverequest.config.GlobalExceptionHandler;
import edu.srmist.leaverequest.dto.CreateProcessInstanceRequest;
import edu.srmist.leaverequest.service.WorkflowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Leave Request REST API & Workflow Tests (Lab EX-6)")
class LeaveRequestControllerTest {

    private MockMvc mockMvc;
    private WorkflowService workflowService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        workflowService = new WorkflowService(null);
        workflowService.clearInstances();
        ProcessInstanceController controller = new ProcessInstanceController(workflowService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Step 3: Start process instance with Ravi, 3 days returns 201 Created and valid keys")
    void testStartLeaveRequestSuccess() throws Exception {
        CreateProcessInstanceRequest request = new CreateProcessInstanceRequest(
                "leave-request",
                Map.of("employeeName", "Ravi", "days", 3)
        );

        mockMvc.perform(post("/v2/process-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.processInstanceKey", notNullValue()))
                .andExpect(jsonPath("$.bpmnProcessId", is("leave-request")))
                .andExpect(jsonPath("$.variables.employeeName", is("Ravi")))
                .andExpect(jsonPath("$.variables.days", is(3)))
                .andExpect(jsonPath("$.state", containsString("ACTIVE")));
    }

    @Test
    @DisplayName("Viva Q2: Misspelled process ID (e.g. 'leave-requst') returns HTTP 404 NOT_FOUND")
    void testStartLeaveRequestMisspelledProcessIdThrows404() throws Exception {
        CreateProcessInstanceRequest request = new CreateProcessInstanceRequest(
                "leave-requst", // Misspelled ID
                Map.of("employeeName", "Ravi", "days", 3)
        );

        mockMvc.perform(post("/v2/process-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("NOT_FOUND")))
                .andExpect(jsonPath("$.message", containsString("Expected to find process definition with BPMN process id 'leave-requst'")));
    }

    @Test
    @DisplayName("Viva Q3: Start process 3 times -> Exactly 3 running instances appear")
    void testStartProcessThreeTimesCountThreeInstances() throws Exception {
        CreateProcessInstanceRequest req1 = new CreateProcessInstanceRequest("leave-request", Map.of("employeeName", "Ravi", "days", 3));
        CreateProcessInstanceRequest req2 = new CreateProcessInstanceRequest("leave-request", Map.of("employeeName", "Priya", "days", 2));
        CreateProcessInstanceRequest req3 = new CreateProcessInstanceRequest("leave-request", Map.of("employeeName", "Amit", "days", 5));

        mockMvc.perform(post("/v2/process-instances").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/v2/process-instances").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/v2/process-instances").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v2/process-instances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", is(3)))
                .andExpect(jsonPath("$.items", hasSize(3)));
    }

    @Test
    @DisplayName("Validation Error: Missing processDefinitionId returns HTTP 400 Bad Request")
    void testMissingProcessDefinitionIdReturns400() throws Exception {
        CreateProcessInstanceRequest request = new CreateProcessInstanceRequest(
                "",
                Map.of("employeeName", "Ravi", "days", 3)
        );

        mockMvc.perform(post("/v2/process-instances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("BAD_REQUEST")));
    }
}
