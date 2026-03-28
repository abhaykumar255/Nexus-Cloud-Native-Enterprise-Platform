package com.nexus.search.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SearchRequest Tests")
class SearchRequestTest {

    @Test
    @DisplayName("Builder creates instance with all fields")
    void builder_createsInstanceWithAllFields() {
        // Arrange
        List<String> fields = Arrays.asList("title", "description");
        Map<String, Object> filters = new HashMap<>();
        filters.put("status", "OPEN");

        // Act
        SearchRequest request = SearchRequest.builder()
                .query("test query")
                .fields(fields)
                .filters(filters)
                .sortBy("createdAt")
                .sortOrder("desc")
                .from(0)
                .size(20)
                .build();

        // Assert
        assertThat(request.getQuery()).isEqualTo("test query");
        assertThat(request.getFields()).containsExactly("title", "description");
        assertThat(request.getFilters()).containsEntry("status", "OPEN");
        assertThat(request.getSortBy()).isEqualTo("createdAt");
        assertThat(request.getSortOrder()).isEqualTo("desc");
        assertThat(request.getFrom()).isEqualTo(0);
        assertThat(request.getSize()).isEqualTo(20);
    }

    @Test
    @DisplayName("No-args constructor creates instance")
    void noArgsConstructor_createsInstance() {
        // Act
        SearchRequest request = new SearchRequest();

        // Assert
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("All-args constructor creates instance")
    void allArgsConstructor_createsInstance() {
        // Arrange
        List<String> fields = Arrays.asList("title");
        Map<String, Object> filters = new HashMap<>();

        // Act
        SearchRequest request = new SearchRequest("query", fields, filters, "title", "asc", 10, 50);

        // Assert
        assertThat(request.getQuery()).isEqualTo("query");
        assertThat(request.getFrom()).isEqualTo(10);
        assertThat(request.getSize()).isEqualTo(50);
    }

    @Test
    @DisplayName("Setters update fields")
    void setters_updateFields() {
        // Arrange
        SearchRequest request = new SearchRequest();

        // Act
        request.setQuery("new query");
        request.setFrom(5);
        request.setSize(15);

        // Assert
        assertThat(request.getQuery()).isEqualTo("new query");
        assertThat(request.getFrom()).isEqualTo(5);
        assertThat(request.getSize()).isEqualTo(15);
    }
}

