package com.nexus.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryStatusTest {

    @Test
    void testEnumValues() {
        DeliveryStatus[] statuses = DeliveryStatus.values();
        assertTrue(statuses.length > 0);
    }

    @Test
    void testEnumValueOf() {
        DeliveryStatus assigned = DeliveryStatus.valueOf("ASSIGNED");
        assertNotNull(assigned);
        assertEquals(DeliveryStatus.ASSIGNED, assigned);
    }

    @Test
    void testAllExpectedValuesExist() {
        assertNotNull(DeliveryStatus.PENDING_ASSIGNMENT);
        assertNotNull(DeliveryStatus.ASSIGNED);
        assertNotNull(DeliveryStatus.PARTNER_ACCEPTED);
        assertNotNull(DeliveryStatus.PICKED_UP);
        assertNotNull(DeliveryStatus.IN_TRANSIT);
        assertNotNull(DeliveryStatus.NEAR_DESTINATION);
        assertNotNull(DeliveryStatus.DELIVERED);
        assertNotNull(DeliveryStatus.FAILED);
        assertNotNull(DeliveryStatus.CANCELLED);
    }
}

