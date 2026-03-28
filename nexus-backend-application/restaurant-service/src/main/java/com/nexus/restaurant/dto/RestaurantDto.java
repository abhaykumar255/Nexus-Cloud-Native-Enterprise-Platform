package com.nexus.restaurant.dto;

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
public class RestaurantDto {
    
    private String id;
    private String sellerId;
    private String name;
    private String description;
    private String cuisineType;
    private String phoneNumber;
    private String email;
    private String address;
    private Double latitude;
    private Double longitude;
    private String openingTime;
    private String closingTime;
    private Boolean isOpen;
    private String status;
    private BigDecimal rating;
    private Long totalReviews;
    private String logoUrl;
    private String bannerUrl;
    private BigDecimal minOrderAmount;
    private BigDecimal deliveryFee;
    private Integer averageDeliveryTimeMinutes;
    private Instant createdAt;
}

