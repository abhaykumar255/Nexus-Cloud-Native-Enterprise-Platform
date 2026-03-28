package com.nexus.external.service;

import com.nexus.external.adapter.NewsApiClient;
import com.nexus.external.adapter.PaymentService;
import com.nexus.external.adapter.WeatherApiClient;
import com.nexus.external.config.ExternalServiceProperties;
import com.nexus.external.dto.NewsItem;
import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;
import com.nexus.external.dto.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ExternalIntegrationFacade
 */
@ExtendWith(MockitoExtension.class)
class ExternalIntegrationFacadeTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @Mock
    private NewsApiClient newsApiClient;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ExternalServiceProperties properties;

    @Mock
    private ExternalServiceProperties.CacheConfig cacheConfig;

    @InjectMocks
    private ExternalIntegrationFacade integrationFacade;

    @Test
    @DisplayName("Should get weather from API when cache miss")
    void getWeather_cacheMiss() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(properties.getCache()).thenReturn(cacheConfig);
        when(cacheConfig.getWeatherTtlMinutes()).thenReturn(30);

        WeatherData weatherData = WeatherData.builder()
                .city("London")
                .temperature(20.0)
                .description("Sunny")
                .build();

        when(valueOperations.get(anyString())).thenReturn(null);
        when(weatherApiClient.getCurrentWeather("London")).thenReturn(weatherData);

        // Act
        WeatherData result = integrationFacade.getWeather("London");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("London");
        verify(weatherApiClient).getCurrentWeather("London");
        verify(valueOperations).set(anyString(), eq(weatherData), any(Duration.class));
    }

    @Test
    @DisplayName("Should get weather from cache when cache hit")
    void getWeather_cacheHit() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        WeatherData cachedWeather = WeatherData.builder()
                .city("Paris")
                .temperature(15.0)
                .description("Cloudy")
                .build();

        when(valueOperations.get(anyString())).thenReturn(cachedWeather);

        // Act
        WeatherData result = integrationFacade.getWeather("Paris");

        // Assert
        assertThat(result).isEqualTo(cachedWeather);
        verify(weatherApiClient, never()).getCurrentWeather(anyString());
    }

    @Test
    @DisplayName("Should get news from API when cache miss")
    void getNews_cacheMiss() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(properties.getCache()).thenReturn(cacheConfig);
        when(cacheConfig.getNewsTtlMinutes()).thenReturn(15);

        List<NewsItem> newsItems = Arrays.asList(
                NewsItem.builder().title("News 1").source("BBC").build(),
                NewsItem.builder().title("News 2").source("CNN").build()
        );

        when(valueOperations.get(anyString())).thenReturn(null);
        when(newsApiClient.getLatestNews("technology", 10)).thenReturn(newsItems);

        // Act
        List<NewsItem> result = integrationFacade.getNews("technology", 10);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("News 1");
        verify(newsApiClient).getLatestNews("technology", 10);
        verify(valueOperations).set(anyString(), eq(newsItems), any(Duration.class));
    }

    @Test
    @DisplayName("Should get news from cache when cache hit")
    void getNews_cacheHit() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        List<NewsItem> cachedNews = Arrays.asList(
                NewsItem.builder().title("Cached News").source("Reuters").build()
        );

        when(valueOperations.get(anyString())).thenReturn(cachedNews);

        // Act
        List<NewsItem> result = integrationFacade.getNews("sports", 5);

        // Assert
        assertThat(result).isEqualTo(cachedNews);
        verify(newsApiClient, never()).getLatestNews(anyString(), anyInt());
    }

    @Test
    @DisplayName("Should process payment successfully")
    void processPayment_success() {
        // Arrange
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .customerId("cust-123")
                .build();

        PaymentResult expectedResult = PaymentResult.succeeded("txn-456");
        when(paymentService.processPayment(request)).thenReturn(expectedResult);

        // Act
        PaymentResult result = integrationFacade.processPayment(request);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getTransactionId()).isEqualTo("txn-456");
        verify(paymentService).processPayment(request);
    }
}

