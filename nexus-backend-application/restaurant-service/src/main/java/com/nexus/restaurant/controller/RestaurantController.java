package com.nexus.restaurant.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.restaurant.dto.RestaurantDto;
import com.nexus.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Slf4j
public class RestaurantController {
    
    private final RestaurantService restaurantService;
    
    /**
     * Get all restaurants
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantDto>>> getAllRestaurants() {
        log.info("Fetching all restaurants");
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(ApiResponse.success("Restaurants retrieved successfully", restaurants));
    }
    
    /**
     * Get restaurant by ID
     */
    @GetMapping("/{restaurantId}")
    public ResponseEntity<ApiResponse<RestaurantDto>> getRestaurantById(
            @PathVariable String restaurantId) {
        
        log.info("Fetching restaurant: {}", restaurantId);
        RestaurantDto restaurant = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(ApiResponse.success("Restaurant retrieved successfully", restaurant));
    }
    
    /**
     * Get open restaurants
     */
    @GetMapping("/open")
    public ResponseEntity<ApiResponse<List<RestaurantDto>>> getOpenRestaurants() {
        log.info("Fetching open restaurants");
        List<RestaurantDto> restaurants = restaurantService.getOpenRestaurants();
        return ResponseEntity.ok(ApiResponse.success("Open restaurants retrieved successfully", restaurants));
    }
    
    /**
     * Get my restaurants (for sellers)
     */
    @GetMapping("/seller/me")
    public ResponseEntity<ApiResponse<List<RestaurantDto>>> getMyRestaurants(
            @RequestHeader("X-User-Id") String userId) {
        
        log.info("Fetching restaurants for seller: {}", userId);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsBySellerId(userId);
        return ResponseEntity.ok(ApiResponse.success("Restaurants retrieved successfully", restaurants));
    }
    
    /**
     * Update restaurant open/close status
     */
    @PatchMapping("/{restaurantId}/status")
    public ResponseEntity<ApiResponse<RestaurantDto>> updateOpenStatus(
            @PathVariable String restaurantId,
            @RequestParam Boolean isOpen) {
        
        log.info("Updating restaurant {} status to: {}", restaurantId, isOpen ? "OPEN" : "CLOSED");
        RestaurantDto restaurant = restaurantService.updateOpenStatus(restaurantId, isOpen);
        return ResponseEntity.ok(ApiResponse.success("Restaurant status updated successfully", restaurant));
    }
}

