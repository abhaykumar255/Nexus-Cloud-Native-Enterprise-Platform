package com.nexus.external.adapter;

import com.nexus.external.dto.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for OpenWeatherMapAdapter
 */
@ExtendWith(MockitoExtension.class)
class OpenWeatherMapAdapterTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private OpenWeatherMapAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new OpenWeatherMapAdapter(webClientBuilder);
        ReflectionTestUtils.setField(adapter, "baseUrl", "https://api.openweathermap.org/data/2.5");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
    }

    @Test
    @DisplayName("Should get weather successfully")
    void getCurrentWeather_success() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 20.5);
        main.put("feels_like", 19.0);
        main.put("humidity", 65);
        
        Map<String, Object> wind = new HashMap<>();
        wind.put("speed", 10.5);
        
        Map<String, Object> sys = new HashMap<>();
        sys.put("country", "GB");
        
        Map<String, Object> weatherItem = new HashMap<>();
        weatherItem.put("description", "Partly cloudy");
        
        response.put("main", main);
        response.put("wind", wind);
        response.put("sys", sys);
        response.put("weather", List.of(weatherItem));

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        // Act
        WeatherData result = adapter.getCurrentWeather("London");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("London");
        assertThat(result.getCountry()).isEqualTo("GB");
        assertThat(result.getTemperature()).isEqualTo(20.5);
        assertThat(result.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Should return unavailable when API returns null")
    void getCurrentWeather_nullResponse() {
        // Arrange
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        // Act
        WeatherData result = adapter.getCurrentWeather("InvalidCity");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCity()).isEqualTo("InvalidCity");
        assertThat(result.isAvailable()).isFalse();
        assertThat(result.getDescription()).isEqualTo("Weather data unavailable");
    }

    @Test
    @DisplayName("Should throw exception on API error")
    void getCurrentWeather_apiError() {
        // Arrange
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new RuntimeException("API error"));

        // Act & Assert
        assertThatThrownBy(() -> adapter.getCurrentWeather("London"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Weather API call failed");
    }
}

