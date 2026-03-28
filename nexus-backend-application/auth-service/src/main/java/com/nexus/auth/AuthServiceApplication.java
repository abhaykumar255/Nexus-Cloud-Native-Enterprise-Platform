package com.nexus.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Authentication Service
 * Provides:
 * - User registration and login
 * - JWT token generation (RS256)
 * - OAuth2 authentication (Google)
 * - Password management (reset, change)
 * - OTP generation and validation
 * - Token refresh and revocation
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.auth", "com.nexus.common"})
@EnableDiscoveryClient
public class AuthServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}

