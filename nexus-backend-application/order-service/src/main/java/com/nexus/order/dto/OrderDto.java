package com.nexus.order.dto;

import com.nexus.common.enums.OrderStatus;
import com.nexus.common.enums.OrderType;
import com.nexus.common.enums.PaymentMethod;
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
public class OrderDto {
    
    private String id;
    private String orderNumber;
    private String userId;
    private OrderType type;
    private OrderStatus status;
    
    private List<OrderItemDto> items;
    
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;
    
    private PaymentMethod paymentMethod;
    private String paymentId;
    private String paymentStatus;
    
    private DeliveryAddressDto deliveryAddress;
    
    private String deliveryId;
    private String trackingNumber;
    private Instant estimatedDeliveryTime;
    private Instant actualDeliveryTime;
    
    private String couponCode;
    private BigDecimal couponDiscount;
    
    private String notes;
    private String cancellationReason;
    
    private Instant createdAt;
    private Instant updatedAt;
    private Instant confirmedAt;
    private Instant dispatchedAt;
}

