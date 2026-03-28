package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails (401)
 */
public class AuthenticationException extends ClientException {
    
    public AuthenticationException(String message) {
        super(message, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED, cause);
    }
}

