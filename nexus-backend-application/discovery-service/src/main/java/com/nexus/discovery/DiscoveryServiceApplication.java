package com.nexus.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Discovery Service
 * Provides service registration and discovery for all microservices
 * Enables client-side load balancing via Feign/Ribbon
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}

