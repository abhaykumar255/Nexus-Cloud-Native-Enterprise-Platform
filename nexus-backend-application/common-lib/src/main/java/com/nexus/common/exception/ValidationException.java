package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Exception thrown when request validation fails (400)
 */
public class ValidationException extends ClientException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_FAILED", HttpStatus.BAD_REQUEST);
    }
    
    public ValidationException(String message, Map<String, Object> details) {
        super(message, "VALIDATION_FAILED", HttpStatus.BAD_REQUEST, details);
    }
}

