package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUnavailableExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String serviceName = "PaymentService";
        ServiceUnavailableException exception = new ServiceUnavailableException(serviceName);

        assertTrue(exception.getMessage().contains(serviceName));
        assertTrue(exception.getMessage().contains("temporarily unavailable"));
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Service under maintenance";
        Throwable cause = new RuntimeException("Circuit breaker open");
        ServiceUnavailableException exception = new ServiceUnavailableException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(ServiceUnavailableException.class, () -> {
            throw new ServiceUnavailableException("Service down for maintenance");
        });
    }
}

