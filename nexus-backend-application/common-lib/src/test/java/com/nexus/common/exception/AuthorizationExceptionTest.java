package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Unauthorized access";
        AuthorizationException exception = new AuthorizationException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionMessageContainsInfo() {
        String message = "Access denied";
        AuthorizationException exception = new AuthorizationException(message);

        assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void testExceptionIsInstanceOfClientException() {
        AuthorizationException exception = new AuthorizationException("Test");
        assertTrue(exception instanceof ClientException);
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(AuthorizationException.class, () -> {
            throw new AuthorizationException("Forbidden");
        });
    }
}

