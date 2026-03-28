package com.nexus.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Standard API response envelope for all REST endpoints
 * Provides consistent structure with success/error status, trace ID, and optional data/pagination
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private boolean success;
    private int status;
    private String message;
    
    @Builder.Default
    private Instant timestamp = Instant.now();
    
    private String requestId;
    private String traceId;
    private T data;
    private ErrorDetails error;
    private PaginationInfo pagination;
    
    /**
     * Create a successful response with data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }
    
    /**
     * Create a successful response with custom message and data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * Create a successful response with status, message, and data
     */
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * Create an error response
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(500)
                .message(message)
                .build();
    }
    
    /**
     * Create an error response with status
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .message(message)
                .build();
    }
    
    /**
     * Create an error response with error details
     */
    public static <T> ApiResponse<T> error(int status, ErrorDetails error) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .error(error)
                .build();
    }
    
    /**
     * Create a paginated response
     */
    public static <T> ApiResponse<T> paginated(T data, PaginationInfo pagination) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message("Success")
                .data(data)
                .pagination(pagination)
                .build();
    }
}

