package com.nexus.analytics.repository;

import com.nexus.analytics.domain.UserBehavior;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserBehaviorRepository extends MongoRepository<UserBehavior, String> {
    
    List<UserBehavior> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    List<UserBehavior> findBySessionId(String sessionId);
    
    List<UserBehavior> findByEventType(String eventType);
    
    @Query("{ 'eventType': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<UserBehavior> findByEventTypeAndTimeRange(String eventType, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'userId': ?0, 'eventType': ?1 }")
    List<UserBehavior> findByUserIdAndEventType(Long userId, String eventType);
    
    long countByEventTypeAndTimestampBetween(String eventType, LocalDateTime start, LocalDateTime end);
}

