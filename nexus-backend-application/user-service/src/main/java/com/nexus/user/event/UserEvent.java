package com.nexus.user.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * User domain event published to Kafka
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    
    private String eventId;
    private EventType eventType;
    private String userId;
    private String email;
    private String username;
    private Instant timestamp;
    
    public enum EventType {
        USER_CREATED,
        USER_UPDATED,
        USER_DELETED,
        PROFILE_COMPLETED
    }
}

