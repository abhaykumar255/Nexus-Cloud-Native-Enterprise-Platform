package com.nexus.delivery.service;

import com.nexus.common.enums.DeliveryStatus;
import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.delivery.domain.entity.Delivery;
import com.nexus.delivery.domain.repository.DeliveryRepository;
import com.nexus.delivery.dto.DeliveryDto;
import com.nexus.delivery.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    
    /**
     * Create a new delivery
     */
    @Transactional
    public DeliveryDto createDelivery(String orderId, String pickupAddress, Double pickupLat, Double pickupLng,
                                      String deliveryAddress, Double deliveryLat, Double deliveryLng) {
        log.info("Creating delivery for order: {}", orderId);
        
        // Calculate distance and fee (simplified)
        BigDecimal distance = calculateDistance(pickupLat, pickupLng, deliveryLat, deliveryLng);
        BigDecimal fee = calculateDeliveryFee(distance);
        
        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .status(DeliveryStatus.PENDING_ASSIGNMENT)
                .pickupAddress(pickupAddress)
                .pickupLatitude(pickupLat)
                .pickupLongitude(pickupLng)
                .deliveryAddress(deliveryAddress)
                .deliveryLatitude(deliveryLat)
                .deliveryLongitude(deliveryLng)
                .distanceKm(distance)
                .deliveryFee(fee)
                .estimatedDeliveryTime(Instant.now().plusSeconds(3600)) // 1 hour ETA
                .build();
        
        delivery = deliveryRepository.save(delivery);
        log.info("Delivery created: {}", delivery.getId());
        
        return deliveryMapper.toDto(delivery);
    }
    
    /**
     * Assign delivery partner
     */
    @Transactional
    public DeliveryDto assignPartner(String deliveryId, String partnerId, String partnerName, String partnerPhone) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));
        
        delivery.setDeliveryPartnerId(partnerId);
        delivery.setDeliveryPartnerName(partnerName);
        delivery.setDeliveryPartnerPhone(partnerPhone);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setEstimatedPickupTime(Instant.now().plusSeconds(1800)); // 30 min to pickup
        
        delivery = deliveryRepository.save(delivery);
        log.info("Delivery {} assigned to partner {}", deliveryId, partnerId);
        
        return deliveryMapper.toDto(delivery);
    }
    
    /**
     * Update delivery status
     */
    @Transactional
    public DeliveryDto updateStatus(String deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));
        
        delivery.setStatus(newStatus);
        
        if (newStatus == DeliveryStatus.PICKED_UP) {
            delivery.setActualPickupTime(Instant.now());
        } else if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setActualDeliveryTime(Instant.now());
        }
        
        delivery = deliveryRepository.save(delivery);
        log.info("Delivery {} status updated to {}", deliveryId, newStatus);
        
        return deliveryMapper.toDto(delivery);
    }
    
    /**
     * Get delivery by order ID
     */
    public DeliveryDto getDeliveryByOrderId(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found for order: " + orderId));
        return deliveryMapper.toDto(delivery);
    }
    
    // Helper methods
    
    private BigDecimal calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // Simplified Haversine formula
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return BigDecimal.TEN;
        }
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = 6371 * c; // Earth radius in km
        
        return BigDecimal.valueOf(distance).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    private BigDecimal calculateDeliveryFee(BigDecimal distance) {
        BigDecimal baseFee = new BigDecimal("5.00");
        BigDecimal perKmFee = new BigDecimal("1.50");
        return baseFee.add(distance.multiply(perKmFee));
    }
}

