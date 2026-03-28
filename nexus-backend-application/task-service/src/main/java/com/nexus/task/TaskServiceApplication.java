package com.nexus.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Task Service
 * Provides:
 * - Task CRUD operations
 * - State machine for task lifecycle
 * - Task assignments and reassignments
 * - Task comments and attachments
 * - Task filtering and search
 * - Kafka event publishing
 * - Database partitioning for scalability
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.task", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class TaskServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}

