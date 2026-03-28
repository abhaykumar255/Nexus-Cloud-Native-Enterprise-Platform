package com.nexus.order.domain.entity;

import com.nexus.common.enums.OrderStatus;
import com.nexus.common.enums.OrderType;
import com.nexus.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type; // STANDARD, FOOD, GROCERY
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    
    // Pricing
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;
    
    @Column(name = "delivery_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal deliveryFee = BigDecimal.ZERO;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    // Payment
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    
    @Column(name = "payment_id")
    private String paymentId;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    
    // Delivery Address
    @Column(name = "delivery_address", nullable = false, length = 500)
    private String deliveryAddress;
    
    @Column(name = "delivery_city", nullable = false)
    private String deliveryCity;
    
    @Column(name = "delivery_state", nullable = false)
    private String deliveryState;
    
    @Column(name = "delivery_postal_code", nullable = false)
    private String deliveryPostalCode;
    
    @Column(name = "delivery_country", nullable = false)
    private String deliveryCountry;
    
    @Column(name = "delivery_latitude")
    private Double deliveryLatitude;
    
    @Column(name = "delivery_longitude")
    private Double deliveryLongitude;
    
    @Column(name = "delivery_phone", nullable = false)
    private String deliveryPhone;
    
    @Column(name = "delivery_instructions", length = 500)
    private String deliveryInstructions;
    
    // Tracking
    @Column(name = "delivery_id")
    private String deliveryId;
    
    @Column(name = "tracking_number")
    private String trackingNumber;
    
    @Column(name = "estimated_delivery_time")
    private Instant estimatedDeliveryTime;
    
    @Column(name = "actual_delivery_time")
    private Instant actualDeliveryTime;
    
    // Coupon
    @Column(name = "coupon_code")
    private String couponCode;
    
    @Column(name = "coupon_discount", precision = 10, scale = 2)
    private BigDecimal couponDiscount;
    
    // Notes
    @Column(length = 1000)
    private String notes;
    
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    // Timestamps
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    @Column(name = "confirmed_at")
    private Instant confirmedAt;
    
    @Column(name = "dispatched_at")
    private Instant dispatchedAt;
}

