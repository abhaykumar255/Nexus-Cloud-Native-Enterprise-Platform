package com.nexus.analytics.repository;

import com.nexus.analytics.domain.ProductPerformance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPerformanceRepository extends MongoRepository<ProductPerformance, String> {
    
    Optional<ProductPerformance> findByProductIdAndDate(Long productId, LocalDate date);
    
    List<ProductPerformance> findByProductId(Long productId);
    
    List<ProductPerformance> findByDateBetweenOrderByRevenueDesc(LocalDate startDate, LocalDate endDate);
    
    List<ProductPerformance> findByCategoryAndDateBetween(String category, LocalDate startDate, LocalDate endDate);
    
    @Query(value = "{ 'date': { $gte: ?0, $lte: ?1 } }", sort = "{ 'unitsSold': -1 }")
    List<ProductPerformance> findTopSellingProducts(LocalDate startDate, LocalDate endDate);
    
    @Query(value = "{ 'date': { $gte: ?0, $lte: ?1 } }", sort = "{ 'revenue': -1 }")
    List<ProductPerformance> findTopRevenueProducts(LocalDate startDate, LocalDate endDate);
}

