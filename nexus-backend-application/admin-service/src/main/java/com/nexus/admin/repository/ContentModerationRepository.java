package com.nexus.admin.repository;

import com.nexus.admin.domain.ContentModeration;
import com.nexus.admin.domain.ContentModeration.EntityType;
import com.nexus.admin.domain.ContentModeration.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentModerationRepository extends JpaRepository<ContentModeration, Long> {
    
    List<ContentModeration> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
    
    Optional<ContentModeration> findByEntityTypeAndEntityIdAndStatus(
        EntityType entityType, Long entityId, ModerationStatus status
    );
    
    List<ContentModeration> findByStatus(ModerationStatus status);
    
    List<ContentModeration> findByReportedByUserId(Long userId);
    
    @Query("SELECT cm FROM ContentModeration cm WHERE cm.reportCount >= :threshold AND cm.status = 'PENDING'")
    List<ContentModeration> findHighPriorityReports(Integer threshold);
    
    @Query("SELECT COUNT(cm) FROM ContentModeration cm WHERE cm.status = :status")
    Long countByStatus(ModerationStatus status);
}

