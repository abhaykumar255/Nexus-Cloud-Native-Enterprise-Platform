package com.nexus.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Weather Data DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData implements Serializable {
    
    private String city;
    private String country;
    private Double temperature;
    private Double feelsLike;
    private String description;
    private String icon;
    private Integer humidity;
    private Double windSpeed;
    private Long timestamp;
    private boolean available;
    
    public static WeatherData unavailable(String city) {
        return WeatherData.builder()
                .city(city)
                .available(false)
                .description("Weather data unavailable")
                .build();
    }
}

