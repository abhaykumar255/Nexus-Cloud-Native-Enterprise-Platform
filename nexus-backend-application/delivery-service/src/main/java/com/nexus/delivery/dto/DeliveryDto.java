package com.nexus.delivery.dto;

import com.nexus.common.enums.DeliveryStatus;
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
public class DeliveryDto {
    
    private String id;
    private String orderId;
    private String deliveryPartnerId;
    private String deliveryPartnerName;
    private String deliveryPartnerPhone;
    private DeliveryStatus status;
    private String pickupAddress;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String deliveryAddress;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private BigDecimal distanceKm;
    private BigDecimal deliveryFee;
    private Instant estimatedPickupTime;
    private Instant estimatedDeliveryTime;
    private Instant actualPickupTime;
    private Instant actualDeliveryTime;
    private String notes;
    private Instant createdAt;
}

