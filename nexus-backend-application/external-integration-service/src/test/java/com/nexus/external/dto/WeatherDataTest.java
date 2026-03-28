package com.nexus.external.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for WeatherData DTO
 */
class WeatherDataTest {

    @Test
    @DisplayName("Should create unavailable WeatherData")
    void unavailableCreatesUnavailableWeather() {
        // Act
        WeatherData weather = WeatherData.unavailable("London");

        // Assert
        assertThat(weather.getCity()).isEqualTo("London");
        assertThat(weather.isAvailable()).isFalse();
        assertThat(weather.getDescription()).isEqualTo("Weather data unavailable");
    }

    @Test
    @DisplayName("Should create WeatherData with builder")
    void builderCreatesWeatherData() {
        // Act
        WeatherData weather = WeatherData.builder()
                .city("Paris")
                .country("FR")
                .temperature(20.5)
                .feelsLike(19.0)
                .description("Partly cloudy")
                .humidity(65)
                .windSpeed(10.5)
                .timestamp(1234567890L)
                .available(true)
                .build();

        // Assert
        assertThat(weather.getCity()).isEqualTo("Paris");
        assertThat(weather.getCountry()).isEqualTo("FR");
        assertThat(weather.getTemperature()).isEqualTo(20.5);
        assertThat(weather.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCodeWork() {
        // Arrange
        WeatherData weather1 = WeatherData.builder()
                .city("Tokyo")
                .temperature(25.0)
                .available(true)
                .build();

        WeatherData weather2 = WeatherData.builder()
                .city("Tokyo")
                .temperature(25.0)
                .available(true)
                .build();

        // Assert
        assertThat(weather1).isEqualTo(weather2);
        assertThat(weather1.hashCode()).isEqualTo(weather2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toStringWorks() {
        // Arrange
        WeatherData weather = WeatherData.builder()
                .city("Berlin")
                .temperature(15.0)
                .build();

        // Act
        String result = weather.toString();

        // Assert
        assertThat(result).contains("Berlin", "15.0");
    }
}

