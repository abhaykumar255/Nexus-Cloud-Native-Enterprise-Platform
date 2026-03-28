package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to create duplicate resource (409)
 */
public class DuplicateResourceException extends ClientException {
    
    public DuplicateResourceException(String resource, String field, String value) {
        super(String.format("%s with %s '%s' already exists", resource, field, value), 
              "DUPLICATE_RESOURCE", 
              HttpStatus.CONFLICT);
    }
    
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE", HttpStatus.CONFLICT);
    }
}

