package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientStockExceptionTest {

    @Test
    void testConstructorWithAllParams() {
        String productId = "PROD-123";
        int requestedQuantity = 10;
        int availableStock = 5;

        InsufficientStockException exception = new InsufficientStockException(productId, requestedQuantity, availableStock);

        assertEquals(productId, exception.getProductId());
        assertEquals(requestedQuantity, exception.getRequestedQuantity());
        assertEquals(availableStock, exception.getAvailableStock());
        assertTrue(exception.getMessage().contains(productId));
        assertTrue(exception.getMessage().contains("10"));
        assertTrue(exception.getMessage().contains("5"));
    }

    @Test
    void testExceptionMessage() {
        InsufficientStockException exception = new InsufficientStockException("PROD-456", 20, 10);
        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    @Test
    void testExceptionIsInstanceOfBusinessRuleException() {
        InsufficientStockException exception = new InsufficientStockException("PROD-789", 5, 0);
        assertTrue(exception instanceof BusinessRuleException);
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(InsufficientStockException.class, () -> {
            throw new InsufficientStockException("PROD-999", 100, 50);
        });
    }
}

