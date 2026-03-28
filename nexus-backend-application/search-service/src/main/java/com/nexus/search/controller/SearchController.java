package com.nexus.search.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.search.document.TaskDocument;
import com.nexus.search.dto.SearchResponse;
import com.nexus.search.service.TaskSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Search REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final TaskSearchService taskSearchService;

    /**
     * Full-text search tasks
     * GET /api/v1/search/tasks?q=keyword&from=0&size=20
     */
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> searchTasks(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Search tasks request: query={}, from={}, size={}, userId={}", query, from, size, userId);

        SearchResponse<TaskDocument> response = taskSearchService.searchTasks(query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Search my tasks
     * GET /api/v1/search/tasks/my?q=keyword&from=0&size=20
     */
    @GetMapping("/tasks/my")
    public ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> searchMyTasks(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Search my tasks request: query={}, userId={}", query, userId);

        SearchResponse<TaskDocument> response = taskSearchService.searchTasksByAssignee(userId, query, from, size);

        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", response));
    }

    /**
     * Autocomplete task titles
     * GET /api/v1/search/autocomplete?prefix=impl
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<ApiResponse<List<String>>> autocomplete(
            @RequestParam("prefix") String prefix,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Autocomplete request: prefix={}", prefix);

        List<String> suggestions = taskSearchService.autocomplete(prefix);

        return ResponseEntity.ok(ApiResponse.success("Autocomplete results", suggestions));
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Search Service is running");
    }
}

