package com.nexus.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Order Service Application
 * 
 * Manages order lifecycle, order history, and coordinates distributed transactions via Saga
 * 
 * Key Features:
 * - Order creation and management
 * - Order status state machine (PENDING → CONFIRMED → PROCESSING → DISPATCHED → DELIVERED)
 * - Saga orchestration for distributed order placement
 * - Order history and tracking
 * - Integration with Payment, Inventory, and Delivery services
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.nexus.order", "com.nexus.common"})
public class OrderServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

