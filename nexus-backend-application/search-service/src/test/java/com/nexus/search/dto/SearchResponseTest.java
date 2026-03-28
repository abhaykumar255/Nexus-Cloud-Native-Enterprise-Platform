package com.nexus.search.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SearchResponse Tests")
class SearchResponseTest {

    @Test
    @DisplayName("Builder creates instance with all fields")
    void builder_createsInstanceWithAllFields() {
        // Arrange
        List<String> results = Arrays.asList("result1", "result2", "result3");

        // Act
        SearchResponse<String> response = SearchResponse.<String>builder()
                .results(results)
                .totalHits(100L)
                .from(0)
                .size(20)
                .took(150L)
                .build();

        // Assert
        assertThat(response.getResults()).containsExactly("result1", "result2", "result3");
        assertThat(response.getTotalHits()).isEqualTo(100L);
        assertThat(response.getFrom()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(20);
        assertThat(response.getTook()).isEqualTo(150L);
    }

    @Test
    @DisplayName("No-args constructor creates instance")
    void noArgsConstructor_createsInstance() {
        // Act
        SearchResponse<String> response = new SearchResponse<>();

        // Assert
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("All-args constructor creates instance")
    void allArgsConstructor_createsInstance() {
        // Arrange
        List<Integer> results = Arrays.asList(1, 2, 3);

        // Act
        SearchResponse<Integer> response = new SearchResponse<>(results, 50L, 10, 5, 200L);

        // Assert
        assertThat(response.getResults()).containsExactly(1, 2, 3);
        assertThat(response.getTotalHits()).isEqualTo(50L);
        assertThat(response.getFrom()).isEqualTo(10);
        assertThat(response.getSize()).isEqualTo(5);
        assertThat(response.getTook()).isEqualTo(200L);
    }

    @Test
    @DisplayName("Setters update fields")
    void setters_updateFields() {
        // Arrange
        SearchResponse<String> response = new SearchResponse<>();
        List<String> newResults = Arrays.asList("new1", "new2");

        // Act
        response.setResults(newResults);
        response.setTotalHits(75L);
        response.setFrom(5);
        response.setSize(10);
        response.setTook(100L);

        // Assert
        assertThat(response.getResults()).containsExactly("new1", "new2");
        assertThat(response.getTotalHits()).isEqualTo(75L);
        assertThat(response.getFrom()).isEqualTo(5);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTook()).isEqualTo(100L);
    }
}

