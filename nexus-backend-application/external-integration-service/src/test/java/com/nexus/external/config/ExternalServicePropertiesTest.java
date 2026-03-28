package com.nexus.external.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ExternalServiceProperties
 */
class ExternalServicePropertiesTest {

    @Test
    @DisplayName("Should have default cache configuration")
    void defaultCacheConfig() {
        // Act
        ExternalServiceProperties properties = new ExternalServiceProperties();
        ExternalServiceProperties.CacheConfig cacheConfig = properties.getCache();

        // Assert
        assertThat(cacheConfig).isNotNull();
        assertThat(cacheConfig.getWeatherTtlMinutes()).isEqualTo(30);
        assertThat(cacheConfig.getNewsTtlMinutes()).isEqualTo(15);
        assertThat(cacheConfig.getMapsTtlMinutes()).isEqualTo(60);
    }

    @Test
    @DisplayName("Should allow setting custom cache configuration")
    void customCacheConfig() {
        // Arrange
        ExternalServiceProperties properties = new ExternalServiceProperties();
        ExternalServiceProperties.CacheConfig customCache = new ExternalServiceProperties.CacheConfig();
        customCache.setWeatherTtlMinutes(60);
        customCache.setNewsTtlMinutes(30);
        customCache.setMapsTtlMinutes(120);

        // Act
        properties.setCache(customCache);

        // Assert
        assertThat(properties.getCache().getWeatherTtlMinutes()).isEqualTo(60);
        assertThat(properties.getCache().getNewsTtlMinutes()).isEqualTo(30);
        assertThat(properties.getCache().getMapsTtlMinutes()).isEqualTo(120);
    }

    @Test
    @DisplayName("CacheConfig should support equals and hashCode")
    void cacheConfigEqualsAndHashCode() {
        // Arrange
        ExternalServiceProperties.CacheConfig config1 = new ExternalServiceProperties.CacheConfig();
        config1.setWeatherTtlMinutes(30);
        config1.setNewsTtlMinutes(15);

        ExternalServiceProperties.CacheConfig config2 = new ExternalServiceProperties.CacheConfig();
        config2.setWeatherTtlMinutes(30);
        config2.setNewsTtlMinutes(15);

        // Assert
        assertThat(config1).isEqualTo(config2);
        assertThat(config1.hashCode()).isEqualTo(config2.hashCode());
    }
}

