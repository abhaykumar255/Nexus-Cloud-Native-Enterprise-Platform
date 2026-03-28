package com.nexus.delivery.domain.entity;

import com.nexus.common.enums.DeliveryStatus;
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

@Entity
@Table(name = "deliveries", indexes = {
        @Index(name = "idx_order_id", columnList = "order_id"),
        @Index(name = "idx_partner_id", columnList = "delivery_partner_id"),
        @Index(name = "idx_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;
    
    @Column(name = "delivery_partner_id")
    private String deliveryPartnerId;
    
    @Column(name = "delivery_partner_name")
    private String deliveryPartnerName;
    
    @Column(name = "delivery_partner_phone")
    private String deliveryPartnerPhone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.PENDING_ASSIGNMENT;
    
    @Column(name = "pickup_address", nullable = false, length = 500)
    private String pickupAddress;
    
    @Column(name = "pickup_latitude")
    private Double pickupLatitude;
    
    @Column(name = "pickup_longitude")
    private Double pickupLongitude;
    
    @Column(name = "delivery_address", nullable = false, length = 500)
    private String deliveryAddress;
    
    @Column(name = "delivery_latitude")
    private Double deliveryLatitude;
    
    @Column(name = "delivery_longitude")
    private Double deliveryLongitude;
    
    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm;
    
    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;
    
    @Column(name = "estimated_pickup_time")
    private Instant estimatedPickupTime;
    
    @Column(name = "estimated_delivery_time")
    private Instant estimatedDeliveryTime;
    
    @Column(name = "actual_pickup_time")
    private Instant actualPickupTime;
    
    @Column(name = "actual_delivery_time")
    private Instant actualDeliveryTime;
    
    @Column(length = 1000)
    private String notes;
    
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}

