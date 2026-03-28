package com.nexus.notification.dto;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationStatus;
import com.nexus.notification.domain.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NotificationDto Unit Tests")
class NotificationDtoTest {

    @Test
    @DisplayName("Builder creates DTO with all fields")
    void builder_createsDtoWithAllFields() {
        // Arrange
        Instant now = Instant.now();
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("key", "value");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "test");

        // Act
        NotificationDto dto = NotificationDto.builder()
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
            .createdAt(now)
            .sentAt(now)
            .deliveredAt(now)
            .readAt(now)
            .build();

        // Assert
        assertThat(dto.getId()).isEqualTo("notif-123");
        assertThat(dto.getUserId()).isEqualTo("user-123");
        assertThat(dto.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(dto.getStatus()).isEqualTo(NotificationStatus.SENT);
        assertThat(dto.getPriority()).isEqualTo(NotificationPriority.HIGH);
        assertThat(dto.getSubject()).isEqualTo("Test Subject");
        assertThat(dto.getMessage()).isEqualTo("Test Message");
        assertThat(dto.getTemplate()).isEqualTo("welcome-email");
        assertThat(dto.getTemplateData()).containsEntry("key", "value");
        assertThat(dto.getRecipient()).isEqualTo("test@example.com");
        assertThat(dto.getSender()).isEqualTo("noreply@nexus.com");
        assertThat(dto.getCategory()).isEqualTo("user");
        assertThat(dto.getMetadata()).containsEntry("source", "test");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getSentAt()).isEqualTo(now);
        assertThat(dto.getDeliveredAt()).isEqualTo(now);
        assertThat(dto.getReadAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty DTO")
    void noArgsConstructor_createsEmptyDto() {
        // Act
        NotificationDto dto = new NotificationDto();

        // Assert
        assertThat(dto.getId()).isNull();
        assertThat(dto.getUserId()).isNull();
        assertThat(dto.getType()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        NotificationDto dto = new NotificationDto();
        Instant now = Instant.now();

        // Act
        dto.setId("notif-456");
        dto.setUserId("user-456");
        dto.setType(NotificationType.SMS);
        dto.setStatus(NotificationStatus.DELIVERED);
        dto.setPriority(NotificationPriority.URGENT);
        dto.setSubject("Urgent Message");
        dto.setMessage("Important update");
        dto.setCreatedAt(now);

        // Assert
        assertThat(dto.getId()).isEqualTo("notif-456");
        assertThat(dto.getUserId()).isEqualTo("user-456");
        assertThat(dto.getType()).isEqualTo(NotificationType.SMS);
        assertThat(dto.getStatus()).isEqualTo(NotificationStatus.DELIVERED);
        assertThat(dto.getPriority()).isEqualTo(NotificationPriority.URGENT);
        assertThat(dto.getSubject()).isEqualTo("Urgent Message");
        assertThat(dto.getMessage()).isEqualTo("Important update");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        NotificationDto dto1 = NotificationDto.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .message("Test")
            .build();

        NotificationDto dto2 = NotificationDto.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .message("Test")
            .build();

        NotificationDto dto3 = NotificationDto.builder()
            .id("notif-456")
            .build();

        // Assert
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        NotificationDto dto = NotificationDto.builder()
            .id("notif-123")
            .userId("user-123")
            .type(NotificationType.EMAIL)
            .subject("Test Subject")
            .build();

        // Act
        String result = dto.toString();

        // Assert
        assertThat(result).contains("notif-123");
        assertThat(result).contains("user-123");
        assertThat(result).contains("EMAIL");
        assertThat(result).contains("Test Subject");
    }

    @Test
    @DisplayName("All-args constructor works")
    void allArgsConstructor_works() {
        Instant now = Instant.now();
        Map<String, Object> templateData = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();

        NotificationDto dto = new NotificationDto(
            "notif-1", "user-1", NotificationType.PUSH, NotificationStatus.PENDING,
            NotificationPriority.LOW, "Subj", "Msg", "tmpl", templateData,
            "rec", "send", "cat", metadata, now, now, now, now
        );

        assertThat(dto.getId()).isEqualTo("notif-1");
        assertThat(dto.getType()).isEqualTo(NotificationType.PUSH);
    }

    @Test
    @DisplayName("Additional setters work")
    void additionalSetters_work() {
        NotificationDto dto = new NotificationDto();
        Instant now = Instant.now();
        Map<String, Object> data = new HashMap<>();

        dto.setTemplate("template");
        dto.setTemplateData(data);
        dto.setRecipient("recipient");
        dto.setSender("sender");
        dto.setCategory("category");
        dto.setMetadata(data);
        dto.setSentAt(now);
        dto.setDeliveredAt(now);
        dto.setReadAt(now);

        assertThat(dto.getTemplate()).isEqualTo("template");
        assertThat(dto.getRecipient()).isEqualTo("recipient");
        assertThat(dto.getSender()).isEqualTo("sender");
        assertThat(dto.getCategory()).isEqualTo("category");
        assertThat(dto.getSentAt()).isEqualTo(now);
        assertThat(dto.getDeliveredAt()).isEqualTo(now);
        assertThat(dto.getReadAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Builder with partial fields works")
    void builder_withPartialFields_works() {
        NotificationDto dto = NotificationDto.builder()
            .id("notif-999")
            .message("Minimal message")
            .build();

        assertThat(dto.getId()).isEqualTo("notif-999");
        assertThat(dto.getMessage()).isEqualTo("Minimal message");
        assertThat(dto.getType()).isNull();
    }

    @Test
    @DisplayName("Equals handles null and same object")
    void equals_handlesNullAndSameObject() {
        NotificationDto dto = NotificationDto.builder()
            .id("notif-123")
            .build();

        assertThat(dto).isEqualTo(dto);
        assertThat(dto).isNotEqualTo(null);
        assertThat(dto).isNotEqualTo("not a dto");
    }

    @Test
    @DisplayName("Test all notification types")
    void testAllNotificationTypes() {
        for (NotificationType type : NotificationType.values()) {
            NotificationDto dto = NotificationDto.builder()
                .type(type)
                .build();
            assertThat(dto.getType()).isEqualTo(type);
        }
    }

    @Test
    @DisplayName("Test all notification statuses")
    void testAllNotificationStatuses() {
        for (NotificationStatus status : NotificationStatus.values()) {
            NotificationDto dto = NotificationDto.builder()
                .status(status)
                .build();
            assertThat(dto.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Test all priorities")
    void testAllPriorities() {
        for (NotificationPriority priority : NotificationPriority.values()) {
            NotificationDto dto = NotificationDto.builder()
                .priority(priority)
                .build();
            assertThat(dto.getPriority()).isEqualTo(priority);
        }
    }
}

