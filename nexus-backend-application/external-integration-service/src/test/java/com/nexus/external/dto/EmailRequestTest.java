package com.nexus.external.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for EmailRequest DTO
 */
class EmailRequestTest {

    @Test
    @DisplayName("Should create EmailRequest with builder")
    void builderCreatesEmailRequest() {
        // Arrange
        List<String> cc = Arrays.asList("cc1@example.com", "cc2@example.com");

        // Act
        EmailRequest request = EmailRequest.builder()
                .to("recipient@example.com")
                .cc(cc)
                .subject("Test Email")
                .htmlContent("<h1>Hello</h1>")
                .textContent("Hello")
                .templateId("template-123")
                .build();

        // Assert
        assertThat(request.getTo()).isEqualTo("recipient@example.com");
        assertThat(request.getCc()).hasSize(2);
        assertThat(request.getSubject()).isEqualTo("Test Email");
        assertThat(request.getHtmlContent()).isEqualTo("<h1>Hello</h1>");
        assertThat(request.getTextContent()).isEqualTo("Hello");
        assertThat(request.getTemplateId()).isEqualTo("template-123");
    }

    @Test
    @DisplayName("Should create EmailRequest with no-args constructor")
    void noArgsConstructorWorks() {
        // Act
        EmailRequest request = new EmailRequest();
        request.setTo("test@example.com");
        request.setSubject("Subject");

        // Assert
        assertThat(request.getTo()).isEqualTo("test@example.com");
        assertThat(request.getSubject()).isEqualTo("Subject");
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCodeWork() {
        // Arrange
        EmailRequest request1 = EmailRequest.builder()
                .to("test@example.com")
                .subject("Test")
                .build();

        EmailRequest request2 = EmailRequest.builder()
                .to("test@example.com")
                .subject("Test")
                .build();

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toStringWorks() {
        // Arrange
        EmailRequest request = EmailRequest.builder()
                .to("test@example.com")
                .subject("Test")
                .build();

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("test@example.com", "Test");
    }
}

