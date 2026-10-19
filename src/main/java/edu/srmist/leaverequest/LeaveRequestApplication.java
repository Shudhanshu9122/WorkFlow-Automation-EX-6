package edu.srmist.leaverequest;

import io.camunda.client.annotation.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot Application for SRMIST Workflow Automation (Lab EX-6).
 * Automatically deploys the 'leave-request' process model to Camunda 8 on startup.
 */
@SpringBootApplication
@Deployment(resources = {
        "classpath:bpmn/leave-request.bpmn",
        "classpath:bpmn/approve-leave.form"
})
public class LeaveRequestApplication {

    private static final Logger log = LoggerFactory.getLogger(LeaveRequestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LeaveRequestApplication.class, args);
    }

    @Bean
    public CommandLineRunner startupBanner() {
        return args -> {
            log.info("==========================================================================");
            log.info(" SRMIST Workflow Automation Lab (EX-6): Leave Request Process Started     ");
            log.info(" BPMN Process ID : leave-request                                          ");
            log.info(" Process Model   : classpath:bpmn/leave-request.bpmn                      ");
            log.info(" Form Schema     : classpath:bpmn/approve-leave.form                      ");
            log.info(" REST Endpoint   : POST http://localhost:8080/v2/process-instances        ");
            log.info(" Query Instances : GET  http://localhost:8080/v2/process-instances        ");
            log.info("==========================================================================");
        };
    }
}
