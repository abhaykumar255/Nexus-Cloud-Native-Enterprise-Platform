package com.nexus.ai.domain.repository;

import com.nexus.ai.domain.entity.UserRecommendationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRecommendationProfileRepository extends JpaRepository<UserRecommendationProfile, UUID> {
    Optional<UserRecommendationProfile> findByUserId(UUID userId);
}

