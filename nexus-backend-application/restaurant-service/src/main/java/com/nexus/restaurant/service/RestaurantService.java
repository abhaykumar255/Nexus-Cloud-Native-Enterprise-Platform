package com.nexus.restaurant.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.restaurant.domain.entity.Restaurant;
import com.nexus.restaurant.domain.repository.RestaurantRepository;
import com.nexus.restaurant.dto.RestaurantDto;
import com.nexus.restaurant.mapper.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    
    /**
     * Get restaurant by ID
     */
    public RestaurantDto getRestaurantById(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + restaurantId));
        return restaurantMapper.toDto(restaurant);
    }
    
    /**
     * Get all restaurants
     */
    public List<RestaurantDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurantMapper.toDtoList(restaurants);
    }
    
    /**
     * Get restaurants by seller ID
     */
    public List<RestaurantDto> getRestaurantsBySellerId(String sellerId) {
        List<Restaurant> restaurants = restaurantRepository.findBySellerId(sellerId);
        return restaurantMapper.toDtoList(restaurants);
    }
    
    /**
     * Get open restaurants
     */
    public List<RestaurantDto> getOpenRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findByIsOpen(true);
        return restaurantMapper.toDtoList(restaurants);
    }
    
    /**
     * Update restaurant open/close status
     */
    @Transactional
    public RestaurantDto updateOpenStatus(String restaurantId, Boolean isOpen) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + restaurantId));
        
        restaurant.setIsOpen(isOpen);
        restaurant = restaurantRepository.save(restaurant);
        
        log.info("Restaurant {} is now {}", restaurantId, isOpen ? "OPEN" : "CLOSED");
        return restaurantMapper.toDto(restaurant);
    }
}

