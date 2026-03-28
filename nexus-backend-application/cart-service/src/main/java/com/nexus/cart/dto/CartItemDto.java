package com.nexus.cart.dto;

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
public class CartItemDto {
    
    private String productId;
    private String productName;
    private String productImage;
    private String sellerId;
    private String sellerName;
    
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    
    private String variantId;
    private String variantName;
    
    private Boolean inStock;
    private Integer availableStock;
    private Boolean available;
    
    private Instant addedAt;
}

