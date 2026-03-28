package com.nexus.gateway.controller;

import com.nexus.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fallback controller for circuit breaker
 * Returns appropriate responses when services are unavailable
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {
    
    @GetMapping("/user-service")
    public ResponseEntity<ApiResponse<Void>> userServiceFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "User Service is temporarily unavailable. Please try again later."
                ));
    }
    
    @GetMapping("/task-service")
    public ResponseEntity<ApiResponse<Void>> taskServiceFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "Task Service is temporarily unavailable. Please try again later."
                ));
    }
    
    @GetMapping("/notification-service")
    public ResponseEntity<ApiResponse<Void>> notificationServiceFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "Notification Service is temporarily unavailable. Please try again later."
                ));
    }
}

