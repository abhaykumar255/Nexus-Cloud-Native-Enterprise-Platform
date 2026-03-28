package com.nexus.tracking.domain.repository;

import com.nexus.tracking.domain.entity.TrackingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingLocationRepository extends JpaRepository<TrackingLocation, String> {
    
    List<TrackingLocation> findByOrderIdOrderByCreatedAtDesc(String orderId);
    
    List<TrackingLocation> findByDeliveryIdOrderByCreatedAtDesc(String deliveryId);
    
    Optional<TrackingLocation> findFirstByOrderIdOrderByCreatedAtDesc(String orderId);
    
    List<TrackingLocation> findByOrderIdAndCreatedAtAfterOrderByCreatedAtDesc(String orderId, Instant since);
}

