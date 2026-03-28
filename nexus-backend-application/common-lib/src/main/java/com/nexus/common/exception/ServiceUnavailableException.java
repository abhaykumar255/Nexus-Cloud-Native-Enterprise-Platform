package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when service is unavailable (503)
 */
public class ServiceUnavailableException extends ServerException {
    
    public ServiceUnavailableException(String service) {
        super(String.format("Service '%s' is temporarily unavailable", service), 
              "SERVICE_UNAVAILABLE", 
              HttpStatus.SERVICE_UNAVAILABLE);
    }
    
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, "SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}

