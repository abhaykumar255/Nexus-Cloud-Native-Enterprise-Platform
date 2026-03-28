package com.nexus.ai.domain.repository;

import com.nexus.ai.domain.entity.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, UUID> {
    
    List<PredictionHistory> findByTaskId(UUID taskId);
    
    List<PredictionHistory> findByUserId(UUID userId);
    
    @Query("SELECT p FROM PredictionHistory p WHERE p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<PredictionHistory> findRecentPredictions(@Param("since") LocalDateTime since);
}

