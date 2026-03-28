package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessRuleExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Business rule violated";
        BusinessRuleException exception = new BusinessRuleException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionMessageContainsInfo() {
        String message = "Invalid business operation";
        BusinessRuleException exception = new BusinessRuleException(message);

        assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void testExceptionIsInstanceOfClientException() {
        BusinessRuleException exception = new BusinessRuleException("Test");
        assertTrue(exception instanceof ClientException);
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(BusinessRuleException.class, () -> {
            throw new BusinessRuleException("Discount exceeds maximum allowed");
        });
    }
}

