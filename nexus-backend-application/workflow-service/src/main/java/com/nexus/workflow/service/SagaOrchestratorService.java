package com.nexus.workflow.service;

import com.nexus.workflow.domain.entity.SagaInstance;
import com.nexus.workflow.domain.repository.SagaInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Saga Orchestrator Service
 * Manages distributed transactions across multiple microservices
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SagaOrchestratorService {

    private final SagaInstanceRepository sagaInstanceRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Start a new saga
     */
    @Transactional
    public SagaInstance startSaga(String sagaType, String correlationId, String payload) {
        log.info("Starting saga: type={}, correlationId={}", sagaType, correlationId);

        SagaInstance saga = SagaInstance.builder()
                .sagaType(sagaType)
                .correlationId(correlationId)
                .status(SagaInstance.SagaStatus.STARTED)
                .currentStep("STEP_1")
                .payload(payload)
                .retryCount(0)
                .build();

        saga = sagaInstanceRepository.save(saga);

        // Publish first step event
        publishStepEvent(saga, "STEP_1");

        return saga;
    }

    /**
     * Advance saga to next step
     */
    @Transactional
    public SagaInstance advanceSaga(String correlationId, String completedStep, SagaInstance.SagaStatus newStatus) {
        log.info("Advancing saga: correlationId={}, completedStep={}", correlationId, completedStep);

        SagaInstance saga = sagaInstanceRepository.findByCorrelationId(correlationId)
                .orElseThrow(() -> new RuntimeException("Saga not found: " + correlationId));

        saga.setStatus(newStatus);
        saga.setCurrentStep(getNextStep(completedStep));
        saga.setUpdatedAt(LocalDateTime.now());

        if (newStatus == SagaInstance.SagaStatus.COMPLETED) {
            saga.setCompletedAt(LocalDateTime.now());
            log.info("Saga completed: {}", correlationId);
        } else {
            // Publish next step event
            publishStepEvent(saga, saga.getCurrentStep());
        }

        return sagaInstanceRepository.save(saga);
    }

    /**
     * Compensate saga (rollback)
     */
    @Transactional
    public SagaInstance compensateSaga(String correlationId, String errorMessage) {
        log.warn("Compensating saga: correlationId={}, error={}", correlationId, errorMessage);

        SagaInstance saga = sagaInstanceRepository.findByCorrelationId(correlationId)
                .orElseThrow(() -> new RuntimeException("Saga not found: " + correlationId));

        saga.setStatus(SagaInstance.SagaStatus.COMPENSATING);
        saga.setErrorMessage(errorMessage);
        saga.setUpdatedAt(LocalDateTime.now());

        // Publish compensation events
        publishCompensationEvent(saga);

        return sagaInstanceRepository.save(saga);
    }

    /**
     * Mark saga as compensated
     */
    @Transactional
    public SagaInstance markCompensated(String correlationId) {
        SagaInstance saga = sagaInstanceRepository.findByCorrelationId(correlationId)
                .orElseThrow(() -> new RuntimeException("Saga not found: " + correlationId));

        saga.setStatus(SagaInstance.SagaStatus.COMPENSATED);
        saga.setUpdatedAt(LocalDateTime.now());

        return sagaInstanceRepository.save(saga);
    }

    /**
     * Get saga by correlation ID
     */
    public SagaInstance getSaga(String correlationId) {
        return sagaInstanceRepository.findByCorrelationId(correlationId)
                .orElseThrow(() -> new RuntimeException("Saga not found: " + correlationId));
    }

    /**
     * Get all sagas
     */
    public List<SagaInstance> getAllSagas() {
        return sagaInstanceRepository.findAll();
    }

    // Helper methods

    private String getNextStep(String currentStep) {
        return switch (currentStep) {
            case "STEP_1" -> "STEP_2";
            case "STEP_2" -> "STEP_3";
            case "STEP_3" -> "COMPLETED";
            default -> "COMPLETED";
        };
    }

    private void publishStepEvent(SagaInstance saga, String step) {
        String topic = "saga." + saga.getSagaType().toLowerCase() + "." + step.toLowerCase();
        kafkaTemplate.send(topic, saga.getCorrelationId(), saga);
        log.info("Published saga step event: topic={}, correlationId={}", topic, saga.getCorrelationId());
    }

    private void publishCompensationEvent(SagaInstance saga) {
        String topic = "saga." + saga.getSagaType().toLowerCase() + ".compensate";
        kafkaTemplate.send(topic, saga.getCorrelationId(), saga);
        log.info("Published saga compensation event: topic={}, correlationId={}", topic, saga.getCorrelationId());
    }
}

