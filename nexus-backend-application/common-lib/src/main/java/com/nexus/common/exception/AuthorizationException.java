package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user lacks required permissions (403)
 */
public class AuthorizationException extends ClientException {
    
    public AuthorizationException(String message) {
        super(message, "ACCESS_DENIED", HttpStatus.FORBIDDEN);
    }
}

