package com.nexus.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Inventory Service
 * Provides:
 * - Real-time inventory tracking
 * - Stock management (add, reduce, adjust)
 * - Soft reservations (Redis - 15 min TTL for cart)
 * - Hard reservations (PostgreSQL - for confirmed orders)
 * - Low stock alerts
 * - Inventory history and audit trail
 * - Multi-location support
 * - Event publishing via Kafka
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.inventory", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EnableScheduling
public class InventoryServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}

