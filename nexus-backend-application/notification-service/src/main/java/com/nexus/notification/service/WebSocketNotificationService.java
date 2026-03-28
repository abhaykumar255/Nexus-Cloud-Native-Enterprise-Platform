package com.nexus.notification.service;

import com.nexus.notification.domain.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * WebSocket notification service for real-time notifications
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Send notification to specific user via WebSocket
     */
    public void sendNotification(String userId, Notification notification) {
        log.info("Sending WebSocket notification to user: {}", userId);
        
        try {
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/notifications",
                    notification
            );
            
            log.debug("WebSocket notification sent successfully to user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification to user: {}", userId, e);
            throw new RuntimeException("Failed to send WebSocket notification", e);
        }
    }
    
    /**
     * Broadcast notification to all users
     */
    public void broadcastNotification(Notification notification) {
        log.info("Broadcasting notification to all users");
        
        try {
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            
            log.debug("Notification broadcast successfully");
        } catch (Exception e) {
            log.error("Failed to broadcast notification", e);
            throw new RuntimeException("Failed to broadcast notification", e);
        }
    }
}

