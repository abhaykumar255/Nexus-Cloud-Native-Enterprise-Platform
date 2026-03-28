package com.nexus.notification.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NotificationProperties Unit Tests")
class NotificationPropertiesTest {

    private NotificationProperties properties;

    @BeforeEach
    void setUp() {
        properties = new NotificationProperties();
    }

    @Test
    @DisplayName("Default expiration config is initialized")
    void defaultExpirationConfig_isInitialized() {
        assertThat(properties.getExpiration()).isNotNull();
        assertThat(properties.getExpiration().getDefaultExpirationDays()).isEqualTo(30);
    }

    @Test
    @DisplayName("Default retry config is initialized")
    void defaultRetryConfig_isInitialized() {
        assertThat(properties.getRetry()).isNotNull();
        assertThat(properties.getRetry().getMaxRetries()).isEqualTo(3);
        assertThat(properties.getRetry().getInitialRetryDelaySeconds()).isEqualTo(60);
    }

    @Test
    @DisplayName("Expiration config can be customized")
    void expirationConfig_canBeCustomized() {
        NotificationProperties.ExpirationConfig expiration = new NotificationProperties.ExpirationConfig();
        expiration.setDefaultExpirationDays(45);
        
        properties.setExpiration(expiration);
        
        assertThat(properties.getExpiration().getDefaultExpirationDays()).isEqualTo(45);
    }

    @Test
    @DisplayName("Retry config can be customized")
    void retryConfig_canBeCustomized() {
        NotificationProperties.RetryConfig retry = new NotificationProperties.RetryConfig();
        retry.setMaxRetries(5);
        retry.setInitialRetryDelaySeconds(120);
        
        properties.setRetry(retry);
        
        assertThat(properties.getRetry().getMaxRetries()).isEqualTo(5);
        assertThat(properties.getRetry().getInitialRetryDelaySeconds()).isEqualTo(120);
    }

    @Test
    @DisplayName("ExpirationConfig equals and hashCode")
    void expirationConfig_equalsAndHashCode() {
        NotificationProperties.ExpirationConfig config1 = new NotificationProperties.ExpirationConfig();
        config1.setDefaultExpirationDays(30);
        
        NotificationProperties.ExpirationConfig config2 = new NotificationProperties.ExpirationConfig();
        config2.setDefaultExpirationDays(30);
        
        NotificationProperties.ExpirationConfig config3 = new NotificationProperties.ExpirationConfig();
        config3.setDefaultExpirationDays(45);
        
        assertThat(config1).isEqualTo(config2);
        assertThat(config1).isNotEqualTo(config3);
        assertThat(config1.hashCode()).isEqualTo(config2.hashCode());
    }

    @Test
    @DisplayName("RetryConfig equals and hashCode")
    void retryConfig_equalsAndHashCode() {
        NotificationProperties.RetryConfig retry1 = new NotificationProperties.RetryConfig();
        retry1.setMaxRetries(3);
        retry1.setInitialRetryDelaySeconds(60);
        
        NotificationProperties.RetryConfig retry2 = new NotificationProperties.RetryConfig();
        retry2.setMaxRetries(3);
        retry2.setInitialRetryDelaySeconds(60);
        
        NotificationProperties.RetryConfig retry3 = new NotificationProperties.RetryConfig();
        retry3.setMaxRetries(5);
        
        assertThat(retry1).isEqualTo(retry2);
        assertThat(retry1).isNotEqualTo(retry3);
        assertThat(retry1.hashCode()).isEqualTo(retry2.hashCode());
    }

    @Test
    @DisplayName("ExpirationConfig toString works")
    void expirationConfig_toString() {
        NotificationProperties.ExpirationConfig config = new NotificationProperties.ExpirationConfig();
        config.setDefaultExpirationDays(60);
        
        String result = config.toString();
        
        assertThat(result).contains("ExpirationConfig");
        assertThat(result).contains("60");
    }

    @Test
    @DisplayName("RetryConfig toString works")
    void retryConfig_toString() {
        NotificationProperties.RetryConfig retry = new NotificationProperties.RetryConfig();
        retry.setMaxRetries(5);
        
        String result = retry.toString();
        
        assertThat(result).contains("RetryConfig");
        assertThat(result).contains("5");
    }

    @Test
    @DisplayName("NotificationProperties toString works")
    void notificationProperties_toString() {
        String result = properties.toString();
        
        assertThat(result).contains("NotificationProperties");
        assertThat(result).contains("expiration");
        assertThat(result).contains("retry");
    }

    @Test
    @DisplayName("NotificationProperties equals and hashCode")
    void notificationProperties_equalsAndHashCode() {
        NotificationProperties props1 = new NotificationProperties();
        NotificationProperties props2 = new NotificationProperties();
        
        assertThat(props1).isEqualTo(props2);
        assertThat(props1.hashCode()).isEqualTo(props2.hashCode());
    }
}

