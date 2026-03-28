package com.nexus.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Workflow Service Application
 * Handles distributed transaction orchestration and saga state machines
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.workflow", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class WorkflowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowServiceApplication.class, args);
    }
}

