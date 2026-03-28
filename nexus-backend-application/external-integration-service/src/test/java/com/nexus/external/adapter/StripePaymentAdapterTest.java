package com.nexus.external.adapter;

import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StripePaymentAdapter
 */
@ExtendWith(MockitoExtension.class)
class StripePaymentAdapterTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private StripePaymentAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new StripePaymentAdapter(webClientBuilder);
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "baseUrl", "https://api.stripe.com/v1");
    }

    @Test
    @DisplayName("Should process payment successfully")
    void processPayment_success() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .paymentMethodId("pm-456")
                .description("Test payment")
                .build();

        Map<String, Object> stripeResponse = new HashMap<>();
        stripeResponse.put("id", "pi_123");
        stripeResponse.put("status", "succeeded");

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(stripeResponse));

        // Act
        PaymentResult result = adapter.processPayment(request);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTransactionId()).isEqualTo("pi_123");
        verify(webClient).post();
    }

    @Test
    @DisplayName("Should handle payment failure")
    void processPayment_failure() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .build();

        Map<String, Object> stripeResponse = new HashMap<>();
        stripeResponse.put("id", "pi_456");
        stripeResponse.put("status", "failed");

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(stripeResponse));

        // Act
        PaymentResult result = adapter.processPayment(request);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).contains("PAYMENT_FAILED");
    }

    @Test
    @DisplayName("Should handle null response from Stripe")
    void processPayment_nullResponse() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        // Act
        PaymentResult result = adapter.processPayment(request);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrorMessage()).isEqualTo("PAYMENT_FAILED");
    }

    @Test
    @DisplayName("Should throw exception on Stripe API error")
    void processPayment_apiError() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new RuntimeException("API error"));

        // Act & Assert
        assertThatThrownBy(() -> adapter.processPayment(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stripe payment failed");
    }
}

