package com.nexus.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for real-time notifications
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Value("${notification.websocket.endpoint:/ws}")
    private String endpoint;
    
    @Value("${notification.websocket.topic-prefix:/topic}")
    private String topicPrefix;
    
    @Value("${notification.websocket.user-prefix:/user}")
    private String userPrefix;
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(topicPrefix, userPrefix);
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix(userPrefix);
        
        log.info("WebSocket message broker configured with topic prefix: {}, user prefix: {}", 
                topicPrefix, userPrefix);
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endpoint)
                .setAllowedOriginPatterns("*")
                .withSockJS();
        
        log.info("STOMP endpoint registered at: {}", endpoint);
    }
}

