package com.nexus.ai.domain.repository;

import com.nexus.ai.domain.document.UserBehaviorAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBehaviorAnalyticsRepository extends MongoRepository<UserBehaviorAnalytics, String> {
    Optional<UserBehaviorAnalytics> findByUserId(String userId);
}

