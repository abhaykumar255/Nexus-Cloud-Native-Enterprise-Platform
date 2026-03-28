package com.nexus.ai.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.ai.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Kafka Consumer for Task Events
 * Consumes task events to update recommendation models in real-time
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventConsumer {

    private final RecommendationService recommendationService;
    private final ObjectMapper objectMapper;

    /**
     * Consume task events to update user recommendation profiles
     */
    @KafkaListener(
            topics = "task.events",
            groupId = "ai-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTaskEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        
        log.info("Received task event from topic: {}, key: {}", topic, key);
        
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            
            String eventType = (String) event.get("eventType");
            log.info("Processing task event type: {}", eventType);
            
            if ("STATUS_CHANGED".equals(eventType) && "DONE".equals(event.get("toStatus"))) {
                // Task completed - update user recommendation profile
                String userIdStr = (String) event.get("assigneeId");
                if (userIdStr != null) {
                    UUID userId = UUID.fromString(userIdStr);
                    
                    // Extract task details for profile update
                    String skills = event.getOrDefault("skills", "").toString();
                    List<String> tags = event.get("tags") != null 
                            ? (List<String>) event.get("tags") 
                            : List.of();
                    
                    // Update recommendation profile
                    recommendationService.updateUserProfile(userId, skills, tags, 1.0);
                    
                    log.info("Updated recommendation profile for user: {}", userId);
                }
            }
            
        } catch (Exception e) {
            log.error("Error processing task event: {}", message, e);
            // In production, send to DLQ for manual replay
        }
    }

    /**
     * Consume user events to update behavior analytics
     */
    @KafkaListener(
            topics = "user.events",
            groupId = "ai-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeUserEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        
        log.info("Received user event from topic: {}", topic);
        
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String eventType = (String) event.get("eventType");
            
            log.info("Processing user event type: {}", eventType);
            
            // Process user creation, profile updates, etc.
            // This would update UserBehaviorAnalytics in MongoDB
            
        } catch (Exception e) {
            log.error("Error processing user event: {}", message, e);
        }
    }
}

