package com.nexus.notification.dto;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Notification type is required")
    private NotificationType type;
    
    private NotificationPriority priority = NotificationPriority.MEDIUM;
    
    private String subject;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private String template;
    
    private Map<String, Object> templateData;
    
    private String recipient;
    
    private String category;
    
    private Map<String, Object> metadata;
}

