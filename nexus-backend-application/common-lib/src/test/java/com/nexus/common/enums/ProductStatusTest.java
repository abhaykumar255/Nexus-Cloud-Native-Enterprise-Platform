package com.nexus.common.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductStatusTest {

    @Test
    void testEnumValues() {
        ProductStatus[] statuses = ProductStatus.values();
        assertTrue(statuses.length > 0);
    }

    @Test
    void testEnumValueOf() {
        ProductStatus published = ProductStatus.valueOf("PUBLISHED");
        assertNotNull(published);
        assertEquals(ProductStatus.PUBLISHED, published);
    }

    @Test
    void testAllExpectedValuesExist() {
        assertNotNull(ProductStatus.DRAFT);
        assertNotNull(ProductStatus.PENDING_REVIEW);
        assertNotNull(ProductStatus.PUBLISHED);
        assertNotNull(ProductStatus.ARCHIVED);
        assertNotNull(ProductStatus.REJECTED);
        assertNotNull(ProductStatus.OUT_OF_STOCK);
    }
}

