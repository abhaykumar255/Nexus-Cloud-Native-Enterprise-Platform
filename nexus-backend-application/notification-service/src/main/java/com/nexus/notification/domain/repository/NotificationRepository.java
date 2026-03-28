package com.nexus.notification.domain.repository;

import com.nexus.notification.domain.entity.Notification;
import com.nexus.notification.domain.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    Page<Notification> findByUserId(String userId, Pageable pageable);
    
    Page<Notification> findByUserIdAndStatus(String userId, NotificationStatus status, Pageable pageable);
    
    List<Notification> findByUserIdAndStatusAndCreatedAtAfter(
            String userId, NotificationStatus status, Instant createdAt);
    
    long countByUserIdAndStatus(String userId, NotificationStatus status);
    
    List<Notification> findByStatusAndCreatedAtBefore(NotificationStatus status, Instant createdAt);
    
    void deleteByExpiresAtBefore(Instant expiredAt);
}

