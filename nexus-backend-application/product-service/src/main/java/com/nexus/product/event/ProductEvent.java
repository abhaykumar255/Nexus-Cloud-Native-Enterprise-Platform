package com.nexus.product.event;

import com.nexus.common.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
    
    private EventType eventType;
    private String productId;
    private String sellerId;
    private String name;
    private String sku;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private ProductStatus status;
    private String productType;
    private Instant timestamp;
    
    public enum EventType {
        PRODUCT_CREATED,
        PRODUCT_UPDATED,
        PRODUCT_DELETED,
        PRODUCT_PUBLISHED,
        PRODUCT_OUT_OF_STOCK,
        PRODUCT_PRICE_CHANGED
    }
}

