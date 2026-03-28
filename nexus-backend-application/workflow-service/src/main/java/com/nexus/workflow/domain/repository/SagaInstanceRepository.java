package com.nexus.workflow.domain.repository;

import com.nexus.workflow.domain.entity.SagaInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for SagaInstance
 */
@Repository
public interface SagaInstanceRepository extends JpaRepository<SagaInstance, UUID> {

    Optional<SagaInstance> findByCorrelationId(String correlationId);

    List<SagaInstance> findByStatus(SagaInstance.SagaStatus status);

    List<SagaInstance> findBySagaType(String sagaType);
}

