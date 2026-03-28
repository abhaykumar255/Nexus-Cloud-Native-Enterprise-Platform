package com.nexus.ai.service;

import com.nexus.ai.config.AiServiceProperties;
import com.nexus.ai.domain.document.UserBehaviorAnalytics;
import com.nexus.ai.domain.entity.UserRecommendationProfile;
import com.nexus.ai.domain.repository.UserBehaviorAnalyticsRepository;
import com.nexus.ai.domain.repository.UserRecommendationProfileRepository;
import com.nexus.ai.dto.RecommendationResponse;
import com.nexus.ai.dto.TaskRecommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Recommendation Service - Task recommendations using collaborative filtering
 * Uses user behavior analytics and recommendation profiles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final UserRecommendationProfileRepository profileRepository;
    private final UserBehaviorAnalyticsRepository behaviorRepository;
    private final AiServiceProperties properties;

    /**
     * Generate task recommendations for a user
     * Caches results for 30 minutes (managed by @Cacheable in Redis)
     */
    @Cacheable(value = "task-recommendations", key = "#userId", unless = "#result == null")
    public RecommendationResponse generateRecommendations(UUID userId, int limit) {
        log.info("Generating task recommendations for user: {} with limit: {}", userId, limit);
        
        // Fetch user profile and behavior
        UserRecommendationProfile profile = profileRepository.findByUserId(userId).orElse(null);
        UserBehaviorAnalytics behavior = behaviorRepository.findByUserId(userId.toString()).orElse(null);
        
        List<TaskRecommendation> recommendations = new ArrayList<>();
        
        if (profile != null && behavior != null) {
            // Real collaborative filtering logic would go here
            // For now, we generate sample recommendations based on user patterns
            recommendations = generateCollaborativeRecommendations(profile, behavior, limit);
        } else {
            log.warn("No profile or behavior data found for user: {}. Returning default recommendations.", userId);
            recommendations = generateDefaultRecommendations(limit);
        }
        
        LocalDateTime now = LocalDateTime.now();
        return RecommendationResponse.builder()
                .recommendations(recommendations)
                .modelVersion(properties.getModel().getVersion())
                .computedAt(now)
                .cachedUntil(now.plusMinutes(properties.getRecommendation().getCacheTtlMinutes()))
                .build();
    }

    /**
     * Generate recommendations using collaborative filtering algorithm
     */
    private List<TaskRecommendation> generateCollaborativeRecommendations(
            UserRecommendationProfile profile, 
            UserBehaviorAnalytics behavior, 
            int limit) {
        
        List<TaskRecommendation> recommendations = new ArrayList<>();
        
        // Sample implementation - in real scenario, this would use ML model
        // or query Neo4j for graph-based recommendations

        int maxRecs = Math.min(limit, properties.getRecommendation().getMaxRecommendations());
        for (int i = 0; i < maxRecs; i++) {
            recommendations.add(TaskRecommendation.builder()
                    .taskId(UUID.randomUUID())
                    .title("Recommended Task " + (i + 1) + " based on user skills")
                    .confidenceScore(properties.getRecommendation().getInitialConfidenceScore() -
                                   (i * properties.getRecommendation().getConfidenceDecrement()))
                    .reason("Matched skills: " + (profile.getSkills() != null ? profile.getSkills() : "general") +
                           " + team graph overlap")
                    .build());
        }

        return recommendations;
    }

    /**
     * Generate default recommendations for new users
     */
    private List<TaskRecommendation> generateDefaultRecommendations(int limit) {
        List<TaskRecommendation> recommendations = new ArrayList<>();

        int defaultRecs = Math.min(limit, properties.getRecommendation().getDefaultRecommendations());
        for (int i = 0; i < defaultRecs; i++) {
            recommendations.add(TaskRecommendation.builder()
                    .taskId(UUID.randomUUID())
                    .title("Getting Started Task " + (i + 1))
                    .confidenceScore(properties.getRecommendation().getDefaultConfidenceScore() -
                                   (i * properties.getRecommendation().getConfidenceDecrement()))
                    .reason("Popular among new users")
                    .build());
        }

        return recommendations;
    }

    /**
     * Update user recommendation profile based on task completion
     */
    public void updateUserProfile(UUID userId, String skills, List<String> tags, double completionRate) {
        log.info("Updating recommendation profile for user: {}", userId);
        
        UserRecommendationProfile profile = profileRepository.findByUserId(userId)
                .orElse(UserRecommendationProfile.builder()
                        .userId(userId)
                        .totalTasksCompleted(0)
                        .build());
        
        profile.setSkills(skills);
        profile.setPreferredTags(String.join(",", tags));
        profile.setTaskCompletionRate(completionRate);
        profile.setModelVersion(properties.getModel().getVersion());
        profile.setTotalTasksCompleted(profile.getTotalTasksCompleted() + 1);
        
        profileRepository.save(profile);
        log.info("User profile updated successfully for user: {}", userId);
    }
}

