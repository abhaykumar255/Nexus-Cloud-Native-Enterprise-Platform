package com.nexus.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Cart Service Application
 * Manages shopping carts with Redis for session-based cart storage
 * 
 * Key Features:
 * - Add/Remove items from cart
 * - Update quantities
 * - Apply coupons/discounts
 * - Cart validation against inventory
 * - Cart expiration (7 days TTL)
 * - Guest cart support
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisRepositories
@ComponentScan(basePackages = {"com.nexus.cart", "com.nexus.common"})
public class CartServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CartServiceApplication.class, args);
    }
}

