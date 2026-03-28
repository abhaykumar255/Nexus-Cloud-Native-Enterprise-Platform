package com.nexus.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void testEnumValues() {
        OrderStatus[] statuses = OrderStatus.values();
        assertTrue(statuses.length > 0);
    }

    @Test
    void testEnumValueOf() {
        OrderStatus pending = OrderStatus.valueOf("PENDING_PAYMENT");
        assertNotNull(pending);
        assertEquals(OrderStatus.PENDING_PAYMENT, pending);
    }

    @Test
    void testAllExpectedValuesExist() {
        assertNotNull(OrderStatus.PENDING_PAYMENT);
        assertNotNull(OrderStatus.PAYMENT_CONFIRMED);
        assertNotNull(OrderStatus.CONFIRMED);
        assertNotNull(OrderStatus.PACKED);
        assertNotNull(OrderStatus.DISPATCHED);
        assertNotNull(OrderStatus.OUT_FOR_DELIVERY);
        assertNotNull(OrderStatus.DELIVERED);
        assertNotNull(OrderStatus.CANCELLED);
        assertNotNull(OrderStatus.FAILED);
    }
}

