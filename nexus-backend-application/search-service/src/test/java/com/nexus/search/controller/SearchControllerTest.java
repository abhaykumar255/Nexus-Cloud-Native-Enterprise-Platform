package com.nexus.search.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.search.document.TaskDocument;
import com.nexus.search.dto.SearchResponse;
import com.nexus.search.service.TaskSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchController Tests")
class SearchControllerTest {

    @Mock
    private TaskSearchService taskSearchService;

    @InjectMocks
    private SearchController searchController;

    private SearchResponse<TaskDocument> sampleSearchResponse;

    @BeforeEach
    void setUp() {
        TaskDocument task = TaskDocument.builder()
                .id("task-123")
                .title("Test Task")
                .description("Test description")
                .build();

        sampleSearchResponse = SearchResponse.<TaskDocument>builder()
                .results(Arrays.asList(task))
                .totalHits(1L)
                .from(0)
                .size(20)
                .took(100L)
                .build();
    }

    @Test
    @DisplayName("Search tasks - Success")
    void searchTasks_success() {
        // Arrange
        when(taskSearchService.searchTasks(anyString(), anyInt(), anyInt()))
                .thenReturn(sampleSearchResponse);

        // Act
        ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> response = 
                searchController.searchTasks("test", 0, 20, "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData().getResults()).hasSize(1);
    }

    @Test
    @DisplayName("Search my tasks - Success with query")
    void searchMyTasks_success_withQuery() {
        // Arrange
        when(taskSearchService.searchTasksByAssignee(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(sampleSearchResponse);

        // Act
        ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> response = 
                searchController.searchMyTasks("test", 0, 20, "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getResults()).hasSize(1);
    }

    @Test
    @DisplayName("Search my tasks - Success without query")
    void searchMyTasks_success_withoutQuery() {
        // Arrange
        when(taskSearchService.searchTasksByAssignee(anyString(), isNull(), anyInt(), anyInt()))
                .thenReturn(sampleSearchResponse);

        // Act
        ResponseEntity<ApiResponse<SearchResponse<TaskDocument>>> response = 
                searchController.searchMyTasks(null, 0, 20, "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Autocomplete - Success")
    void autocomplete_success() {
        // Arrange
        List<String> suggestions = Arrays.asList("Implement feature", "Implement API");
        when(taskSearchService.autocomplete(anyString()))
                .thenReturn(suggestions);

        // Act
        ResponseEntity<ApiResponse<List<String>>> response = 
                searchController.autocomplete("Impl", "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(2);
        assertThat(response.getBody().getData()).contains("Implement feature", "Implement API");
    }

    @Test
    @DisplayName("Health check - Success")
    void health_success() {
        // Act
        ResponseEntity<String> response = searchController.health();

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Search Service is running");
    }
}

