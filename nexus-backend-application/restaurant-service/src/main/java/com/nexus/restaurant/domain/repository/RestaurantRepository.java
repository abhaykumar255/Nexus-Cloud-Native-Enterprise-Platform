package com.nexus.restaurant.domain.repository;

import com.nexus.restaurant.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    
    List<Restaurant> findBySellerId(String sellerId);
    
    List<Restaurant> findByStatus(String status);
    
    List<Restaurant> findByIsOpen(Boolean isOpen);
    
    List<Restaurant> findByCuisineType(String cuisineType);
    
    Optional<Restaurant> findBySellerIdAndId(String sellerId, String id);
}

