package com.nexus.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Notification Service
 * All expiration times and retry configurations externalized
 */
@Configuration
@ConfigurationProperties(prefix = "notification")
@Data
public class NotificationProperties {

    private ExpirationConfig expiration = new ExpirationConfig();
    private RetryConfig retry = new RetryConfig();

    @Data
    public static class ExpirationConfig {
        private int defaultExpirationDays = 30;
    }

    @Data
    public static class RetryConfig {
        private int maxRetries = 3;
        private int initialRetryDelaySeconds = 60;
    }
}

