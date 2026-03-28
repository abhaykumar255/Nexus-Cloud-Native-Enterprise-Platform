package com.nexus.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void testEnumValues() {
        PaymentStatus[] statuses = PaymentStatus.values();
        assertTrue(statuses.length > 0);
    }

    @Test
    void testEnumValueOf() {
        PaymentStatus pending = PaymentStatus.valueOf("PENDING");
        assertNotNull(pending);
        assertEquals(PaymentStatus.PENDING, pending);
    }

    @Test
    void testAllExpectedValuesExist() {
        assertNotNull(PaymentStatus.PENDING);
        assertNotNull(PaymentStatus.PROCESSING);
        assertNotNull(PaymentStatus.COMPLETED);
        assertNotNull(PaymentStatus.FAILED);
        assertNotNull(PaymentStatus.REFUNDED);
    }
}

