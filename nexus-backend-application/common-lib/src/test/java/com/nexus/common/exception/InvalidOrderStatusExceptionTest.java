package com.nexus.common.exception;

import com.nexus.common.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidOrderStatusExceptionTest {

    @Test
    void testConstructorWithOrderStatuses() {
        OrderStatus current = OrderStatus.PENDING_PAYMENT;
        OrderStatus attempted = OrderStatus.DELIVERED;

        InvalidOrderStatusException exception = new InvalidOrderStatusException(current, attempted);

        assertEquals(current, exception.getCurrentStatus());
        assertEquals(attempted, exception.getAttemptedStatus());
        assertTrue(exception.getMessage().contains("PENDING_PAYMENT"));
        assertTrue(exception.getMessage().contains("DELIVERED"));
    }

    @Test
    void testExceptionMessage() {
        InvalidOrderStatusException exception = new InvalidOrderStatusException(
            OrderStatus.CANCELLED, OrderStatus.CONFIRMED
        );
        assertTrue(exception.getMessage().contains("Cannot transition"));
    }

    @Test
    void testExceptionIsInstanceOfBusinessRuleException() {
        InvalidOrderStatusException exception = new InvalidOrderStatusException(
            OrderStatus.DELIVERED, OrderStatus.PENDING_PAYMENT
        );
        assertTrue(exception instanceof BusinessRuleException);
    }

    @Test
    void testExceptionCanBeThrown() {
        assertThrows(InvalidOrderStatusException.class, () -> {
            throw new InvalidOrderStatusException(OrderStatus.CANCELLED, OrderStatus.PENDING_PAYMENT);
        });
    }
}

