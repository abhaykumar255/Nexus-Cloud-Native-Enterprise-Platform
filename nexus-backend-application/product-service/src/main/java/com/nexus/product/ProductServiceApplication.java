package com.nexus.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Product Service
 * Provides:
 * - Product catalog management (CRUD)
 * - Multi-category support (Products, Food, Grocery)
 * - Seller product management
 * - Product search and filtering
 * - Product attributes and variants
 * - Redis caching for performance
 * - Event publishing via Kafka
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.product", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class ProductServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}

