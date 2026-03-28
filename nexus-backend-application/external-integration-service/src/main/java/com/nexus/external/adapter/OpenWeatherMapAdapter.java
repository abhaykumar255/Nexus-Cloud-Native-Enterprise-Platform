package com.nexus.external.adapter;

import com.nexus.external.dto.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * OpenWeatherMap API Adapter Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenWeatherMapAdapter implements WeatherApiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${external.api.weather.base-url}")
    private String baseUrl;

    @Value("${external.api.weather.api-key}")
    private String apiKey;

    @Override
    public WeatherData getCurrentWeather(String city) {
        try {
            log.info("Fetching weather for city: {}", city);

            Map<String, Object> response = webClientBuilder.build()
                    .get()
                    .uri(baseUrl + "/weather?q={city}&appid={apiKey}&units=metric", city, apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                return WeatherData.unavailable(city);
            }

            Map<String, Object> main = (Map<String, Object>) response.get("main");
            Map<String, Object> wind = (Map<String, Object>) response.get("wind");
            Map<String, Object> sys = (Map<String, Object>) response.get("sys");
            
            return WeatherData.builder()
                    .city(city)
                    .country((String) sys.get("country"))
                    .temperature(((Number) main.get("temp")).doubleValue())
                    .feelsLike(((Number) main.get("feels_like")).doubleValue())
                    .description(((Map<String, Object>) ((java.util.List<?>) response.get("weather")).get(0)).get("description").toString())
                    .humidity(((Number) main.get("humidity")).intValue())
                    .windSpeed(((Number) wind.get("speed")).doubleValue())
                    .timestamp(System.currentTimeMillis())
                    .available(true)
                    .build();

        } catch (Exception e) {
            log.error("Failed to fetch weather for city: {}", city, e);
            throw new RuntimeException("Weather API call failed", e);
        }
    }
}

