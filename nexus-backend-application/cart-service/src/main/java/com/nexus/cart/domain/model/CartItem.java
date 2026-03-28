package com.nexus.cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Individual item in the shopping cart
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    
    private String productId;
    private String productName;
    private String productImage;
    private String sellerId;
    private String sellerName;
    
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    
    private String variantId; // For product variants (size, color, etc.)
    private String variantName;
    
    private Boolean inStock;
    private Integer availableStock;
    
    private Instant addedAt;
    
    /**
     * Calculate total price for this item
     */
    public void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
    }
    
    /**
     * Check if item is available
     */
    public boolean isAvailable() {
        return inStock != null && inStock && availableStock != null && availableStock >= quantity;
    }
}

