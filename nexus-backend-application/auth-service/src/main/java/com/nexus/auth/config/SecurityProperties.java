package com.nexus.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Security configuration properties for Auth Service
 * BCrypt strength and other security settings
 */
@Configuration
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    private PasswordConfig password = new PasswordConfig();

    @Data
    public static class PasswordConfig {
        private int bcryptStrength = 12;
    }
}

