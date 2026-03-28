package com.nexus.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void testEnumValues() {
        PaymentMethod[] methods = PaymentMethod.values();
        assertTrue(methods.length > 0);
    }

    @Test
    void testEnumValueOf() {
        PaymentMethod card = PaymentMethod.valueOf("CREDIT_CARD");
        assertNotNull(card);
        assertEquals(PaymentMethod.CREDIT_CARD, card);
    }

    @Test
    void testAllExpectedValuesExist() {
        assertNotNull(PaymentMethod.CREDIT_CARD);
        assertNotNull(PaymentMethod.DEBIT_CARD);
        assertNotNull(PaymentMethod.NET_BANKING);
        assertNotNull(PaymentMethod.UPI);
        assertNotNull(PaymentMethod.WALLET);
        assertNotNull(PaymentMethod.COD);
        assertNotNull(PaymentMethod.EMI);
    }
}

