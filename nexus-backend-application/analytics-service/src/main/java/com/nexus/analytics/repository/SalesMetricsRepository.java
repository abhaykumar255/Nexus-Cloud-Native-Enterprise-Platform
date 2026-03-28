package com.nexus.analytics.repository;

import com.nexus.analytics.domain.SalesMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesMetricsRepository extends MongoRepository<SalesMetrics, String> {
    
    Optional<SalesMetrics> findByDate(LocalDate date);
    
    List<SalesMetrics> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
    
    @Query("{ 'date': { $gte: ?0, $lte: ?1 } }")
    List<SalesMetrics> findMetricsBetweenDates(LocalDate startDate, LocalDate endDate);
    
    @Query(value = "{ 'date': { $gte: ?0 } }", sort = "{ 'date': -1 }")
    List<SalesMetrics> findRecentMetrics(LocalDate since);
}

