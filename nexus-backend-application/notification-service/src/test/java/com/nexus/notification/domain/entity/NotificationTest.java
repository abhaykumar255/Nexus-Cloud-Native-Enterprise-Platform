package com.nexus.notification.domain.entity;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Notification Entity Unit Tests")
class NotificationTest {

    @Test
    @DisplayName("Builder creates entity with all fields")
    void builder_createsEntityWithAllFields() {
        // Arrange
        Instant now = Instant.now();
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("key", "value");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "test");

        // Act
        Notification notification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .status(NotificationStatus.SENT)
            .priority(NotificationPriority.HIGH)
            .subject("Test Subject")
            .message("Test Message")
            .template("welcome-email")
            .templateData(templateData)
            .recipient("test@example.com")
            .sender("noreply@nexus.com")
            .category("user")
            .metadata(metadata)
            .errorMessage(null)
            .retryCount(0)
            .createdAt(now)
            .sentAt(now)
            .deliveredAt(now)
            .readAt(now)
            .expiresAt(now.plusSeconds(2592000))
            .build();

        // Assert
        assertThat(notification.getId()).isEqualTo("notif-123");
        assertThat(notification.getUserId()).isEqualTo("user-123");
        assertThat(notification.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.SENT);
        assertThat(notification.getPriority()).isEqualTo(NotificationPriority.HIGH);
        assertThat(notification.getSubject()).isEqualTo("Test Subject");
        assertThat(notification.getMessage()).isEqualTo("Test Message");
        assertThat(notification.getTemplate()).isEqualTo("welcome-email");
        assertThat(notification.getTemplateData()).containsEntry("key", "value");
        assertThat(notification.getRecipient()).isEqualTo("test@example.com");
        assertThat(notification.getSender()).isEqualTo("noreply@nexus.com");
        assertThat(notification.getCategory()).isEqualTo("user");
        assertThat(notification.getMetadata()).containsEntry("source", "test");
        assertThat(notification.getRetryCount()).isEqualTo(0);
        assertThat(notification.getCreatedAt()).isEqualTo(now);
        assertThat(notification.getSentAt()).isEqualTo(now);
        assertThat(notification.getDeliveredAt()).isEqualTo(now);
        assertThat(notification.getReadAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty entity")
    void noArgsConstructor_createsEmptyEntity() {
        // Act
        Notification notification = new Notification();

        // Assert
        assertThat(notification.getId()).isNull();
        assertThat(notification.getUserId()).isNull();
        assertThat(notification.getType()).isNull();
        assertThat(notification.getRetryCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        Notification notification = new Notification();
        Instant now = Instant.now();

        // Act
        notification.setId("notif-456");
        notification.setUserId("user-456");
        notification.setType(NotificationType.SMS);
        notification.setStatus(NotificationStatus.DELIVERED);
        notification.setPriority(NotificationPriority.URGENT);
        notification.setSubject("Urgent");
        notification.setMessage("Urgent message");
        notification.setRecipient("+1234567890");
        notification.setRetryCount(2);
        notification.setErrorMessage("Error occurred");
        notification.setCreatedAt(now);

        // Assert
        assertThat(notification.getId()).isEqualTo("notif-456");
        assertThat(notification.getUserId()).isEqualTo("user-456");
        assertThat(notification.getType()).isEqualTo(NotificationType.SMS);
        assertThat(notification.getStatus()).isEqualTo(NotificationStatus.DELIVERED);
        assertThat(notification.getPriority()).isEqualTo(NotificationPriority.URGENT);
        assertThat(notification.getSubject()).isEqualTo("Urgent");
        assertThat(notification.getMessage()).isEqualTo("Urgent message");
        assertThat(notification.getRecipient()).isEqualTo("+1234567890");
        assertThat(notification.getRetryCount()).isEqualTo(2);
        assertThat(notification.getErrorMessage()).isEqualTo("Error occurred");
        assertThat(notification.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        Notification notification1 = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .message("Test")
            .build();

        Notification notification2 = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .message("Test")
            .build();

        Notification notification3 = Notification.builder()
            .id("notif-456")
            .build();

        // Assert
        assertThat(notification1).isEqualTo(notification2);
        assertThat(notification1).isNotEqualTo(notification3);
        assertThat(notification1.hashCode()).isEqualTo(notification2.hashCode());
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        Notification notification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .subject("Test Subject")
            .build();

        // Act
        String result = notification.toString();

        // Assert
        assertThat(result).contains("notif-123");
        assertThat(result).contains("user-123");
        assertThat(result).contains("EMAIL");
        assertThat(result).contains("Test Subject");
    }

    @Test
    @DisplayName("All-args constructor creates full entity")
    void allArgsConstructor_createsFullEntity() {
        // Arrange
        Instant now = Instant.now();
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("key", "value");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "test");

        // Act
        Notification notification = new Notification(
            "notif-123",
            "user-123",
            NotificationType.EMAIL,
            NotificationStatus.SENT,
            NotificationPriority.HIGH,
            "Subject",
            "Message",
            "template",
            templateData,
            "recipient@example.com",
            "sender@example.com",
            "category",
            metadata,
            null,
            0,
            now,
            now,
            now,
            now,
            now.plusSeconds(3600)
        );

        // Assert
        assertThat(notification.getId()).isEqualTo("notif-123");
        assertThat(notification.getUserId()).isEqualTo("user-123");
        assertThat(notification.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(notification.getPriority()).isEqualTo(NotificationPriority.HIGH);
    }

    @Test
    @DisplayName("Setters for all timestamp fields work correctly")
    void timestampSetters_workCorrectly() {
        // Arrange
        Notification notification = new Notification();
        Instant now = Instant.now();
        Instant later = now.plusSeconds(100);

        // Act
        notification.setSentAt(now);
        notification.setDeliveredAt(later);
        notification.setReadAt(later.plusSeconds(50));
        notification.setExpiresAt(now.plusSeconds(2592000));

        // Assert
        assertThat(notification.getSentAt()).isEqualTo(now);
        assertThat(notification.getDeliveredAt()).isEqualTo(later);
        assertThat(notification.getReadAt()).isEqualTo(later.plusSeconds(50));
        assertThat(notification.getExpiresAt()).isEqualTo(now.plusSeconds(2592000));
    }

    @Test
    @DisplayName("Setters for template and metadata fields work correctly")
    void templateAndMetadataSetters_workCorrectly() {
        // Arrange
        Notification notification = new Notification();
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("name", "John");
        templateData.put("action", "login");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("ip", "192.168.1.1");
        metadata.put("userAgent", "Chrome");

        // Act
        notification.setTemplate("user-action-template");
        notification.setTemplateData(templateData);
        notification.setMetadata(metadata);
        notification.setSender("system@nexus.com");
        notification.setCategory("security");

        // Assert
        assertThat(notification.getTemplate()).isEqualTo("user-action-template");
        assertThat(notification.getTemplateData()).hasSize(2);
        assertThat(notification.getTemplateData()).containsEntry("name", "John");
        assertThat(notification.getTemplateData()).containsEntry("action", "login");
        assertThat(notification.getMetadata()).hasSize(2);
        assertThat(notification.getMetadata()).containsEntry("ip", "192.168.1.1");
        assertThat(notification.getMetadata()).containsEntry("userAgent", "Chrome");
        assertThat(notification.getSender()).isEqualTo("system@nexus.com");
        assertThat(notification.getCategory()).isEqualTo("security");
    }

    @Test
    @DisplayName("Builder with minimal fields works correctly")
    void builderWithMinimalFields_works() {
        // Act
        Notification notification = Notification.builder()
            .userId("user-minimal")
            .type(NotificationType.IN_APP)
            .message("Minimal notification")
            .build();

        // Assert
        assertThat(notification.getUserId()).isEqualTo("user-minimal");
        assertThat(notification.getType()).isEqualTo(NotificationType.IN_APP);
        assertThat(notification.getMessage()).isEqualTo("Minimal notification");
        assertThat(notification.getId()).isNull();
        assertThat(notification.getStatus()).isNull();
        assertThat(notification.getPriority()).isNull();
    }

    @Test
    @DisplayName("Equals handles null and different types correctly")
    void equals_handlesNullAndDifferentTypes() {
        // Arrange
        Notification notification = Notification.builder()
            .id("notif-123")
            .userId("user-123")
            .build();

        // Assert
        assertThat(notification).isNotEqualTo(null);
        assertThat(notification).isNotEqualTo("string");
        assertThat(notification).isEqualTo(notification);
    }

    @Test
    @DisplayName("Builder allows setting all notification types")
    void builder_allowsAllNotificationTypes() {
        // Act & Assert - EMAIL
        Notification email = Notification.builder().type(NotificationType.EMAIL).build();
        assertThat(email.getType()).isEqualTo(NotificationType.EMAIL);

        // SMS
        Notification sms = Notification.builder().type(NotificationType.SMS).build();
        assertThat(sms.getType()).isEqualTo(NotificationType.SMS);

        // PUSH
        Notification push = Notification.builder().type(NotificationType.PUSH).build();
        assertThat(push.getType()).isEqualTo(NotificationType.PUSH);

        // WEBSOCKET
        Notification websocket = Notification.builder().type(NotificationType.WEBSOCKET).build();
        assertThat(websocket.getType()).isEqualTo(NotificationType.WEBSOCKET);

        // IN_APP
        Notification inApp = Notification.builder().type(NotificationType.IN_APP).build();
        assertThat(inApp.getType()).isEqualTo(NotificationType.IN_APP);
    }

    @Test
    @DisplayName("Builder allows setting all notification statuses")
    void builder_allowsAllNotificationStatuses() {
        // Act & Assert
        Notification pending = Notification.builder().status(NotificationStatus.PENDING).build();
        assertThat(pending.getStatus()).isEqualTo(NotificationStatus.PENDING);

        Notification sent = Notification.builder().status(NotificationStatus.SENT).build();
        assertThat(sent.getStatus()).isEqualTo(NotificationStatus.SENT);

        Notification delivered = Notification.builder().status(NotificationStatus.DELIVERED).build();
        assertThat(delivered.getStatus()).isEqualTo(NotificationStatus.DELIVERED);

        Notification failed = Notification.builder().status(NotificationStatus.FAILED).build();
        assertThat(failed.getStatus()).isEqualTo(NotificationStatus.FAILED);

        Notification read = Notification.builder().status(NotificationStatus.READ).build();
        assertThat(read.getStatus()).isEqualTo(NotificationStatus.READ);
    }

    @Test
    @DisplayName("Builder allows setting all priority levels")
    void builder_allowsAllPriorityLevels() {
        // Act & Assert
        Notification low = Notification.builder().priority(NotificationPriority.LOW).build();
        assertThat(low.getPriority()).isEqualTo(NotificationPriority.LOW);

        Notification medium = Notification.builder().priority(NotificationPriority.MEDIUM).build();
        assertThat(medium.getPriority()).isEqualTo(NotificationPriority.MEDIUM);

        Notification high = Notification.builder().priority(NotificationPriority.HIGH).build();
        assertThat(high.getPriority()).isEqualTo(NotificationPriority.HIGH);

        Notification urgent = Notification.builder().priority(NotificationPriority.URGENT).build();
        assertThat(urgent.getPriority()).isEqualTo(NotificationPriority.URGENT);
    }
}

