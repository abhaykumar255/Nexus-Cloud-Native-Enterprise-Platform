package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nexus.search.document.RestaurantDocument;
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
 * Restaurant Search Service using Elasticsearch
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Value("${elasticsearch.indices.restaurants:nexus-restaurants}")
    private String indexName;

    /**
     * Index a restaurant document
     */
    public void indexRestaurant(RestaurantDocument restaurant) {
        try {
            IndexRequest<RestaurantDocument> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .id(restaurant.getId())
                    .document(restaurant)
            );

            elasticsearchClient.index(request);
            log.info("Indexed restaurant: {}", restaurant.getId());

        } catch (IOException e) {
            log.error("Failed to index restaurant: {}", restaurant.getId(), e);
            throw new RuntimeException("Failed to index restaurant", e);
        }
    }

    /**
     * Full-text search restaurants
     */
    public SearchResponse<RestaurantDocument> searchRestaurants(String queryString, int from, int size) {
        try {
            Query query = Query.of(q -> q
                    .multiMatch(m -> m
                            .query(queryString)
                            .fields("name^3", "description^2", "cuisineType^2", "popularDishes")
                            .fuzziness("AUTO")
                    )
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(query)
                    .from(from)
                    .size(size)
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<RestaurantDocument> response = 
                    elasticsearchClient.search(request, RestaurantDocument.class);

            List<RestaurantDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<RestaurantDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search restaurants", e);
            throw new RuntimeException("Failed to search restaurants", e);
        }
    }

    /**
     * Search restaurants by cuisine and location
     */
    public SearchResponse<RestaurantDocument> searchRestaurantsByCuisine(
            String cuisine, String query, Boolean isOpen, int from, int size) {
        try {
            List<Query> mustQueries = new ArrayList<>();

            // Filter by cuisine type
            if (cuisine != null && !cuisine.isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .term(t -> t.field("cuisineType.keyword").value(cuisine))
                ));
            }

            // Filter by open status
            if (isOpen != null && isOpen) {
                mustQueries.add(Query.of(q -> q
                        .term(t -> t.field("isOpen").value(true))
                ));
            }

            // Text search if provided
            if (query != null && !query.isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields("name^3", "description^2", "popularDishes")
                        )
                ));
            }

            BoolQuery boolQuery = BoolQuery.of(b -> b.must(mustQueries));

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(q -> q.bool(boolQuery))
                    .from(from)
                    .size(size)
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("rating")
                                    .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                            )
                    )
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<RestaurantDocument> response = 
                    elasticsearchClient.search(request, RestaurantDocument.class);

            List<RestaurantDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<RestaurantDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search restaurants by cuisine", e);
            throw new RuntimeException("Failed to search restaurants", e);
        }
    }
}

