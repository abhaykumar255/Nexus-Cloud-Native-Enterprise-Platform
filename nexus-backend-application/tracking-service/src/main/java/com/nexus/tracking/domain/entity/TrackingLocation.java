package com.nexus.tracking.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "tracking_locations", indexes = {
        @Index(name = "idx_delivery_id", columnList = "delivery_id"),
        @Index(name = "idx_order_id", columnList = "order_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "delivery_id", nullable = false)
    private String deliveryId;
    
    @Column(name = "order_id", nullable = false)
    private String orderId;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(name = "speed_kmph")
    private Double speedKmph;
    
    @Column(name = "heading_degrees")
    private Double headingDegrees;
    
    @Column(name = "accuracy_meters")
    private Double accuracyMeters;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}

