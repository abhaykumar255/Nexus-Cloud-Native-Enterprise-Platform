package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.nexus.search.document.TaskDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskSearchService Tests")
class TaskSearchServiceTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @InjectMocks
    private TaskSearchService taskSearchService;

    private TaskDocument sampleTask;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(taskSearchService, "indexName", "nexus-tasks");
        
        sampleTask = TaskDocument.builder()
                .id("task-123")
                .title("Implement feature")
                .description("Feature description")
                .status("OPEN")
                .priority("HIGH")
                .assigneeId("user-123")
                .build();
    }

    @Test
    @DisplayName("Index task - Success")
    void indexTask_success() throws IOException {
        // Arrange
        IndexResponse indexResponse = mock(IndexResponse.class);
        when(elasticsearchClient.index(any(IndexRequest.class))).thenReturn(indexResponse);

        // Act
        taskSearchService.indexTask(sampleTask);

        // Assert
        verify(elasticsearchClient).index(any(IndexRequest.class));
    }

    @Test
    @DisplayName("Index task - IOException throws RuntimeException")
    void indexTask_ioException_throwsRuntimeException() throws IOException {
        // Arrange
        when(elasticsearchClient.index(any(IndexRequest.class)))
                .thenThrow(new IOException("Connection error"));

        // Act & Assert
        assertThatThrownBy(() -> taskSearchService.indexTask(sampleTask))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to index task");
    }

    @Test
    @DisplayName("Search tasks - Success with results")
    void searchTasks_success_withResults() throws IOException {
        // Arrange
        Hit<TaskDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(sampleTask);

        TotalHits totalHits = mock(TotalHits.class);
        when(totalHits.value()).thenReturn(1L);

        HitsMetadata<TaskDocument> hitsMetadata = mock(HitsMetadata.class);
        when(hitsMetadata.hits()).thenReturn(Arrays.asList(hit));
        when(hitsMetadata.total()).thenReturn(totalHits);

        SearchResponse<TaskDocument> searchResponse = mock(SearchResponse.class);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(searchResponse.took()).thenReturn(150L);

        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenReturn(searchResponse);

        // Act
        com.nexus.search.dto.SearchResponse<TaskDocument> result = taskSearchService.searchTasks("test", 0, 20);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResults()).hasSize(1);
        assertThat(result.getResults().get(0).getId()).isEqualTo("task-123");
        assertThat(result.getTotalHits()).isEqualTo(1L);
        assertThat(result.getTook()).isEqualTo(150L);
    }

    @Test
    @DisplayName("Search tasks - IOException throws RuntimeException")
    void searchTasks_ioException_throwsRuntimeException() throws IOException {
        // Arrange
        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenThrow(new IOException("Search error"));

        // Act & Assert
        assertThatThrownBy(() -> taskSearchService.searchTasks("test", 0, 20))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to search tasks");
    }

    @Test
    @DisplayName("Search tasks by assignee - Success with query")
    void searchTasksByAssignee_success_withQuery() throws IOException {
        // Arrange
        Hit<TaskDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(sampleTask);

        TotalHits totalHits = mock(TotalHits.class);
        when(totalHits.value()).thenReturn(1L);

        HitsMetadata<TaskDocument> hitsMetadata = mock(HitsMetadata.class);
        when(hitsMetadata.hits()).thenReturn(Arrays.asList(hit));
        when(hitsMetadata.total()).thenReturn(totalHits);

        SearchResponse<TaskDocument> searchResponse = mock(SearchResponse.class);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(searchResponse.took()).thenReturn(100L);

        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenReturn(searchResponse);

        // Act
        com.nexus.search.dto.SearchResponse<TaskDocument> result =
                taskSearchService.searchTasksByAssignee("user-123", "feature", 0, 20);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResults()).hasSize(1);
    }

    @Test
    @DisplayName("Search tasks by assignee - Success without query")
    void searchTasksByAssignee_success_withoutQuery() throws IOException {
        // Arrange
        Hit<TaskDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(sampleTask);

        TotalHits totalHits = mock(TotalHits.class);
        when(totalHits.value()).thenReturn(1L);

        HitsMetadata<TaskDocument> hitsMetadata = mock(HitsMetadata.class);
        when(hitsMetadata.hits()).thenReturn(Arrays.asList(hit));
        when(hitsMetadata.total()).thenReturn(totalHits);

        SearchResponse<TaskDocument> searchResponse = mock(SearchResponse.class);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(searchResponse.took()).thenReturn(80L);

        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenReturn(searchResponse);

        // Act
        com.nexus.search.dto.SearchResponse<TaskDocument> result =
                taskSearchService.searchTasksByAssignee("user-123", null, 0, 20);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResults()).hasSize(1);
    }

    @Test
    @DisplayName("Search tasks by assignee - Success with blank query")
    void searchTasksByAssignee_success_withBlankQuery() throws IOException {
        // Arrange
        Hit<TaskDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(sampleTask);

        TotalHits totalHits = mock(TotalHits.class);
        when(totalHits.value()).thenReturn(1L);

        HitsMetadata<TaskDocument> hitsMetadata = mock(HitsMetadata.class);
        when(hitsMetadata.hits()).thenReturn(Arrays.asList(hit));
        when(hitsMetadata.total()).thenReturn(totalHits);

        SearchResponse<TaskDocument> searchResponse = mock(SearchResponse.class);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(searchResponse.took()).thenReturn(80L);

        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenReturn(searchResponse);

        // Act
        com.nexus.search.dto.SearchResponse<TaskDocument> result =
                taskSearchService.searchTasksByAssignee("user-123", "   ", 0, 20);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getResults()).hasSize(1);
    }

    @Test
    @DisplayName("Search tasks by assignee - IOException throws RuntimeException")
    void searchTasksByAssignee_ioException_throwsRuntimeException() throws IOException {
        // Arrange
        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenThrow(new IOException("Search error"));

        // Act & Assert
        assertThatThrownBy(() -> taskSearchService.searchTasksByAssignee("user-123", "test", 0, 20))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to search tasks");
    }

    @Test
    @DisplayName("Autocomplete - Success")
    void autocomplete_success() throws IOException {
        // Arrange
        TaskDocument task1 = TaskDocument.builder().title("Implement feature").build();
        TaskDocument task2 = TaskDocument.builder().title("Implement API").build();

        Hit<TaskDocument> hit1 = mock(Hit.class);
        Hit<TaskDocument> hit2 = mock(Hit.class);
        when(hit1.source()).thenReturn(task1);
        when(hit2.source()).thenReturn(task2);

        HitsMetadata<TaskDocument> hitsMetadata = mock(HitsMetadata.class);
        when(hitsMetadata.hits()).thenReturn(Arrays.asList(hit1, hit2));

        SearchResponse<TaskDocument> searchResponse = mock(SearchResponse.class);
        when(searchResponse.hits()).thenReturn(hitsMetadata);

        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenReturn(searchResponse);

        // Act
        List<String> result = taskSearchService.autocomplete("Impl");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).contains("Implement feature", "Implement API");
    }

    @Test
    @DisplayName("Autocomplete - IOException returns empty list")
    void autocomplete_ioException_returnsEmptyList() throws IOException {
        // Arrange
        when(elasticsearchClient.search(any(co.elastic.clients.elasticsearch.core.SearchRequest.class), eq(TaskDocument.class)))
                .thenThrow(new IOException("Search error"));

        // Act
        List<String> result = taskSearchService.autocomplete("test");

        // Assert
        assertThat(result).isEmpty();
    }
}

