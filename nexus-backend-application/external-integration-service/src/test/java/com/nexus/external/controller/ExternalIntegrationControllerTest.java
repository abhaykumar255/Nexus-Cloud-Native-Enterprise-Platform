package com.nexus.external.controller;

import com.nexus.external.dto.NewsItem;
import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;
import com.nexus.external.dto.WeatherData;
import com.nexus.external.service.ExternalIntegrationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ExternalIntegrationController
 */
@ExtendWith(MockitoExtension.class)
class ExternalIntegrationControllerTest {

    @Mock
    private ExternalIntegrationFacade integrationFacade;

    @InjectMocks
    private ExternalIntegrationController controller;

    private WeatherData sampleWeather;
    private List<NewsItem> sampleNews;
    private PaymentRequest samplePaymentRequest;

    @BeforeEach
    void setUp() {
        sampleWeather = WeatherData.builder()
                .city("London")
                .temperature(20.0)
                .description("Sunny")
                .humidity(60)
                .build();

        sampleNews = Arrays.asList(
                NewsItem.builder().title("News 1").source("BBC").build(),
                NewsItem.builder().title("News 2").source("CNN").build()
        );

        samplePaymentRequest = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .paymentMethodId("pm-456")
                .build();
    }

    @Test
    @DisplayName("Should get weather successfully")
    void getWeather_success() {
        // Arrange
        when(integrationFacade.getWeather("London")).thenReturn(sampleWeather);

        // Act
        ResponseEntity<?> response = controller.getWeather("London", "user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should get news successfully")
    void getNews_success() {
        // Arrange
        when(integrationFacade.getNews("technology", 10)).thenReturn(sampleNews);

        // Act
        ResponseEntity<?> response = controller.getNews("technology", 10, "user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Should process payment successfully")
    void processPayment_success() {
        // Arrange
        PaymentResult successResult = PaymentResult.succeeded("txn-789");
        when(integrationFacade.processPayment(samplePaymentRequest)).thenReturn(successResult);

        // Act
        ResponseEntity<?> response = controller.processPayment(samplePaymentRequest, "user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Should return bad request when payment fails")
    void processPayment_failure() {
        // Arrange
        PaymentResult failedResult = PaymentResult.failed("Insufficient funds");
        when(integrationFacade.processPayment(samplePaymentRequest)).thenReturn(failedResult);

        // Act
        ResponseEntity<?> response = controller.processPayment(samplePaymentRequest, "user-123");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Health check should return OK")
    void health_returnsOk() {
        // Act
        ResponseEntity<String> response = controller.health();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("External Integration Service is running");
    }
}

