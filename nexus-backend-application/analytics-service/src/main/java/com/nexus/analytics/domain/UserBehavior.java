package com.nexus.analytics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * User Behavior Tracking Document
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_behavior")
@CompoundIndex(name = "user_timestamp_idx", def = "{'userId': 1, 'timestamp': -1}")
public class UserBehavior {
    
    @Id
    private String id;
    
    private Long userId;
    private String sessionId;
    private String eventType; // PRODUCT_VIEW, ADD_TO_CART, REMOVE_FROM_CART, CHECKOUT, ORDER_PLACED, etc.
    private String entityType; // PRODUCT, CART, ORDER, REVIEW
    private Long entityId;
    
    // Event details
    private Map<String, Object> eventData;
    
    // Device & Location
    private String deviceType; // MOBILE, WEB, TABLET
    private String browser;
    private String os;
    private String ipAddress;
    private String location; // City/Country
    
    // Tracking
    private String referrer;
    private String pageUrl;
    
    private LocalDateTime timestamp;
}

