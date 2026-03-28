package com.nexus.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Restaurant document for Elasticsearch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDocument {
    
    private String id;
    private String name;
    private String description;
    private String cuisineType;
    private List<String> cuisineTypes;
    private String sellerId;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private String email;
    private Boolean isOpen;
    private String openingTime;
    private String closingTime;
    private BigDecimal rating;
    private Long totalReviews;
    private BigDecimal minOrderAmount;
    private BigDecimal deliveryFee;
    private Integer averageDeliveryTimeMinutes;
    private String status;
    private List<String> popularDishes;
    private Instant createdAt;
}

