package com.nexus.notification.service;

import com.nexus.notification.domain.entity.Notification;
import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebSocketNotificationService Unit Tests")
class WebSocketNotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketNotificationService webSocketNotificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        notification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.WEBSOCKET)
            .status(NotificationStatus.PENDING)
            .priority(NotificationPriority.HIGH)
            .subject("Test Notification")
            .message("This is a test WebSocket notification")
            .createdAt(Instant.now())
            .build();
    }

    @Test
    @DisplayName("Send notification to specific user successfully")
    void sendNotification_success() {
        // Arrange
        String userId = "user-123";
        doNothing().when(messagingTemplate).convertAndSendToUser(
            anyString(), anyString(), any(Notification.class));

        // Act
        webSocketNotificationService.sendNotification(userId, notification);

        // Assert
        verify(messagingTemplate).convertAndSendToUser(
            eq(userId),
            eq("/queue/notifications"),
            eq(notification)
        );
    }

    @Test
    @DisplayName("Send notification to different user successfully")
    void sendNotification_differentUser() {
        // Arrange
        String userId = "user-456";
        doNothing().when(messagingTemplate).convertAndSendToUser(
            anyString(), anyString(), any(Notification.class));

        // Act
        webSocketNotificationService.sendNotification(userId, notification);

        // Assert
        verify(messagingTemplate).convertAndSendToUser(
            eq(userId),
            eq("/queue/notifications"),
            eq(notification)
        );
    }

    @Test
    @DisplayName("Send notification failure throws RuntimeException")
    void sendNotification_failure() {
        // Arrange
        String userId = "user-123";
        doThrow(new RuntimeException("WebSocket connection error"))
            .when(messagingTemplate).convertAndSendToUser(
                anyString(), anyString(), any(Notification.class));

        // Act & Assert
        assertThatThrownBy(() -> webSocketNotificationService.sendNotification(userId, notification))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Failed to send WebSocket notification");

        verify(messagingTemplate).convertAndSendToUser(
            eq(userId),
            eq("/queue/notifications"),
            eq(notification)
        );
    }

    @Test
    @DisplayName("Broadcast notification to all users successfully")
    void broadcastNotification_success() {
        // Arrange
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(Notification.class));

        // Act
        webSocketNotificationService.broadcastNotification(notification);

        // Assert
        verify(messagingTemplate).convertAndSend(
            eq("/topic/notifications"),
            eq(notification)
        );
    }

    @Test
    @DisplayName("Broadcast notification with high priority")
    void broadcastNotification_highPriority() {
        // Arrange
        Notification urgentNotification = Notification.builder()
            .id("notif-999")
            .userId("system")
            .type(NotificationType.WEBSOCKET)
            .status(NotificationStatus.PENDING)
            .priority(NotificationPriority.URGENT)
            .subject("System Alert")
            .message("Critical system notification")
            .createdAt(Instant.now())
            .build();

        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(Notification.class));

        // Act
        webSocketNotificationService.broadcastNotification(urgentNotification);

        // Assert
        verify(messagingTemplate).convertAndSend(
            eq("/topic/notifications"),
            eq(urgentNotification)
        );
    }

    @Test
    @DisplayName("Broadcast notification failure throws RuntimeException")
    void broadcastNotification_failure() {
        // Arrange
        doThrow(new RuntimeException("Broadcast failed"))
            .when(messagingTemplate).convertAndSend(anyString(), any(Notification.class));

        // Act & Assert
        assertThatThrownBy(() -> webSocketNotificationService.broadcastNotification(notification))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Failed to broadcast notification");

        verify(messagingTemplate).convertAndSend(
            eq("/topic/notifications"),
            eq(notification)
        );
    }
}

