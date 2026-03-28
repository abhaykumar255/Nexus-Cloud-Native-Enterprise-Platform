package com.nexus.notification.domain.repository;

import com.nexus.notification.domain.entity.NotificationPreference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends MongoRepository<NotificationPreference, String> {
    
    Optional<NotificationPreference> findByUserId(String userId);
}

