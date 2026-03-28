package com.nexus.search.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SellerDocumentTest {

    @Test
    void testSellerDocumentBuilder() {
        Instant now = Instant.now();

        SellerDocument seller = SellerDocument.builder()
                .id("seller-1")
                .businessName("Tech Electronics")
                .businessType("RETAIL")
                .businessEmail("contact@techelectronics.com")
                .businessPhone("+1234567890")
                .businessAddress("456 Commerce Ave")
                .status("ACTIVE")
                .verificationStatus("VERIFIED")
                .kycVerified(true)
                .rating(new BigDecimal("4.8"))
                .totalReviews(1000)
                .totalProducts(500)
                .totalOrders(5000)
                .totalRevenue(new BigDecimal("250000.00"))
                .description("Leading electronics retailer")
                .createdAt(now)
                .build();

        assertEquals("seller-1", seller.getId());
        assertEquals("Tech Electronics", seller.getBusinessName());
        assertEquals("RETAIL", seller.getBusinessType());
        assertEquals("contact@techelectronics.com", seller.getBusinessEmail());
        assertEquals("+1234567890", seller.getBusinessPhone());
        assertEquals("456 Commerce Ave", seller.getBusinessAddress());
        assertEquals("ACTIVE", seller.getStatus());
        assertEquals("VERIFIED", seller.getVerificationStatus());
        assertTrue(seller.getKycVerified());
        assertEquals(new BigDecimal("4.8"), seller.getRating());
        assertEquals(1000, seller.getTotalReviews());
        assertEquals(500, seller.getTotalProducts());
        assertEquals(5000, seller.getTotalOrders());
        assertEquals(new BigDecimal("250000.00"), seller.getTotalRevenue());
        assertEquals("Leading electronics retailer", seller.getDescription());
        assertEquals(now, seller.getCreatedAt());
    }

    @Test
    void testSellerDocumentNoArgsConstructor() {
        SellerDocument seller = new SellerDocument();
        assertNotNull(seller);
    }

    @Test
    void testSellerDocumentAllArgsConstructor() {
        Instant now = Instant.now();

        SellerDocument seller = new SellerDocument(
                "id", "Business Name", "Type", "email@test.com", "phone",
                "address", "ACTIVE", "VERIFIED", true, BigDecimal.valueOf(4.5),
                100, 50, 200, new BigDecimal("10000"), "description", now
        );

        assertEquals("id", seller.getId());
        assertEquals("Business Name", seller.getBusinessName());
        assertTrue(seller.getKycVerified());
    }

    @Test
    void testSellerDocumentSettersAndGetters() {
        SellerDocument seller = new SellerDocument();
        seller.setId("seller-2");
        seller.setBusinessName("Food Market");
        seller.setKycVerified(false);
        seller.setTotalProducts(150);

        assertEquals("seller-2", seller.getId());
        assertEquals("Food Market", seller.getBusinessName());
        assertFalse(seller.getKycVerified());
        assertEquals(150, seller.getTotalProducts());
    }

    @Test
    void testSellerDocumentEqualsAndHashCode() {
        SellerDocument seller1 = SellerDocument.builder()
                .id("seller-1")
                .businessName("Test Business")
                .build();

        SellerDocument seller2 = SellerDocument.builder()
                .id("seller-1")
                .businessName("Test Business")
                .build();

        assertEquals(seller1, seller2);
        assertEquals(seller1.hashCode(), seller2.hashCode());
    }

    @Test
    void testSellerDocumentToString() {
        SellerDocument seller = SellerDocument.builder()
                .id("seller-1")
                .businessName("Test")
                .build();

        String toString = seller.toString();
        assertTrue(toString.contains("seller-1"));
        assertTrue(toString.contains("Test"));
    }

    @Test
    void testSellerDocumentWithMetrics() {
        SellerDocument seller = SellerDocument.builder()
                .totalOrders(1000)
                .totalRevenue(new BigDecimal("50000.00"))
                .rating(new BigDecimal("4.9"))
                .totalReviews(250)
                .build();

        assertEquals(1000, seller.getTotalOrders());
        assertEquals(new BigDecimal("50000.00"), seller.getTotalRevenue());
        assertEquals(new BigDecimal("4.9"), seller.getRating());
        assertEquals(250, seller.getTotalReviews());
    }
}

