package com.nexus.external.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for PaymentResult DTO
 */
class PaymentResultTest {

    @Test
    @DisplayName("Should create succeeded PaymentResult")
    void succeededCreatesSuccessfulResult() {
        // Act
        PaymentResult result = PaymentResult.succeeded("txn-123");

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTransactionId()).isEqualTo("txn-123");
        assertThat(result.getStatus()).isEqualTo("SUCCESS");
        assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    @DisplayName("Should create failed PaymentResult")
    void failedCreatesFailedResult() {
        // Act
        PaymentResult result = PaymentResult.failed("Insufficient funds");

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getStatus()).isEqualTo("FAILED");
        assertThat(result.getErrorMessage()).isEqualTo("Insufficient funds");
        assertThat(result.getTransactionId()).isNull();
    }

    @Test
    @DisplayName("Should create PaymentResult with builder")
    void builderCreatesPaymentResult() {
        // Act
        PaymentResult result = PaymentResult.builder()
                .success(true)
                .transactionId("txn-456")
                .status("PENDING")
                .errorMessage(null)
                .build();

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTransactionId()).isEqualTo("txn-456");
        assertThat(result.getStatus()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCodeWork() {
        // Arrange
        PaymentResult result1 = PaymentResult.succeeded("txn-789");
        PaymentResult result2 = PaymentResult.succeeded("txn-789");

        // Assert
        assertThat(result1).isEqualTo(result2);
        assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toStringWorks() {
        // Arrange
        PaymentResult result = PaymentResult.succeeded("txn-999");

        // Act
        String str = result.toString();

        // Assert
        assertThat(str).contains("txn-999", "SUCCESS");
    }
}

