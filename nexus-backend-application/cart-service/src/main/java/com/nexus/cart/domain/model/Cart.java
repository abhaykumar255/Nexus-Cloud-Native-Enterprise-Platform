package com.nexus.cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping Cart stored in Redis
 * TTL: 7 days (604800 seconds)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("cart")
public class Cart implements Serializable {
    
    @Id
    private String id; // userId or guestId
    
    private String userId;
    private String guestId;
    
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
    
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal total;
    
    private String couponCode;
    private BigDecimal couponDiscount;
    
    private Instant createdAt;
    private Instant updatedAt;
    
    @TimeToLive
    private Long ttl; // 604800 seconds = 7 days
    
    /**
     * Calculate cart totals
     */
    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.discount = couponDiscount != null ? couponDiscount : BigDecimal.ZERO;
        
        // Tax calculation (example: 10%)
        BigDecimal taxableAmount = subtotal.subtract(discount);
        this.tax = taxableAmount.multiply(new BigDecimal("0.10"));
        
        this.total = subtotal.subtract(discount).add(tax);
        this.updatedAt = Instant.now();
    }
    
    /**
     * Get total item count
     */
    public int getTotalItemCount() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    /**
     * Check if cart is empty
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}

