package com.nexus.external.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for PaymentRequest DTO
 */
class PaymentRequestTest {

    @Test
    @DisplayName("Should create PaymentRequest with builder")
    void builderCreatesPaymentRequest() {
        // Act
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .paymentMethodId("pm-456")
                .description("Test payment")
                .build();

        // Assert
        assertThat(request.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(request.getCurrency()).isEqualTo("USD");
        assertThat(request.getCustomerId()).isEqualTo("cust-123");
        assertThat(request.getPaymentMethodId()).isEqualTo("pm-456");
        assertThat(request.getDescription()).isEqualTo("Test payment");
    }

    @Test
    @DisplayName("Should create PaymentRequest with no-args constructor and setters")
    void noArgsConstructorAndSettersWork() {
        // Act
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("50.00"));
        request.setCurrency("EUR");
        request.setCustomerId("cust-789");
        request.setPaymentMethodId("pm-012");
        request.setDescription("Another payment");

        // Assert
        assertThat(request.getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(request.getCurrency()).isEqualTo("EUR");
        assertThat(request.getCustomerId()).isEqualTo("cust-789");
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCodeWork() {
        // Arrange
        PaymentRequest request1 = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .build();

        PaymentRequest request2 = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .build();

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toStringWorks() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).contains("100.00", "USD");
    }
}

