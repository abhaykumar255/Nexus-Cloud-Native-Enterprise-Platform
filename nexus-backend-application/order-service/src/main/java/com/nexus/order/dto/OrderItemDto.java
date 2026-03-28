package com.nexus.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    
    private String id;
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
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
}

