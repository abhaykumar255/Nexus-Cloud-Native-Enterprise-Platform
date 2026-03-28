package com.nexus.ai.service;

import com.nexus.ai.config.AiServiceProperties;
import com.nexus.ai.domain.document.UserBehaviorAnalytics;
import com.nexus.ai.domain.entity.UserRecommendationProfile;
import com.nexus.ai.domain.repository.UserBehaviorAnalyticsRepository;
import com.nexus.ai.domain.repository.UserRecommendationProfileRepository;
import com.nexus.ai.dto.RecommendationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendationService Tests")
class RecommendationServiceTest {

    @Mock
    private UserRecommendationProfileRepository userRecommendationProfileRepository;

    @Mock
    private UserBehaviorAnalyticsRepository userBehaviorAnalyticsRepository;

    @Mock
    private AiServiceProperties properties;

    @InjectMocks
    private RecommendationService recommendationService;

    private AiServiceProperties.RecommendationConfig recommendationConfig;
    private AiServiceProperties.ModelConfig modelConfig;

    @BeforeEach
    void setUp() {
        recommendationConfig = new AiServiceProperties.RecommendationConfig();
        recommendationConfig.setCacheTtlMinutes(30);
        recommendationConfig.setDefaultLimit(10);
        recommendationConfig.setMaxRecommendations(5);
        recommendationConfig.setDefaultRecommendations(3);
        recommendationConfig.setInitialConfidenceScore(0.85);
        recommendationConfig.setConfidenceDecrement(0.05);
        recommendationConfig.setDefaultConfidenceScore(0.70);

        modelConfig = new AiServiceProperties.ModelConfig();
        modelConfig.setVersion("1.3.0");

        lenient().when(properties.getRecommendation()).thenReturn(recommendationConfig);
        lenient().when(properties.getModel()).thenReturn(modelConfig);
    }

    @Test
    @DisplayName("Generate recommendations - User with profile and behavior")
    void generateRecommendations_userWithData() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRecommendationProfile profile = UserRecommendationProfile.builder()
                .userId(userId)
                .skills("Java, Spring Boot, Microservices")
                .taskCompletionRate(0.85)
                .totalTasksCompleted(50)
                .build();

        UserBehaviorAnalytics behavior = UserBehaviorAnalytics.builder()
                .userId(userId.toString())
                .totalTasksCompleted(50)
                .totalTasksCreated(60)
                .build();

        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(userBehaviorAnalyticsRepository.findByUserId(userId.toString())).thenReturn(Optional.of(behavior));

        // Act
        RecommendationResponse response = recommendationService.generateRecommendations(userId, 5);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendations()).isNotEmpty();
        assertThat(response.getRecommendations()).hasSizeLessThanOrEqualTo(5);
        assertThat(response.getModelVersion()).isEqualTo("1.3.0");
        assertThat(response.getComputedAt()).isNotNull();
        assertThat(response.getCachedUntil()).isNotNull();
        assertThat(response.getRecommendations().get(0).getConfidenceScore()).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("Generate recommendations - User without profile returns defaults")
    void generateRecommendations_userWithoutProfile() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userBehaviorAnalyticsRepository.findByUserId(userId.toString())).thenReturn(Optional.empty());

        // Act
        RecommendationResponse response = recommendationService.generateRecommendations(userId, 3);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendations()).isNotEmpty();
        assertThat(response.getRecommendations()).hasSizeLessThanOrEqualTo(3);
        assertThat(response.getModelVersion()).isEqualTo("1.3.0");
    }

    @Test
    @DisplayName("Generate recommendations - Limit exceeds max")
    void generateRecommendations_limitExceedsMax() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRecommendationProfile profile = UserRecommendationProfile.builder()
                .userId(userId)
                .build();
        UserBehaviorAnalytics behavior = UserBehaviorAnalytics.builder()
                .userId(userId.toString())
                .build();

        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(userBehaviorAnalyticsRepository.findByUserId(userId.toString())).thenReturn(Optional.of(behavior));

        // Act
        RecommendationResponse response = recommendationService.generateRecommendations(userId, 20); // Exceeds max

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendations()).hasSizeLessThanOrEqualTo(5); // Should be capped at max
    }

    @Test
    @DisplayName("Generate recommendations - Confidence scores decrease")
    void generateRecommendations_confidenceScoresDecrease() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRecommendationProfile profile = UserRecommendationProfile.builder()
                .userId(userId)
                .build();
        UserBehaviorAnalytics behavior = UserBehaviorAnalytics.builder()
                .userId(userId.toString())
                .build();

        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(userBehaviorAnalyticsRepository.findByUserId(userId.toString())).thenReturn(Optional.of(behavior));

        // Act
        RecommendationResponse response = recommendationService.generateRecommendations(userId, 5);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendations()).hasSizeGreaterThan(1);

        // Verify confidence scores decrease
        Double firstConfidence = response.getRecommendations().get(0).getConfidenceScore();
        Double secondConfidence = response.getRecommendations().get(1).getConfidenceScore();
        assertThat(firstConfidence).isGreaterThan(secondConfidence);
    }

    @Test
    @DisplayName("Update user profile - New profile")
    void updateUserProfile_newProfile() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String skills = "Java, Kubernetes";
        List<String> tags = Arrays.asList("backend", "devops");
        double completionRate = 0.85;

        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRecommendationProfileRepository.save(any(UserRecommendationProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        recommendationService.updateUserProfile(userId, skills, tags, completionRate);

        // Assert
        verify(userRecommendationProfileRepository, times(1)).findByUserId(userId);
        verify(userRecommendationProfileRepository, times(1)).save(any(UserRecommendationProfile.class));
    }

    @Test
    @DisplayName("Update user profile - Existing profile")
    void updateUserProfile_existingProfile() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRecommendationProfile existingProfile = UserRecommendationProfile.builder()
                .userId(userId)
                .totalTasksCompleted(10)
                .skills("Python")
                .build();

        String newSkills = "Java, Spring Boot";
        List<String> tags = Arrays.asList("microservices");
        double completionRate = 0.90;

        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.of(existingProfile));
        when(userRecommendationProfileRepository.save(any(UserRecommendationProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        recommendationService.updateUserProfile(userId, newSkills, tags, completionRate);

        // Assert
        verify(userRecommendationProfileRepository, times(1)).findByUserId(userId);
        verify(userRecommendationProfileRepository, times(1)).save(any(UserRecommendationProfile.class));
    }

    @Test
    @DisplayName("Generate recommendations - User with null skills")
    void generateRecommendations_userWithNullSkills() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRecommendationProfile profile = UserRecommendationProfile.builder()
                .userId(userId)
                .skills(null)  // Null skills
                .build();
        UserBehaviorAnalytics behavior = UserBehaviorAnalytics.builder()
                .userId(userId.toString())
                .build();

        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.of(profile));
        when(userBehaviorAnalyticsRepository.findByUserId(userId.toString())).thenReturn(Optional.of(behavior));

        // Act
        RecommendationResponse response = recommendationService.generateRecommendations(userId, 3);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendations()).isNotEmpty();
    }

    @Test
    @DisplayName("Generate default recommendations - Limit is applied")
    void generateDefaultRecommendations_limitApplied() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRecommendationProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userBehaviorAnalyticsRepository.findByUserId(userId.toString())).thenReturn(Optional.empty());

        // Act
        RecommendationResponse response = recommendationService.generateRecommendations(userId, 10);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRecommendations()).hasSizeLessThanOrEqualTo(10);
    }
}

