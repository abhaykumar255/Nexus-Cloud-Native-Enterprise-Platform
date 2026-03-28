package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when external API call fails (502)
 */
public class ExternalServiceException extends ServerException {
    
    public ExternalServiceException(String message) {
        super(message, "EXTERNAL_SERVICE_ERROR", HttpStatus.BAD_GATEWAY);
    }
    
    public ExternalServiceException(String message, Throwable cause) {
        super(message, "EXTERNAL_SERVICE_ERROR", HttpStatus.BAD_GATEWAY, cause);
    }
}

