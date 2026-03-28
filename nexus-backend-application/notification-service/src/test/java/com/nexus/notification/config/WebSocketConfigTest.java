package com.nexus.notification.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WebSocketConfig
 */
@DisplayName("WebSocketConfig Unit Tests")
class WebSocketConfigTest {

    private WebSocketConfig webSocketConfig;

    @BeforeEach
    void setUp() {
        webSocketConfig = new WebSocketConfig();
        // Inject field values using reflection
        ReflectionTestUtils.setField(webSocketConfig, "endpoint", "/test-ws");
        ReflectionTestUtils.setField(webSocketConfig, "topicPrefix", "/test-topic");
        ReflectionTestUtils.setField(webSocketConfig, "userPrefix", "/test-user");
    }

    @Test
    @DisplayName("configureMessageBroker configures broker with correct prefixes")
    void configureMessageBroker_configuresBroker() {
        // Arrange
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
        
        // Act
        webSocketConfig.configureMessageBroker(registry);
        
        // Assert
        verify(registry).enableSimpleBroker("/test-topic", "/test-user");
        verify(registry).setApplicationDestinationPrefixes("/app");
        verify(registry).setUserDestinationPrefix("/test-user");
    }

    @Test
    @DisplayName("registerStompEndpoints registers endpoint with correct path")
    void registerStompEndpoints_registersEndpoint() {
        // Arrange
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        var endpointRegistration = mock(org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration.class);
        when(registry.addEndpoint("/test-ws")).thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOriginPatterns(any())).thenReturn(endpointRegistration);

        // Act
        webSocketConfig.registerStompEndpoints(registry);

        // Assert
        verify(registry).addEndpoint("/test-ws");
        verify(endpointRegistration).setAllowedOriginPatterns(any());
    }

    @Test
    @DisplayName("configureMessageBroker is called without errors")
    void configureMessageBroker_noErrors() {
        // Arrange
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
        when(registry.enableSimpleBroker(any())).thenReturn(null);
        when(registry.setApplicationDestinationPrefixes(anyString())).thenReturn(registry);
        when(registry.setUserDestinationPrefix(anyString())).thenReturn(registry);
        
        // Act & Assert - should not throw
        webSocketConfig.configureMessageBroker(registry);
    }
}

