package com.nexus.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Base exception for all NEXUS application exceptions
 * All custom exceptions should extend this class
 */
@Getter
public abstract class NexusException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;
    
    protected NexusException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }
    
    protected NexusException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = null;
    }
    
    protected NexusException(String message, String errorCode, HttpStatus httpStatus, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }
}

