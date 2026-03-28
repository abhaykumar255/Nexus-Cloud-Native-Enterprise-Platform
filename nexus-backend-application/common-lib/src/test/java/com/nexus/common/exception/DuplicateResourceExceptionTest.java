package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateResourceExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Resource already exists";
        DuplicateResourceException exception = new DuplicateResourceException(message);

        assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void testConstructorWithResourceFieldValue() {
        String resource = "User";
        String field = "email";
        String value = "test@example.com";

        DuplicateResourceException exception = new DuplicateResourceException(resource, field, value);

        assertTrue(exception.getMessage().contains(resource));
        assertTrue(exception.getMessage().contains(field));
        assertTrue(exception.getMessage().contains(value));
    }

    @Test
    void testExceptionIsInstanceOfClientException() {
        DuplicateResourceException exception = new DuplicateResourceException("Test");
        assertTrue(exception instanceof ClientException);
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(DuplicateResourceException.class, () -> {
            throw new DuplicateResourceException("Email already registered");
        });
    }
}

