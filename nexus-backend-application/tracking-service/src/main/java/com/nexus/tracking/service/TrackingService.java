package com.nexus.tracking.service;

import com.nexus.tracking.domain.entity.TrackingLocation;
import com.nexus.tracking.domain.repository.TrackingLocationRepository;
import com.nexus.tracking.dto.TrackingLocationDto;
import com.nexus.tracking.dto.UpdateLocationRequest;
import com.nexus.tracking.mapper.TrackingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {
    
    private final TrackingLocationRepository trackingLocationRepository;
    private final TrackingMapper trackingMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String LOCATION_CACHE_KEY_PREFIX = "tracking:location:";
    private static final long LOCATION_TTL_MINUTES = 60;
    
    /**
     * Update tracking location
     */
    @Transactional
    public TrackingLocationDto updateLocation(UpdateLocationRequest request) {
        log.info("Updating location for order: {}, delivery: {}", request.getOrderId(), request.getDeliveryId());
        
        // Save to PostgreSQL for history
        TrackingLocation location = TrackingLocation.builder()
                .deliveryId(request.getDeliveryId())
                .orderId(request.getOrderId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .speedKmph(request.getSpeedKmph())
                .headingDegrees(request.getHeadingDegrees())
                .accuracyMeters(request.getAccuracyMeters())
                .build();
        
        location = trackingLocationRepository.save(location);
        TrackingLocationDto dto = trackingMapper.toDto(location);
        
        // Cache latest location in Redis for real-time access
        String cacheKey = LOCATION_CACHE_KEY_PREFIX + request.getOrderId();
        redisTemplate.opsForValue().set(cacheKey, dto, LOCATION_TTL_MINUTES, TimeUnit.MINUTES);
        
        log.info("Location updated successfully: {}", location.getId());
        return dto;
    }
    
    /**
     * Get latest location for an order
     */
    public TrackingLocationDto getLatestLocation(String orderId) {
        // Try Redis first
        String cacheKey = LOCATION_CACHE_KEY_PREFIX + orderId;
        TrackingLocationDto cached = (TrackingLocationDto) redisTemplate.opsForValue().get(cacheKey);
        
        if (cached != null) {
            log.debug("Location found in cache for order: {}", orderId);
            return cached;
        }
        
        // Fallback to PostgreSQL
        log.debug("Location not in cache, fetching from database for order: {}", orderId);
        return trackingLocationRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId)
                .map(trackingMapper::toDto)
                .orElse(null);
    }
    
    /**
     * Get location history for an order
     */
    public List<TrackingLocationDto> getLocationHistory(String orderId) {
        log.info("Fetching location history for order: {}", orderId);
        List<TrackingLocation> locations = trackingLocationRepository.findByOrderIdOrderByCreatedAtDesc(orderId);
        return trackingMapper.toDtoList(locations);
    }
}

