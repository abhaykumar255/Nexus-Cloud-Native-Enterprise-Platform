package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nexus.search.document.ProductDocument;
import com.nexus.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Search Service using Elasticsearch
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Value("${elasticsearch.indices.products:nexus-products}")
    private String indexName;

    /**
     * Index a product document
     */
    public void indexProduct(ProductDocument product) {
        try {
            IndexRequest<ProductDocument> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .id(product.getId())
                    .document(product)
            );

            elasticsearchClient.index(request);
            log.info("Indexed product: {}", product.getId());

        } catch (IOException e) {
            log.error("Failed to index product: {}", product.getId(), e);
            throw new RuntimeException("Failed to index product", e);
        }
    }

    /**
     * Full-text search products
     */
    public SearchResponse<ProductDocument> searchProducts(String queryString, int from, int size) {
        try {
            Query query = Query.of(q -> q
                    .multiMatch(m -> m
                            .query(queryString)
                            .fields("name^3", "description^2", "brand^2", "category", "tags")
                            .fuzziness("AUTO")
                    )
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(query)
                    .from(from)
                    .size(size)
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<ProductDocument> response = 
                    elasticsearchClient.search(request, ProductDocument.class);

            List<ProductDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<ProductDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search products", e);
            throw new RuntimeException("Failed to search products", e);
        }
    }

    /**
     * Search products by category with filters
     */
    public SearchResponse<ProductDocument> searchProductsByCategory(
            String category, String query, BigDecimal minPrice, BigDecimal maxPrice, 
            Boolean inStock, int from, int size) {
        try {
            List<Query> mustQueries = new ArrayList<>();

            // Filter by category
            if (category != null && !category.isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .term(t -> t.field("category.keyword").value(category))
                ));
            }

            // Filter by stock availability
            if (inStock != null && inStock) {
                mustQueries.add(Query.of(q -> q
                        .term(t -> t.field("inStock").value(true))
                ));
            }

            // Text search if provided
            if (query != null && !query.isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields("name^3", "description^2", "brand^2", "tags")
                        )
                ));
            }

            // Price range filter
            List<Query> filterQueries = new ArrayList<>();
            if (minPrice != null || maxPrice != null) {
                filterQueries.add(Query.of(q -> q
                        .range(r -> {
                            var rangeQuery = r.field("price");
                            if (minPrice != null) rangeQuery.gte(co.elastic.clients.json.JsonData.of(minPrice));
                            if (maxPrice != null) rangeQuery.lte(co.elastic.clients.json.JsonData.of(maxPrice));
                            return rangeQuery;
                        })
                ));
            }

            BoolQuery boolQuery = BoolQuery.of(b -> {
                var builder = b.must(mustQueries);
                if (!filterQueries.isEmpty()) {
                    builder.filter(filterQueries);
                }
                return builder;
            });

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(q -> q.bool(boolQuery))
                    .from(from)
                    .size(size)
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<ProductDocument> response = 
                    elasticsearchClient.search(request, ProductDocument.class);

            List<ProductDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<ProductDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search products by category", e);
            throw new RuntimeException("Failed to search products", e);
        }
    }
}

