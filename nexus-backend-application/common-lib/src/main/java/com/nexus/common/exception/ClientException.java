package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Base class for 4xx client-side errors
 */
public abstract class ClientException extends NexusException {
    
    protected ClientException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
    
    protected ClientException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, errorCode, httpStatus, cause);
    }
    
    protected ClientException(String message, String errorCode, HttpStatus httpStatus, Map<String, Object> details) {
        super(message, errorCode, httpStatus, details);
    }
}

