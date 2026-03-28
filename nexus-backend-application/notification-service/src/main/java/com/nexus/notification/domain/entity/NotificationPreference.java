package com.nexus.notification.domain.entity;

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
 * User notification preferences
 */
@Document(collection = "notification_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreference {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String userId;
    
    private Map<String, Boolean> channelPreferences; // email: true, sms: false, etc.
    
    private Map<String, Boolean> categoryPreferences; // task: true, user: false, etc.
    
    private boolean globalEnabled;
    
    private String quietHoursStart; // HH:mm format
    
    private String quietHoursEnd;
    
    private String timezone;
    
    private Instant createdAt;
    
    private Instant updatedAt;
}

