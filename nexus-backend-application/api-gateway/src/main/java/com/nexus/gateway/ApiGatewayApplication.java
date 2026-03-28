package com.nexus.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Service
 * Routes requests to backend microservices with:
 * - JWT authentication/authorization
 * - Rate limiting (Redis-backed)
 * - Circuit breakers (Resilience4j)
 * - Request/response logging
 * - CORS configuration
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

