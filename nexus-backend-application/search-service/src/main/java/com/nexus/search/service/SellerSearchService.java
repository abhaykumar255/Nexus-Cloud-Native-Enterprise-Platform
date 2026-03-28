package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nexus.search.document.SellerDocument;
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
 * Seller Search Service using Elasticsearch
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SellerSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Value("${elasticsearch.indices.sellers:nexus-sellers}")
    private String indexName;

    /**
     * Index a seller document
     */
    public void indexSeller(SellerDocument seller) {
        try {
            IndexRequest<SellerDocument> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .id(seller.getId())
                    .document(seller)
            );

            elasticsearchClient.index(request);
            log.info("Indexed seller: {}", seller.getId());

        } catch (IOException e) {
            log.error("Failed to index seller: {}", seller.getId(), e);
            throw new RuntimeException("Failed to index seller", e);
        }
    }

    /**
     * Full-text search sellers
     */
    public SearchResponse<SellerDocument> searchSellers(String queryString, int from, int size) {
        try {
            Query query = Query.of(q -> q
                    .multiMatch(m -> m
                            .query(queryString)
                            .fields("businessName^3", "description^2", "businessType")
                            .fuzziness("AUTO")
                    )
            );

            SearchRequest request = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(query)
                    .from(from)
                    .size(size)
            );

            co.elastic.clients.elasticsearch.core.SearchResponse<SellerDocument> response = 
                    elasticsearchClient.search(request, SellerDocument.class);

            List<SellerDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<SellerDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search sellers", e);
            throw new RuntimeException("Failed to search sellers", e);
        }
    }

    /**
     * Search verified sellers
     */
    public SearchResponse<SellerDocument> searchVerifiedSellers(
            String query, Boolean kycVerified, int from, int size) {
        try {
            List<Query> mustQueries = new ArrayList<>();

            // Filter by KYC verification
            if (kycVerified != null && kycVerified) {
                mustQueries.add(Query.of(q -> q
                        .term(t -> t.field("kycVerified").value(true))
                ));
            }

            // Filter by active status
            mustQueries.add(Query.of(q -> q
                    .term(t -> t.field("status.keyword").value("ACTIVE"))
            ));

            // Text search if provided
            if (query != null && !query.isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields("businessName^3", "description^2", "businessType")
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

            co.elastic.clients.elasticsearch.core.SearchResponse<SellerDocument> response = 
                    elasticsearchClient.search(request, SellerDocument.class);

            List<SellerDocument> results = response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            return SearchResponse.<SellerDocument>builder()
                    .results(results)
                    .totalHits(response.hits().total().value())
                    .from(from)
                    .size(size)
                    .took(response.took())
                    .build();

        } catch (IOException e) {
            log.error("Failed to search verified sellers", e);
            throw new RuntimeException("Failed to search sellers", e);
        }
    }
}

