package com.nexus.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for AuthenticationException
 */
class AuthenticationExceptionTest {

    @Test
    void constructor_withMessage_shouldCreateException() {
        String message = "Invalid credentials";
        
        AuthenticationException exception = new AuthenticationException(message);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("UNAUTHORIZED");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void constructor_withMessageAndCause_shouldCreateException() {
        String message = "Authentication failed";
        Throwable cause = new RuntimeException("Token expired");
        
        AuthenticationException exception = new AuthenticationException(message, cause);
        
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("UNAUTHORIZED");
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void exception_shouldExtendClientException() {
        AuthenticationException exception = new AuthenticationException("Test");
        
        assertThat(exception).isInstanceOf(ClientException.class);
        assertThat(exception).isInstanceOf(NexusException.class);
    }
}

