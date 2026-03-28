package com.nexus.admin.repository;

import com.nexus.admin.domain.UserModeration;
import com.nexus.admin.domain.UserModeration.ModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserModerationRepository extends JpaRepository<UserModeration, Long> {
    
    Optional<UserModeration> findByUserId(Long userId);
    
    List<UserModeration> findByStatus(ModerationStatus status);
    
    List<UserModeration> findByStatusIn(List<ModerationStatus> statuses);
    
    @Query("SELECT um FROM UserModeration um WHERE um.violationCount >= :threshold")
    List<UserModeration> findUsersWithHighViolations(Integer threshold);
    
    @Query("SELECT COUNT(um) FROM UserModeration um WHERE um.status = :status")
    Long countByStatus(ModerationStatus status);
}

