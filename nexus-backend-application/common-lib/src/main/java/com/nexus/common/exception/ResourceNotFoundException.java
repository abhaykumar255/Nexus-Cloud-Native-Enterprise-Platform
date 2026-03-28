package com.nexus.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when requested resource is not found (404)
 */
public class ResourceNotFoundException extends ClientException {
    
    public ResourceNotFoundException(String resource, String identifier) {
        super(String.format("%s with identifier '%s' not found", resource, identifier), 
              "RESOURCE_NOT_FOUND", 
              HttpStatus.NOT_FOUND);
    }
    
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}

