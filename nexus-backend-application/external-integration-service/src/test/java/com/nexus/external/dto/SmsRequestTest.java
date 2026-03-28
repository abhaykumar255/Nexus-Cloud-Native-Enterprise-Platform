package com.nexus.external.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for SmsRequest DTO
 */
class SmsRequestTest {

    @Test
    @DisplayName("Should create SmsRequest with builder")
    void builderCreatesSmsRequest() {
        // Act
        SmsRequest request = SmsRequest.builder()
                .to("+1234567890")
                .message("Test message")
                .build();

        // Assert
        assertThat(request.getTo()).isEqualTo("+1234567890");
        assertThat(request.getMessage()).isEqualTo("Test message");
    }

    @Test
    @DisplayName("Should create SmsRequest with no-args constructor")
    void noArgsConstructorWorks() {
        // Act
        SmsRequest request = new SmsRequest();
        request.setTo("+1234567890");
        request.setMessage("Hello");

        // Assert
        assertThat(request.getTo()).isEqualTo("+1234567890");
        assertThat(request.getMessage()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("Should create SmsRequest with all-args constructor")
    void allArgsConstructorWorks() {
        // Act
        SmsRequest request = new SmsRequest("+1234567890", "Test message");

        // Assert
        assertThat(request.getTo()).isEqualTo("+1234567890");
        assertThat(request.getMessage()).isEqualTo("Test message");
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCodeWork() {
        // Arrange
        SmsRequest request1 = SmsRequest.builder()
                .to("+1234567890")
                .message("Test")
                .build();

        SmsRequest request2 = SmsRequest.builder()
                .to("+1234567890")
                .message("Test")
                .build();

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toStringWorks() {
        // Arrange
        SmsRequest request = SmsRequest.builder()
                .to("+1234567890")
                .message("Test")
                .build();

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("+1234567890", "Test");
    }
}

