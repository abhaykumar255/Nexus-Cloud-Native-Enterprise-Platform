package com.nexus.tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingLocationDto {
    
    private String id;
    private String deliveryId;
    private String orderId;
    private Double latitude;
    private Double longitude;
    private Double speedKmph;
    private Double headingDegrees;
    private Double accuracyMeters;
    private Instant createdAt;
}

