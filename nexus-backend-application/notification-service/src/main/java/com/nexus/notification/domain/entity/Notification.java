package com.nexus.notification.domain.entity;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * Notification document stored in MongoDB
 */
@Document(collection = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private NotificationType type;
    
    private NotificationStatus status;
    
    private NotificationPriority priority;
    
    private String subject;
    
    private String message;
    
    private String template;
    
    private Map<String, Object> templateData;
    
    private String recipient; // Email, phone number, or device token
    
    private String sender;
    
    private String category;
    
    private Map<String, Object> metadata;
    
    private String errorMessage;
    
    private int retryCount;
    
    @Indexed
    private Instant createdAt;
    
    private Instant sentAt;
    
    private Instant deliveredAt;
    
    private Instant readAt;
    
    @Indexed(expireAfterSeconds = 2592000) // 30 days TTL
    private Instant expiresAt;
}

