package com.nexus.notification.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.notification.config.NotificationProperties;
import java.util.HashMap;
import java.util.Map;
import com.nexus.notification.domain.entity.Notification;
import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import com.nexus.notification.domain.repository.NotificationRepository;
import com.nexus.notification.dto.NotificationDto;
import com.nexus.notification.dto.SendNotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Unit Tests")
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private WebSocketNotificationService webSocketService;

    @Mock
    private NotificationProperties properties;

    @InjectMocks
    private NotificationService notificationService;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    private NotificationProperties.ExpirationConfig expirationConfig;
    private SendNotificationRequest request;
    private Notification notification;

    @BeforeEach
    void setUp() {
        expirationConfig = new NotificationProperties.ExpirationConfig();
        expirationConfig.setDefaultExpirationDays(30);

        request = new SendNotificationRequest();
        request.setUserId("user-123");
        request.setType(NotificationType.EMAIL);
        request.setPriority(NotificationPriority.HIGH);
        request.setSubject("Test Subject");
        request.setMessage("Test Message");
        request.setRecipient("test@example.com");
        request.setCategory("user");

        notification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .status(NotificationStatus.READ)
            .priority(NotificationPriority.HIGH)
            .subject("Test Subject")
            .message("Test Message")
            .recipient("test@example.com")
            .category("user")
            .createdAt(Instant.now())
            .readAt(Instant.now())
            .build();
    }

    @Test
    @DisplayName("Send notification successfully")
    void sendNotification_success() {
        // Arrange
        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doNothing().when(emailService).sendSimpleEmail(anyString(), anyString(), anyString());

        // Act
        NotificationDto result = notificationService.sendNotification(request);

        // Assert
        assertThat(result).isNotNull();
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(emailService).sendSimpleEmail("test@example.com", "Test Subject", "Test Message");
    }

    @Test
    @DisplayName("Get user notifications")
    void getUserNotifications_success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 20);
        Page<Notification> page = new PageImpl<>(Collections.singletonList(notification));
        when(notificationRepository.findByUserId("user-123", pageable)).thenReturn(page);

        // Act
        Page<NotificationDto> result = notificationService.getUserNotifications("user-123", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(notificationRepository).findByUserId("user-123", pageable);
    }

    @Test
    @DisplayName("Get unread count")
    void getUnreadCount_success() {
        // Arrange
        when(notificationRepository.countByUserIdAndStatus("user-123", NotificationStatus.SENT)).thenReturn(5L);

        // Act
        long count = notificationService.getUnreadCount("user-123");

        // Assert
        assertThat(count).isEqualTo(5L);
        verify(notificationRepository).countByUserIdAndStatus("user-123", NotificationStatus.SENT);
    }

    @Test
    @DisplayName("Mark as read successfully")
    void markAsRead_success() {
        // Arrange
        when(notificationRepository.findById("notif-123")).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        NotificationDto result = notificationService.markAsRead("notif-123", "user-123");

        // Assert
        assertThat(result).isNotNull();
        verify(notificationRepository).findById("notif-123");
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Mark as read throws exception when notification not found")
    void markAsRead_notFound_throwsException() {
        // Arrange
        when(notificationRepository.findById("notif-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.markAsRead("notif-999", "user-123"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("notif-999");

        verify(notificationRepository).findById("notif-999");
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    @DisplayName("Mark all as read successfully")
    void markAllAsRead_success() {
        // Arrange
        Page<Notification> page = new PageImpl<>(Collections.singletonList(notification));
        when(notificationRepository.findByUserIdAndStatus(eq("user-123"), eq(NotificationStatus.SENT), any()))
            .thenReturn(page);
        when(notificationRepository.saveAll(any())).thenReturn(Collections.singletonList(notification));

        // Act
        notificationService.markAllAsRead("user-123");

        // Assert
        verify(notificationRepository).findByUserIdAndStatus(eq("user-123"), eq(NotificationStatus.SENT), any());
        verify(notificationRepository).saveAll(any());
    }

    @Test
    @DisplayName("Delete notification successfully")
    void deleteNotification_success() {
        // Arrange
        when(notificationRepository.findById("notif-123")).thenReturn(Optional.of(notification));
        doNothing().when(notificationRepository).delete(any(Notification.class));

        // Act
        notificationService.deleteNotification("notif-123", "user-123");

        // Assert
        verify(notificationRepository).findById("notif-123");
        verify(notificationRepository).delete(notification);
    }

    @Test
    @DisplayName("Delete notification throws exception when notification not found")
    void deleteNotification_notFound_throwsException() {
        // Arrange
        when(notificationRepository.findById("notif-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.deleteNotification("notif-999", "user-123"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("notif-999");

        verify(notificationRepository).findById("notif-999");
        verify(notificationRepository, never()).delete(any(Notification.class));
    }

    @Test
    @DisplayName("Send notification with template EMAIL")
    void sendNotification_withTemplateEmail() {
        // Arrange
        request.setTemplate("welcome-template");
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("name", "John");
        request.setTemplateData(templateData);

        Notification templatedNotification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .template("welcome-template")
            .templateData(templateData)
            .subject("Test Subject")
            .recipient("test@example.com")
            .build();

        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(templatedNotification);
        doNothing().when(emailService).sendTemplateEmail(anyString(), anyString(), anyString(), any());

        // Act
        NotificationDto result = notificationService.sendNotification(request);

        // Assert
        assertThat(result).isNotNull();
        verify(emailService).sendTemplateEmail(eq("test@example.com"), eq("Test Subject"), eq("welcome-template"), any());
    }

    @Test
    @DisplayName("Send IN_APP notification")
    void sendNotification_inApp() {
        // Arrange
        request.setType(NotificationType.IN_APP);

        Notification inAppNotification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.IN_APP)
            .build();

        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(inAppNotification);
        doNothing().when(webSocketService).sendNotification(anyString(), any());

        // Act
        notificationService.sendNotification(request);

        // Assert
        verify(webSocketService).sendNotification(eq("user-123"), any(Notification.class));
    }

    @Test
    @DisplayName("Send WEBSOCKET notification")
    void sendNotification_websocket() {
        // Arrange
        request.setType(NotificationType.WEBSOCKET);

        Notification websocketNotification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.WEBSOCKET)
            .build();

        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(websocketNotification);
        doNothing().when(webSocketService).sendNotification(anyString(), any());

        // Act
        notificationService.sendNotification(request);

        // Assert
        verify(webSocketService).sendNotification(eq("user-123"), any(Notification.class));
    }

    @Test
    @DisplayName("Send SMS notification logs warning")
    void sendNotification_sms() {
        // Arrange
        request.setType(NotificationType.SMS);

        Notification smsNotification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.SMS)
            .build();

        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(smsNotification);

        // Act
        notificationService.sendNotification(request);

        // Assert
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Send PUSH notification logs warning")
    void sendNotification_push() {
        // Arrange
        request.setType(NotificationType.PUSH);

        Notification pushNotification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.PUSH)
            .build();

        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(pushNotification);

        // Act
        notificationService.sendNotification(request);

        // Assert
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    @DisplayName("Get user notifications with empty page")
    void getUserNotifications_emptyPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Notification> emptyPage = new PageImpl<>(Collections.emptyList());
        when(notificationRepository.findByUserId("user-999", pageable)).thenReturn(emptyPage);

        // Act
        Page<NotificationDto> result = notificationService.getUserNotifications("user-999", pageable);

        // Assert
        assertThat(result.getContent()).isEmpty();
        verify(notificationRepository).findByUserId("user-999", pageable);
    }

    @Test
    @DisplayName("Get unread count returns zero when no unread notifications")
    void getUnreadCount_returnsZero() {
        // Arrange
        when(notificationRepository.countByUserIdAndStatus("user-123", NotificationStatus.SENT))
            .thenReturn(0L);

        // Act
        long count = notificationService.getUnreadCount("user-123");

        // Assert
        assertThat(count).isEqualTo(0L);
        verify(notificationRepository).countByUserIdAndStatus("user-123", NotificationStatus.SENT);
    }

    @Test
    @DisplayName("Send notification with LOW priority")
    void sendNotification_lowPriority() {
        // Arrange
        request.setPriority(NotificationPriority.LOW);

        Notification lowPriorityNotification = notification;

        when(properties.getExpiration()).thenReturn(expirationConfig);
        when(notificationRepository.save(any(Notification.class))).thenReturn(lowPriorityNotification);
        doNothing().when(emailService).sendSimpleEmail(anyString(), anyString(), anyString());

        // Act
        NotificationDto result = notificationService.sendNotification(request);

        // Assert
        assertThat(result).isNotNull();
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(emailService).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Get user notifications returns DTOs with all fields populated")
    void getUserNotifications_returnsDtosWithAllFields() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Instant now = Instant.now();
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("key", "value");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("meta", "data");

        Notification fullNotification = Notification.builder()
            .id("notif-1")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .status(NotificationStatus.DELIVERED)
            .priority(NotificationPriority.HIGH)
            .subject("Full Subject")
            .message("Full Message")
            .template("template-1")
            .templateData(templateData)
            .recipient("test@example.com")
            .sender("sender@example.com")
            .category("test-category")
            .metadata(metadata)
            .createdAt(now)
            .sentAt(now)
            .deliveredAt(now)
            .readAt(now)
            .build();

        Page<Notification> page = new PageImpl<>(List.of(fullNotification));
        when(notificationRepository.findByUserId("user-123", pageable)).thenReturn(page);

        // Act
        Page<NotificationDto> result = notificationService.getUserNotifications("user-123", pageable);

        // Assert
        assertThat(result.getContent()).hasSize(1);
        NotificationDto dto = result.getContent().get(0);
        assertThat(dto.getId()).isEqualTo("notif-1");
        assertThat(dto.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(dto.getStatus()).isEqualTo(NotificationStatus.DELIVERED);
        assertThat(dto.getPriority()).isEqualTo(NotificationPriority.HIGH);
        assertThat(dto.getSubject()).isEqualTo("Full Subject");
        assertThat(dto.getMessage()).isEqualTo("Full Message");
        assertThat(dto.getTemplate()).isEqualTo("template-1");
        assertThat(dto.getTemplateData()).isEqualTo(templateData);
        assertThat(dto.getRecipient()).isEqualTo("test@example.com");
        assertThat(dto.getSender()).isEqualTo("sender@example.com");
        assertThat(dto.getCategory()).isEqualTo("test-category");
        assertThat(dto.getMetadata()).isEqualTo(metadata);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getSentAt()).isEqualTo(now);
        assertThat(dto.getDeliveredAt()).isEqualTo(now);
        assertThat(dto.getReadAt()).isEqualTo(now);
        verify(notificationRepository).findByUserId("user-123", pageable);
    }
}

