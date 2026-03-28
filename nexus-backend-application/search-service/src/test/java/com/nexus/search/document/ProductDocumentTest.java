package com.nexus.search.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDocumentTest {

    @Test
    void testProductDocumentBuilder() {
        List<String> tags = Arrays.asList("electronics", "smartphone");
        List<String> images = Arrays.asList("img1.jpg", "img2.jpg");
        Instant now = Instant.now();

        ProductDocument product = ProductDocument.builder()
                .id("prod-1")
                .name("iPhone 15")
                .description("Latest iPhone model")
                .shortDescription("Apple iPhone 15")
                .brand("Apple")
                .category("Electronics")
                .subCategory("Smartphones")
                .sellerId("seller-1")
                .sellerName("Tech Store")
                .price(new BigDecimal("999.99"))
                .discountedPrice(new BigDecimal("899.99"))
                .discountPercentage(10)
                .stockQuantity(100)
                .inStock(true)
                .sku("IPHONE15-BLK-128")
                .tags(tags)
                .images(images)
                .rating(new BigDecimal("4.5"))
                .reviewCount(250)
                .status("AVAILABLE")
                .createdAt(now)
                .build();

        assertEquals("prod-1", product.getId());
        assertEquals("iPhone 15", product.getName());
        assertEquals("Latest iPhone model", product.getDescription());
        assertEquals("Apple", product.getBrand());
        assertEquals("Electronics", product.getCategory());
        assertEquals("seller-1", product.getSellerId());
        assertEquals(new BigDecimal("999.99"), product.getPrice());
        assertEquals(new BigDecimal("899.99"), product.getDiscountedPrice());
        assertEquals(10, product.getDiscountPercentage());
        assertEquals(100, product.getStockQuantity());
        assertTrue(product.getInStock());
        assertEquals(tags, product.getTags());
        assertEquals(images, product.getImages());
        assertEquals(new BigDecimal("4.5"), product.getRating());
        assertEquals(250, product.getReviewCount());
        assertEquals("AVAILABLE", product.getStatus());
        assertEquals(now, product.getCreatedAt());
    }

    @Test
    void testProductDocumentNoArgsConstructor() {
        ProductDocument product = new ProductDocument();
        assertNotNull(product);
    }

    @Test
    void testProductDocumentAllArgsConstructor() {
        List<String> tags = Arrays.asList("test");
        List<String> images = Arrays.asList("test.jpg");
        Instant now = Instant.now();

        ProductDocument product = new ProductDocument(
                "id", "name", "desc", "shortDesc", "brand", "category", "subCategory",
                "sellerId", "sellerName", BigDecimal.TEN, BigDecimal.ONE, 10, 
                50, true, "SKU123", tags, images, BigDecimal.valueOf(4.5), 100, 
                "ACTIVE", now
        );

        assertEquals("id", product.getId());
        assertEquals("name", product.getName());
        assertEquals("brand", product.getBrand());
    }

    @Test
    void testProductDocumentSettersAndGetters() {
        ProductDocument product = new ProductDocument();
        product.setId("prod-2");
        product.setName("Samsung Galaxy");
        product.setPrice(new BigDecimal("799.99"));
        product.setInStock(false);

        assertEquals("prod-2", product.getId());
        assertEquals("Samsung Galaxy", product.getName());
        assertEquals(new BigDecimal("799.99"), product.getPrice());
        assertFalse(product.getInStock());
    }

    @Test
    void testProductDocumentEqualsAndHashCode() {
        ProductDocument product1 = ProductDocument.builder()
                .id("prod-1")
                .name("Test Product")
                .build();

        ProductDocument product2 = ProductDocument.builder()
                .id("prod-1")
                .name("Test Product")
                .build();

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testProductDocumentToString() {
        ProductDocument product = ProductDocument.builder()
                .id("prod-1")
                .name("Test")
                .build();

        String toString = product.toString();
        assertTrue(toString.contains("prod-1"));
        assertTrue(toString.contains("Test"));
    }
}

