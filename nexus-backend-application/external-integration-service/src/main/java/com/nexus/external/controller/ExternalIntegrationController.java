package com.nexus.external.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.external.dto.NewsItem;
import com.nexus.external.dto.PaymentRequest;
import com.nexus.external.dto.PaymentResult;
import com.nexus.external.dto.WeatherData;
import com.nexus.external.service.ExternalIntegrationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * External Integration REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/external")
@RequiredArgsConstructor
public class ExternalIntegrationController {

    private final ExternalIntegrationFacade integrationFacade;

    /**
     * Get current weather
     * GET /api/v1/external/weather?city=London
     */
    @GetMapping("/weather")
    public ResponseEntity<ApiResponse<WeatherData>> getWeather(
            @RequestParam String city,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        log.info("Weather request: city={}, userId={}", city, userId);

        WeatherData weather = integrationFacade.getWeather(city);

        return ResponseEntity.ok(ApiResponse.success("Weather data retrieved", weather));
    }

    /**
     * Get latest news
     * GET /api/v1/external/news?category=technology&limit=10
     */
    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<NewsItem>>> getNews(
            @RequestParam(defaultValue = "general") String category,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        log.info("News request: category={}, limit={}, userId={}", category, limit, userId);

        List<NewsItem> news = integrationFacade.getNews(category, limit);

        return ResponseEntity.ok(ApiResponse.success("News retrieved", news));
    }

    /**
     * Process payment (mock)
     * POST /api/v1/external/payment
     */
    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<PaymentResult>> processPayment(
            @RequestBody PaymentRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {

        log.info("Payment request: amount={} {}, userId={}", 
                request.getAmount(), request.getCurrency(), userId);

        PaymentResult result = integrationFacade.processPayment(request);

        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success("Payment processed successfully", result));
        } else {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<PaymentResult>builder()
                            .success(false)
                            .status(400)
                            .message("Payment failed: " + result.getErrorMessage())
                            .data(result)
                            .build()
            );
        }
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("External Integration Service is running");
    }
}

