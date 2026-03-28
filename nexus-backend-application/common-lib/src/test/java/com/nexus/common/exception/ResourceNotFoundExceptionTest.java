package com.nexus.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ResourceNotFoundException
 */
class ResourceNotFoundExceptionTest {

    @Test
    void constructor_withResourceAndIdentifier_shouldCreateException() {
        String resource = "User";
        String identifier = "user-123";
        
        ResourceNotFoundException exception = new ResourceNotFoundException(resource, identifier);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("User with identifier 'user-123' not found");
        assertThat(exception.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void constructor_withMessage_shouldCreateException() {
        String message = "Custom not found message";
        
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void exception_shouldExtendClientException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test", "123");
        
        assertThat(exception).isInstanceOf(ClientException.class);
        assertThat(exception).isInstanceOf(NexusException.class);
    }
}

