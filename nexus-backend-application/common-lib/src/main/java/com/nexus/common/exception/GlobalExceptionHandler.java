package com.nexus.common.exception;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for all REST controllers
 * Catches all exceptions and maps them to standardized ApiResponse format
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle all NEXUS custom exceptions
     */
    @ExceptionHandler(NexusException.class)
    public ResponseEntity<ApiResponse<Void>> handleNexusException(NexusException ex, WebRequest request) {
        log.error("NEXUS Exception: {} - {}", ex.getErrorCode(), ex.getMessage(), ex);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .status(ex.getHttpStatus().value())
                .error(errorDetails)
                .timestamp(Instant.now())
                .build();
        
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }
    
    /**
     * Handle Spring validation errors (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<ErrorDetails.ValidationError> validationErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
                    Object rejectedValue = error instanceof FieldError ? ((FieldError) error).getRejectedValue() : null;
                    return ErrorDetails.ValidationError.builder()
                            .field(fieldName)
                            .rejected(rejectedValue)
                            .message(error.getDefaultMessage())
                            .build();
                })
                .collect(Collectors.toList());
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("VALIDATION_FAILED")
                .message("Request validation failed")
                .details(validationErrors)
                .build();
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .error(errorDetails)
                .timestamp(Instant.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected exception occurred", ex);
        
        ErrorDetails errorDetails = ErrorDetails.builder()
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .build();
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(errorDetails)
                .timestamp(Instant.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

