package com.nexus.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * User Service
 * Provides:
 * - User profile management (CRUD)
 * - Role-Based Access Control (RBAC)
 * - User preferences and settings
 * - Avatar/profile picture management
 * - Inter-service communication via Feign
 * - Event publishing via Kafka
 * - Caching with Redis
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.user", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

