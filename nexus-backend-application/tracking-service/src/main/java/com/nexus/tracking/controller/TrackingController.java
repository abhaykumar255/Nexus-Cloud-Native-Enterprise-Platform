package com.nexus.tracking.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.tracking.dto.TrackingLocationDto;
import com.nexus.tracking.dto.UpdateLocationRequest;
import com.nexus.tracking.service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
@Slf4j
public class TrackingController {
    
    private final TrackingService trackingService;
    
    /**
     * Update location (for delivery partners)
     */
    @PostMapping("/location")
    public ResponseEntity<ApiResponse<TrackingLocationDto>> updateLocation(
            @Valid @RequestBody UpdateLocationRequest request) {
        
        log.info("Updating location for order: {}", request.getOrderId());
        TrackingLocationDto location = trackingService.updateLocation(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Location updated successfully", location));
    }
    
    /**
     * Get latest location for an order
     */
    @GetMapping("/order/{orderId}/latest")
    public ResponseEntity<ApiResponse<TrackingLocationDto>> getLatestLocation(
            @PathVariable String orderId) {
        
        log.info("Fetching latest location for order: {}", orderId);
        TrackingLocationDto location = trackingService.getLatestLocation(orderId);
        
        if (location == null) {
            return ResponseEntity.ok(ApiResponse.success("No tracking data available", null));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Latest location retrieved successfully", location));
    }
    
    /**
     * Get location history for an order
     */
    @GetMapping("/order/{orderId}/history")
    public ResponseEntity<ApiResponse<List<TrackingLocationDto>>> getLocationHistory(
            @PathVariable String orderId) {
        
        log.info("Fetching location history for order: {}", orderId);
        List<TrackingLocationDto> locations = trackingService.getLocationHistory(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("Location history retrieved successfully", locations));
    }
}

