package com.nexus.external.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for External Integration Service
 * All cache TTL values and circuit breaker settings externalized
 */
@Configuration
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalServiceProperties {

    private CacheConfig cache = new CacheConfig();

    @Data
    public static class CacheConfig {
        private int weatherTtlMinutes = 30;
        private int newsTtlMinutes = 15;
        private int mapsTtlMinutes = 60;
    }
}

