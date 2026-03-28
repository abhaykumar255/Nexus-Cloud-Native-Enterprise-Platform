package com.nexus.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    
    private String id;
    private String userId;
    private String guestId;
    
    private List<CartItemDto> items;
    
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal total;
    
    private String couponCode;
    private BigDecimal couponDiscount;
    
    private Integer totalItemCount;
    
    private Instant createdAt;
    private Instant updatedAt;
}

