package com.nexus.external.service;

import com.nexus.common.constants.CacheKeys;
import com.nexus.external.adapter.NewsApiClient;
import com.nexus.external.adapter.PaymentService;
import com.nexus.external.adapter.WeatherApiClient;
import com.nexus.external.config.ExternalServiceProperties;
import com.nexus.external.dto.NewsItem;
import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;
import com.nexus.external.dto.WeatherData;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * External Integration Facade Service
 * Implements Facade Pattern with Resilience4j patterns
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalIntegrationFacade {

    private final WeatherApiClient weatherApiClient;
    private final NewsApiClient newsApiClient;
    private final PaymentService paymentService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExternalServiceProperties properties;

    /**
     * Get weather with circuit breaker, retry, and caching
     */
    @CircuitBreaker(name = "weatherApi", fallbackMethod = "weatherFallback")
    @Retry(name = "weatherApi")
    @TimeLimiter(name = "weatherApi")
    @Bulkhead(name = "weatherApi")
    public WeatherData getWeather(String city) {
        log.info("Getting weather for city: {}", city);

        // Check cache first
        String cacheKey = CacheKeys.EXT_WEATHER_PREFIX + city.toLowerCase();
        WeatherData cached = (WeatherData) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("Weather cache hit for city: {}", city);
            return cached;
        }

        // Call external API
        WeatherData weather = weatherApiClient.getCurrentWeather(city);

        // Cache result using configured TTL
        redisTemplate.opsForValue().set(cacheKey, weather,
                Duration.ofMinutes(properties.getCache().getWeatherTtlMinutes()));
        
        return weather;
    }

    /**
     * Fallback for weather API
     */
    private WeatherData weatherFallback(String city, Throwable t) {
        log.warn("Weather API fallback triggered for city: {}, error: {}", city, t.getMessage());

        // Try cache as fallback
        String cacheKey = CacheKeys.EXT_WEATHER_PREFIX + city.toLowerCase();
        WeatherData cached = (WeatherData) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("Returning stale weather data from cache");
            return cached;
        }

        // Return unavailable
        return WeatherData.unavailable(city);
    }

    /**
     * Get news with circuit breaker and caching
     */
    @CircuitBreaker(name = "newsApi", fallbackMethod = "newsFallback")
    @Retry(name = "newsApi")
    @TimeLimiter(name = "newsApi")
    public List<NewsItem> getNews(String category, int limit) {
        log.info("Getting news: category={}, limit={}", category, limit);

        // Check cache
        String cacheKey = CacheKeys.EXT_NEWS_PREFIX + category + ":" + limit;
        List<NewsItem> cached = (List<NewsItem>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("News cache hit for category: {}", category);
            return cached;
        }

        // Call external API
        List<NewsItem> news = newsApiClient.getLatestNews(category, limit);

        // Cache using configured TTL
        redisTemplate.opsForValue().set(cacheKey, news,
                Duration.ofMinutes(properties.getCache().getNewsTtlMinutes()));
        
        return news;
    }

    /**
     * Fallback for news API
     */
    private List<NewsItem> newsFallback(String category, int limit, Throwable t) {
        log.warn("News API fallback triggered: category={}, error: {}", category, t.getMessage());
        
        // Try cache
        String cacheKey = "ext:news:" + category + ":" + limit;
        List<NewsItem> cached = (List<NewsItem>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        // Return empty list gracefully
        return Collections.emptyList();
    }

    /**
     * Process payment (uses mock service)
     */
    @CircuitBreaker(name = "paymentApi", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentApi")
    public PaymentResult processPayment(PaymentRequest request) {
        log.info("Processing payment: amount={} {}", request.getAmount(), request.getCurrency());
        return paymentService.processPayment(request);
    }

    /**
     * Fallback for payment
     */
    private PaymentResult paymentFallback(PaymentRequest request, Throwable t) {
        log.error("Payment API fallback triggered: {}", t.getMessage());
        return PaymentResult.failed("PAYMENT_SERVICE_UNAVAILABLE");
    }
}

