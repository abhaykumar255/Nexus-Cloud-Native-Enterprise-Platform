package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Base class for 5xx server-side errors
 */
public abstract class ServerException extends NexusException {
    
    protected ServerException(String message, String errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
    
    protected ServerException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, errorCode, httpStatus, cause);
    }
    
    protected ServerException(String message, String errorCode, HttpStatus httpStatus, Map<String, Object> details) {
        super(message, errorCode, httpStatus, details);
    }
}

