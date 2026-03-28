package com.nexus.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Notification Service
 * Provides:
 * - Multi-channel notifications (Email, SMS, Push, In-App)
 * - WebSocket real-time notifications
 * - Notification templates with Thymeleaf
 * - Kafka event consumers
 * - Notification preferences and history
 * - MongoDB for storage
 * - Async processing with thread pools
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.notification", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EnableAsync
@EnableScheduling
public class NotificationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}

