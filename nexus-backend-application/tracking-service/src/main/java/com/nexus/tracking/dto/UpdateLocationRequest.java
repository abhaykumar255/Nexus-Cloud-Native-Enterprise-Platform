package com.nexus.tracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationRequest {
    
    @NotBlank(message = "Delivery ID is required")
    private String deliveryId;
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotNull(message = "Latitude is required")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
    private Double speedKmph;
    private Double headingDegrees;
    private Double accuracyMeters;
}

