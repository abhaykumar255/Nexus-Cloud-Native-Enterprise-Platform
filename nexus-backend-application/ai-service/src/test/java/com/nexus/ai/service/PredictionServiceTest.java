package com.nexus.ai.service;

import com.nexus.ai.config.AiServiceProperties;
import com.nexus.ai.domain.entity.PredictionHistory;
import com.nexus.ai.domain.repository.PredictionHistoryRepository;
import com.nexus.ai.dto.PredictionRequest;
import com.nexus.ai.dto.PredictionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PredictionService Tests")
class PredictionServiceTest {

    @Mock
    private PredictionHistoryRepository predictionHistoryRepository;

    @Mock
    private AiServiceProperties properties;

    @InjectMocks
    private PredictionService predictionService;

    private AiServiceProperties.PredictionConfig predictionConfig;

    @BeforeEach
    void setUp() {
        predictionConfig = new AiServiceProperties.PredictionConfig();
        predictionConfig.setBaseEstimateDays(3.0);
        predictionConfig.setBaseConfidence(0.75);
        predictionConfig.setConfidenceBoostForAssignee(0.05);
        predictionConfig.setConfidenceBoostForTags(0.03);
        predictionConfig.setMaxConfidence(0.95);
        predictionConfig.setAuthTaskMultiplier(1.2);
        predictionConfig.setHighComplexityVariance(30.0);
        predictionConfig.setAuthUnderestimation(20.0);

        HashMap<String, Double> complexityMultipliers = new HashMap<>();
        complexityMultipliers.put("LOW", 1.0);
        complexityMultipliers.put("MEDIUM", 1.5);
        complexityMultipliers.put("HIGH", 2.5);
        predictionConfig.setComplexityMultipliers(complexityMultipliers);

        HashMap<String, Double> priorityMultipliers = new HashMap<>();
        priorityMultipliers.put("LOW", 1.2);
        priorityMultipliers.put("MEDIUM", 1.0);
        priorityMultipliers.put("HIGH", 0.9);
        predictionConfig.setPriorityMultipliers(priorityMultipliers);

        HashMap<String, String> riskMessages = new HashMap<>();
        riskMessages.put("HIGH_COMPLEXITY", "High complexity tasks have {variance}% variance in completion time");
        riskMessages.put("AUTH_TAG", "Auth complexity historically underestimated by {underestimation}%");
        riskMessages.put("UNASSIGNED", "Unassigned tasks typically take longer to start");
        predictionConfig.setRiskMessages(riskMessages);

        // Mock model config
        AiServiceProperties.ModelConfig modelConfig = new AiServiceProperties.ModelConfig();
        modelConfig.setVersion("1.3.0");

        lenient().when(properties.getPrediction()).thenReturn(predictionConfig);
        lenient().when(properties.getModel()).thenReturn(modelConfig);
        lenient().when(predictionHistoryRepository.save(any(PredictionHistory.class))).thenReturn(new PredictionHistory());
    }

    @Test
    @DisplayName("Predict completion time - High priority and complexity")
    void predictCompletionTime_highPriorityAndComplexity() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Implement OAuth2")
                .priority("HIGH")
                .complexity("HIGH")
                .assigneeId(UUID.randomUUID())
                .tags(Arrays.asList("auth", "security"))
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEstimatedDays()).isGreaterThan(0);
        assertThat(response.getConfidence()).isBetween(0.0, 1.0);
        assertThat(response.getBasis()).isNotNull();
        assertThat(response.getRiskFactors()).isNotNull();
        verify(predictionHistoryRepository).save(any(PredictionHistory.class));
    }

    @Test
    @DisplayName("Predict completion time - Medium priority and complexity with no assignee")
    void predictCompletionTime_mediumPriorityNoAssignee() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Fix bug")
                .priority("MEDIUM")
                .complexity("MEDIUM")
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEstimatedDays()).isGreaterThan(0);
        assertThat(response.getConfidence()).isLessThan(0.9); // Lower confidence without assignee
        assertThat(response.getRiskFactors()).contains("Unassigned tasks typically take longer to start");
    }

    @Test
    @DisplayName("Predict completion time - Low complexity and priority")
    void predictCompletionTime_lowComplexityAndPriority() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Update documentation")
                .priority("LOW")
                .complexity("LOW")
                .assigneeId(UUID.randomUUID())
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEstimatedDays()).isGreaterThan(0);
        assertThat(response.getConfidence()).isGreaterThan(0.5);
    }

    @Test
    @DisplayName("Predict completion time - Null priority and complexity defaults to MEDIUM")
    void predictCompletionTime_nullPriorityAndComplexity() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Generic task")
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEstimatedDays()).isEqualTo(4.5); // 3.0 * 1.5 (MEDIUM complexity) * 1.0 (MEDIUM priority)
    }

    @Test
    @DisplayName("Predict completion time - Auth tag increases estimate")
    void predictCompletionTime_authTagMultiplier() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Implement authentication")
                .priority("MEDIUM")
                .complexity("MEDIUM")
                .tags(Arrays.asList("auth", "security"))
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEstimatedDays()).isGreaterThan(4.5); // Should be 4.5 * 1.2 = 5.4
        assertThat(response.getRiskFactors()).anySatisfy(risk ->
            assertThat(risk).contains("underestimated")
        );
    }

    @Test
    @DisplayName("Predict completion time - High complexity generates risk factor")
    void predictCompletionTime_highComplexityRiskFactor() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Complex refactoring")
                .priority("MEDIUM")
                .complexity("HIGH")
                .assigneeId(UUID.randomUUID())
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getRiskFactors()).anySatisfy(risk ->
            assertThat(risk).contains("variance")
        );
    }

    @Test
    @DisplayName("Predict completion time - Empty tags list")
    void predictCompletionTime_emptyTagsList() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Simple task")
                .priority("LOW")
                .complexity("LOW")
                .tags(Arrays.asList()) // Empty list
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEstimatedDays()).isGreaterThan(0);
        // Empty tags mean lower confidence than if tags were present
        assertThat(response.getConfidence()).isLessThan(1.0);
    }

    @Test
    @DisplayName("Predict completion time - Confidence capped at max")
    void predictCompletionTime_confidenceCappedAtMax() {
        // Arrange
        PredictionRequest request = PredictionRequest.builder()
                .taskId(UUID.randomUUID())
                .title("Well-defined task")
                .priority("MEDIUM")
                .complexity("MEDIUM")
                .assigneeId(UUID.randomUUID())
                .tags(Arrays.asList("backend", "api", "database"))
                .build();

        // Act
        PredictionResponse response = predictionService.predictCompletionTime(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getConfidence()).isLessThanOrEqualTo(0.95); // Max confidence
        assertThat(response.getConfidence()).isGreaterThan(0.75); // Base confidence
        verify(predictionHistoryRepository).save(any(PredictionHistory.class));
    }
}

