package com.nexus.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void testEnumValues() {
        UserRole[] roles = UserRole.values();
        assertTrue(roles.length > 0);
    }

    @Test
    void testEnumValueOf() {
        UserRole admin = UserRole.valueOf("ADMIN");
        assertNotNull(admin);
        assertEquals(UserRole.ADMIN, admin);
    }

    @Test
    void testAllExpectedValuesExist() {
        assertNotNull(UserRole.CUSTOMER);
        assertNotNull(UserRole.SELLER);
        assertNotNull(UserRole.DELIVERY_PARTNER);
        assertNotNull(UserRole.RESTAURANT_OWNER);
        assertNotNull(UserRole.ADMIN);
        assertNotNull(UserRole.SUPER_ADMIN);
    }
}

