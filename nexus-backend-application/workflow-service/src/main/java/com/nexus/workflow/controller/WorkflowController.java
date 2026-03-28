package com.nexus.workflow.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.workflow.domain.entity.SagaInstance;
import com.nexus.workflow.service.SagaOrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Workflow REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final SagaOrchestratorService sagaOrchestratorService;

    /**
     * Start a new saga
     * POST /api/v1/workflows/sagas
     */
    @PostMapping("/sagas")
    public ResponseEntity<ApiResponse<SagaInstance>> startSaga(
            @RequestBody Map<String, String> request,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Start saga request: type={}, correlationId={}", 
                request.get("sagaType"), request.get("correlationId"));

        SagaInstance saga = sagaOrchestratorService.startSaga(
                request.get("sagaType"),
                request.get("correlationId"),
                request.get("payload")
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Saga started successfully", saga));
    }

    /**
     * Get saga by correlation ID
     * GET /api/v1/workflows/sagas/{correlationId}
     */
    @GetMapping("/sagas/{correlationId}")
    public ResponseEntity<ApiResponse<SagaInstance>> getSaga(
            @PathVariable String correlationId,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Get saga request: correlationId={}", correlationId);

        SagaInstance saga = sagaOrchestratorService.getSaga(correlationId);

        return ResponseEntity.ok(ApiResponse.success(saga));
    }

    /**
     * Get all sagas
     * GET /api/v1/workflows/sagas
     */
    @GetMapping("/sagas")
    public ResponseEntity<ApiResponse<List<SagaInstance>>> getAllSagas(
            @RequestHeader("X-User-Id") String userId) {

        log.info("Get all sagas request");

        List<SagaInstance> sagas = sagaOrchestratorService.getAllSagas();

        return ResponseEntity.ok(ApiResponse.success("Sagas retrieved successfully", sagas));
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Workflow Service is running");
    }
}

