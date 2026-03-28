package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nexus.search.document.TaskDocument;
import com.nexus.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task Search Service using Elasticsearch
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Value("${elasticsearch.indices.tasks:nexus-tasks}")
    private String indexName;

    /**
     * Index a task document
     */
    public void indexTask(TaskDocument task) {
        try {
            IndexRequest<TaskDocument> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .id(task.getId())
                    .document(task)
            );

            elasticsearchClient.index(request);
            log.info("Indexed task: {}", task.getId());

        } catch (IOException e) {
            log.error("Failed to index task: {}", task.getId(), e);
            throw new RuntimeException("Failed to index task", e);
        }
    }

    /**
     * Full-text search tasks
     */
    public com.nexus.search.dto.SearchResponse<TaskDocument> searchTasks(String queryString, int from, int size) {
        try {
            Query query = Query.of(q -> q
                    .multiMatch(m -> m
                            .query(queryString)
                            .fields("title^3", "description^2", "tags", "category")
                            .fuzziness("AUTO")
                    )
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(query)
                    .from(from)
                    .size(size)
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<TaskDocument> response = elasticsearchClient.search(request, TaskDocument.class);

            List<TaskDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<TaskDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search tasks", e);
            throw new RuntimeException("Failed to search tasks", e);
        }
    }

    /**
     * Search tasks by assignee
     */
    public com.nexus.search.dto.SearchResponse<TaskDocument> searchTasksByAssignee(
            String assigneeId, String query, int from, int size) {
        try {
            List<Query> mustQueries = new ArrayList<>();

            // Filter by assignee
            mustQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("assigneeId")
                            .value(assigneeId)
                    )
            ));

            // Add text search if provided
            if (query != null && !query.isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields("title^3", "description^2", "tags")
                        )
                ));
            }

            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .must(mustQueries)
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(q -> q.bool(boolQuery))
                    .from(from)
                    .size(size)
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("createdAt")
                                    .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                            )
                    )
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<TaskDocument> response = elasticsearchClient.search(request, TaskDocument.class);

            List<TaskDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<TaskDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search tasks by assignee", e);
            throw new RuntimeException("Failed to search tasks", e);
        }
    }

    /**
     * Autocomplete for task titles
     */
    public List<String> autocomplete(String prefix) {
        try {
            Query query = Query.of(q -> q
                    .prefix(p -> p
                            .field("title")
                            .value(prefix)
                    )
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(query)
                    .size(10)
                    .source(source -> source
                            .filter(f -> f
                                    .includes("title")
                            )
                    )
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<TaskDocument> response = elasticsearchClient.search(request, TaskDocument.class);

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .map(TaskDocument::getTitle)
                    .distinct()
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Failed to autocomplete", e);
            return new ArrayList<>();
        }
    }
}

