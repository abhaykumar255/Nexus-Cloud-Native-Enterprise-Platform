package com.nexus.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * External Integration Service Application
 * Handles third-party API integrations with adapter pattern and circuit breaker
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.external", "com.nexus.common"})
@EnableDiscoveryClient
public class ExternalIntegrationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalIntegrationServiceApplication.class, args);
    }
}

