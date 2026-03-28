package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExternalServiceExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "External service unavailable";
        ExternalServiceException exception = new ExternalServiceException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Payment gateway error";
        Throwable cause = new RuntimeException("Timeout");
        ExternalServiceException exception = new ExternalServiceException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionIsInstanceOfNexusException() {
        ExternalServiceException exception = new ExternalServiceException("Test");
        assertTrue(exception instanceof NexusException);
    }
}

