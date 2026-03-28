package com.nexus.product.event;

import com.nexus.common.enums.ProductStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEventTest {

    @Test
    void testProductEventBuilder() {
        // Given
        Instant now = Instant.now();

        // When
        ProductEvent event = ProductEvent.builder()
                .eventType(ProductEvent.EventType.PRODUCT_CREATED)
                .productId("prod-1")
                .sellerId("seller-1")
                .name("Test Product")
                .sku("SKU-001")
                .price(new BigDecimal("99.99"))
                .discountPrice(new BigDecimal("79.99"))
                .status(ProductStatus.PUBLISHED)
                .productType("PRODUCT")
                .timestamp(now)
                .build();

        // Then
        assertThat(event.getEventType()).isEqualTo(ProductEvent.EventType.PRODUCT_CREATED);
        assertThat(event.getProductId()).isEqualTo("prod-1");
        assertThat(event.getSellerId()).isEqualTo("seller-1");
        assertThat(event.getName()).isEqualTo("Test Product");
        assertThat(event.getSku()).isEqualTo("SKU-001");
        assertThat(event.getPrice()).isEqualByComparingTo(new BigDecimal("99.99"));
        assertThat(event.getDiscountPrice()).isEqualByComparingTo(new BigDecimal("79.99"));
        assertThat(event.getStatus()).isEqualTo(ProductStatus.PUBLISHED);
        assertThat(event.getProductType()).isEqualTo("PRODUCT");
        assertThat(event.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testProductEventSettersAndGetters() {
        // Given
        ProductEvent event = new ProductEvent();
        Instant now = Instant.now();

        // When
        event.setEventType(ProductEvent.EventType.PRODUCT_UPDATED);
        event.setProductId("prod-2");
        event.setSellerId("seller-2");
        event.setName("Updated Product");
        event.setPrice(new BigDecimal("149.99"));
        event.setStatus(ProductStatus.DRAFT);
        event.setTimestamp(now);

        // Then
        assertThat(event.getEventType()).isEqualTo(ProductEvent.EventType.PRODUCT_UPDATED);
        assertThat(event.getProductId()).isEqualTo("prod-2");
        assertThat(event.getSellerId()).isEqualTo("seller-2");
        assertThat(event.getName()).isEqualTo("Updated Product");
        assertThat(event.getPrice()).isEqualByComparingTo(new BigDecimal("149.99"));
        assertThat(event.getStatus()).isEqualTo(ProductStatus.DRAFT);
        assertThat(event.getTimestamp()).isEqualTo(now);
    }

    @Test
    void testAllEventTypesAreAccessible() {
        // Then
        assertThat(ProductEvent.EventType.values()).containsExactlyInAnyOrder(
                ProductEvent.EventType.PRODUCT_CREATED,
                ProductEvent.EventType.PRODUCT_UPDATED,
                ProductEvent.EventType.PRODUCT_DELETED,
                ProductEvent.EventType.PRODUCT_PUBLISHED,
                ProductEvent.EventType.PRODUCT_OUT_OF_STOCK,
                ProductEvent.EventType.PRODUCT_PRICE_CHANGED
        );
    }

    @Test
    void testProductDeletedEvent() {
        // When
        ProductEvent event = ProductEvent.builder()
                .eventType(ProductEvent.EventType.PRODUCT_DELETED)
                .productId("prod-delete")
                .sellerId("seller-1")
                .name("Deleted Product")
                .build();

        // Then
        assertThat(event.getEventType()).isEqualTo(ProductEvent.EventType.PRODUCT_DELETED);
        assertThat(event.getProductId()).isEqualTo("prod-delete");
    }

    @Test
    void testProductPublishedEvent() {
        // When
        ProductEvent event = ProductEvent.builder()
                .eventType(ProductEvent.EventType.PRODUCT_PUBLISHED)
                .productId("prod-publish")
                .status(ProductStatus.PUBLISHED)
                .build();

        // Then
        assertThat(event.getEventType()).isEqualTo(ProductEvent.EventType.PRODUCT_PUBLISHED);
        assertThat(event.getStatus()).isEqualTo(ProductStatus.PUBLISHED);
    }

    @Test
    void testProductOutOfStockEvent() {
        // When
        ProductEvent event = ProductEvent.builder()
                .eventType(ProductEvent.EventType.PRODUCT_OUT_OF_STOCK)
                .productId("prod-oos")
                .build();

        // Then
        assertThat(event.getEventType()).isEqualTo(ProductEvent.EventType.PRODUCT_OUT_OF_STOCK);
    }

    @Test
    void testProductPriceChangedEvent() {
        // When
        ProductEvent event = ProductEvent.builder()
                .eventType(ProductEvent.EventType.PRODUCT_PRICE_CHANGED)
                .productId("prod-price")
                .price(new BigDecimal("199.99"))
                .discountPrice(new BigDecimal("159.99"))
                .build();

        // Then
        assertThat(event.getEventType()).isEqualTo(ProductEvent.EventType.PRODUCT_PRICE_CHANGED);
        assertThat(event.getPrice()).isEqualByComparingTo(new BigDecimal("199.99"));
        assertThat(event.getDiscountPrice()).isEqualByComparingTo(new BigDecimal("159.99"));
    }
}

