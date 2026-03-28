package com.nexus.notification.dto;

import com.nexus.notification.domain.enums.NotificationPriority;
import com.nexus.notification.domain.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SendNotificationRequest Unit Tests")
class SendNotificationRequestTest {

    @Test
    @DisplayName("All-args constructor creates request with all fields")
    void allArgsConstructor_createsRequestWithAllFields() {
        // Arrange
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("name", "John");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "test");

        // Act
        SendNotificationRequest request = new SendNotificationRequest(
            "user-123",
            NotificationType.EMAIL,
            NotificationPriority.HIGH,
            "Test Subject",
            "Test Message",
            "welcome-email",
            templateData,
            "test@example.com",
            "user",
            metadata
        );

        // Assert
        assertThat(request.getUserId()).isEqualTo("user-123");
        assertThat(request.getType()).isEqualTo(NotificationType.EMAIL);
        assertThat(request.getPriority()).isEqualTo(NotificationPriority.HIGH);
        assertThat(request.getSubject()).isEqualTo("Test Subject");
        assertThat(request.getMessage()).isEqualTo("Test Message");
        assertThat(request.getTemplate()).isEqualTo("welcome-email");
        assertThat(request.getTemplateData()).containsEntry("name", "John");
        assertThat(request.getRecipient()).isEqualTo("test@example.com");
        assertThat(request.getCategory()).isEqualTo("user");
        assertThat(request.getMetadata()).containsEntry("source", "test");
    }

    @Test
    @DisplayName("No-args constructor creates empty request")
    void noArgsConstructor_createsEmptyRequest() {
        // Act
        SendNotificationRequest request = new SendNotificationRequest();

        // Assert
        assertThat(request.getUserId()).isNull();
        assertThat(request.getType()).isNull();
        assertThat(request.getMessage()).isNull();
        assertThat(request.getPriority()).isEqualTo(NotificationPriority.MEDIUM); // default
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();

        // Act
        request.setUserId("user-456");
        request.setType(NotificationType.SMS);
        request.setPriority(NotificationPriority.URGENT);
        request.setSubject("Urgent");
        request.setMessage("Urgent message");
        request.setRecipient("+1234567890");
        request.setCategory("alert");

        // Assert
        assertThat(request.getUserId()).isEqualTo("user-456");
        assertThat(request.getType()).isEqualTo(NotificationType.SMS);
        assertThat(request.getPriority()).isEqualTo(NotificationPriority.URGENT);
        assertThat(request.getSubject()).isEqualTo("Urgent");
        assertThat(request.getMessage()).isEqualTo("Urgent message");
        assertThat(request.getRecipient()).isEqualTo("+1234567890");
        assertThat(request.getCategory()).isEqualTo("alert");
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        // Arrange
        SendNotificationRequest request1 = new SendNotificationRequest();
        request1.setUserId("user-123");
        request1.setType(NotificationType.EMAIL);
        request1.setMessage("Test");

        SendNotificationRequest request2 = new SendNotificationRequest();
        request2.setUserId("user-123");
        request2.setType(NotificationType.EMAIL);
        request2.setMessage("Test");

        SendNotificationRequest request3 = new SendNotificationRequest();
        request3.setUserId("user-456");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();
        request.setUserId("user-123");
        request.setType(NotificationType.EMAIL);
        request.setSubject("Test Subject");
        request.setMessage("Test Message");

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("user-123");
        assertThat(result).contains("EMAIL");
        assertThat(result).contains("Test Subject");
        assertThat(result).contains("Test Message");
    }

    @Test
    @DisplayName("Default priority is MEDIUM")
    void defaultPriority_isMedium() {
        // Act
        SendNotificationRequest request = new SendNotificationRequest();

        // Assert
        assertThat(request.getPriority()).isEqualTo(NotificationPriority.MEDIUM);
    }

    @Test
    @DisplayName("Additional setter combinations work correctly")
    void additionalSetters_workCorrectly() {
        // Arrange
        SendNotificationRequest request = new SendNotificationRequest();
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("var1", "value1");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", "value1");

        // Act
        request.setTemplate("test-template");
        request.setTemplateData(templateData);
        request.setMetadata(metadata);

        // Assert
        assertThat(request.getTemplate()).isEqualTo("test-template");
        assertThat(request.getTemplateData()).containsEntry("var1", "value1");
        assertThat(request.getMetadata()).containsEntry("key1", "value1");
    }

    @Test
    @DisplayName("Test all notification types in request")
    void testAllNotificationTypesInRequest() {
        for (NotificationType type : NotificationType.values()) {
            SendNotificationRequest request = new SendNotificationRequest();
            request.setType(type);
            assertThat(request.getType()).isEqualTo(type);
        }
    }

    @Test
    @DisplayName("Test all priorities in request")
    void testAllPrioritiesInRequest() {
        for (NotificationPriority priority : NotificationPriority.values()) {
            SendNotificationRequest request = new SendNotificationRequest();
            request.setPriority(priority);
            assertThat(request.getPriority()).isEqualTo(priority);
        }
    }

    @Test
    @DisplayName("Equals handles same object and null")
    void equalsHandlesSameObjectAndNull() {
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.EMAIL);

        assertThat(request).isEqualTo(request);
        assertThat(request).isNotEqualTo(null);
        assertThat(request).isNotEqualTo("not a request");
    }

    @Test
    @DisplayName("toString with empty maps works")
    void toStringWithEmptyMaps_works() {
        SendNotificationRequest request = new SendNotificationRequest();
        request.setType(NotificationType.PUSH);
        request.setMessage("Test");

        String str = request.toString();
        assertThat(str).contains("PUSH");
        assertThat(str).contains("Test");
    }
}

