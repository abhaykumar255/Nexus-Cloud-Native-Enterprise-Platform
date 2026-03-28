package com.nexus.external.adapter;

import com.nexus.external.dto.WeatherData;

/**
 * Weather API Client Interface (Adapter Pattern)
 */
public interface WeatherApiClient {
    
    /**
     * Get current weather for a city
     */
    WeatherData getCurrentWeather(String city);
}

