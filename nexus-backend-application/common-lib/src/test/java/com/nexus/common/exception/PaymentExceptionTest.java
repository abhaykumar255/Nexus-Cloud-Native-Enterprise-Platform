package com.nexus.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Payment processing failed";
        PaymentException exception = new PaymentException(message);

        assertTrue(exception.getMessage().contains(message));
        assertNull(exception.getTransactionId());
        assertNull(exception.getGatewayErrorCode());
    }

    @Test
    void testConstructorWithAllParams() {
        String message = "Card declined";
        String transactionId = "TXN-123";
        String gatewayCode = "ERR-500";

        PaymentException exception = new PaymentException(message, transactionId, gatewayCode);

        assertTrue(exception.getMessage().contains(message));
        assertEquals(transactionId, exception.getTransactionId());
        assertEquals(gatewayCode, exception.getGatewayErrorCode());
    }

    @Test
    void testExceptionIsInstanceOfNexusException() {
        PaymentException exception = new PaymentException("Test");
        assertTrue(exception instanceof NexusException);
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(PaymentException.class, () -> {
            throw new PaymentException("Payment gateway timeout");
        });
    }
}

