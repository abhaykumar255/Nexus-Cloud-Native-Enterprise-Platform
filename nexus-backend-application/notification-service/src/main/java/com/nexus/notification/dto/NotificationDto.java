package com.nexus.notification.dto;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    
    private String id;
    private String userId;
    private NotificationType type;
    private NotificationStatus status;
    private NotificationPriority priority;
    private String subject;
    private String message;
    private String template;
    private Map<String, Object> templateData;
    private String recipient;
    private String sender;
    private String category;
    private Map<String, Object> metadata;
    private Instant createdAt;
    private Instant sentAt;
    private Instant deliveredAt;
    private Instant readAt;
}

