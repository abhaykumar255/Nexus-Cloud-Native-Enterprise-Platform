package com.nexus.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ValidationException
 */
class ValidationExceptionTest {

    @Test
    void constructor_withMessage_shouldCreateException() {
        String message = "Validation failed";
        
        ValidationException exception = new ValidationException(message);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_FAILED");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getDetails()).isNull();
    }

    @Test
    void constructor_withMessageAndDetails_shouldCreateException() {
        String message = "Validation failed";
        Map<String, Object> details = new HashMap<>();
        details.put("field", "email");
        details.put("error", "Invalid format");
        
        ValidationException exception = new ValidationException(message, details);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_FAILED");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception.getDetails()).isEqualTo(details);
        assertThat(exception.getDetails().get("field")).isEqualTo("email");
    }

    @Test
    void exception_shouldExtendClientException() {
        ValidationException exception = new ValidationException("Test");
        
        assertThat(exception).isInstanceOf(ClientException.class);
        assertThat(exception).isInstanceOf(NexusException.class);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

