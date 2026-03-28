package com.nexus.external.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for WebClientConfig
 */
class WebClientConfigTest {

    @Test
    @DisplayName("Should create WebClient.Builder bean")
    void webClientBuilderBeanCreation() {
        // Arrange
        WebClientConfig config = new WebClientConfig();

        // Act
        WebClient.Builder builder = config.webClientBuilder();

        // Assert
        assertThat(builder).isNotNull();
        WebClient webClient = builder.build();
        assertThat(webClient).isNotNull();
    }
}

