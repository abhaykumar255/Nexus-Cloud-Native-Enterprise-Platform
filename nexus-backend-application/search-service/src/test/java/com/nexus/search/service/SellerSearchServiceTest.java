package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.nexus.search.document.SellerDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerSearchServiceTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @InjectMocks
    private SellerSearchService sellerSearchService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sellerSearchService, "indexName", "nexus-sellers");
    }

    @Test
    void testIndexSeller_Success() throws IOException {
        SellerDocument seller = SellerDocument.builder()
                .id("seller-1")
                .businessName("Tech Store")
                .businessType("RETAIL")
                .build();

        IndexResponse indexResponse = mock(IndexResponse.class);
        when(elasticsearchClient.index(any(IndexRequest.class))).thenReturn(indexResponse);

        assertDoesNotThrow(() -> sellerSearchService.indexSeller(seller));
        verify(elasticsearchClient, times(1)).index(any(IndexRequest.class));
    }

    @Test
    void testIndexSeller_Failure() throws IOException {
        SellerDocument seller = SellerDocument.builder()
                .id("seller-1")
                .businessName("Tech Store")
                .build();

        when(elasticsearchClient.index(any(IndexRequest.class)))
                .thenThrow(new IOException("Connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> sellerSearchService.indexSeller(seller));
        
        assertTrue(exception.getMessage().contains("Failed to index seller"));
    }

    @Test
    void testSearchSellers_Success() throws IOException {
        SellerDocument seller = SellerDocument.builder()
                .id("seller-1")
                .businessName("Electronics Shop")
                .businessType("RETAIL")
                .build();

        SearchResponse<SellerDocument> mockResponse = mockSearchResponse(Arrays.asList(seller), 1L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(SellerDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<SellerDocument> result = 
                sellerSearchService.searchSellers("Electronics", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        assertEquals("seller-1", result.getResults().get(0).getId());
    }

    @Test
    void testSearchSellers_Failure() throws IOException {
        when(elasticsearchClient.search(any(SearchRequest.class), eq(SellerDocument.class)))
                .thenThrow(new IOException("Search failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sellerSearchService.searchSellers("test", 0, 10));

        assertTrue(exception.getMessage().contains("Failed to search sellers"));
    }

    @Test
    void testSearchVerifiedSellers_WithKycVerified() throws IOException {
        SellerDocument seller = SellerDocument.builder()
                .id("seller-1")
                .businessName("Verified Store")
                .kycVerified(true)
                .status("ACTIVE")
                .rating(new BigDecimal("4.8"))
                .build();

        SearchResponse<SellerDocument> mockResponse = mockSearchResponse(Arrays.asList(seller), 1L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(SellerDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<SellerDocument> result = 
                sellerSearchService.searchVerifiedSellers("Store", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
    }

    @Test
    void testSearchVerifiedSellers_WithNullFilters() throws IOException {
        SearchResponse<SellerDocument> mockResponse = mockSearchResponse(Arrays.asList(), 0L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(SellerDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<SellerDocument> result = 
                sellerSearchService.searchVerifiedSellers(null, null, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getResults().size());
    }

    @SuppressWarnings("unchecked")
    private SearchResponse<SellerDocument> mockSearchResponse(List<SellerDocument> sellers, Long totalHits) {
        SearchResponse<SellerDocument> response = mock(SearchResponse.class);
        HitsMetadata<SellerDocument> hitsMetadata = mock(HitsMetadata.class);
        TotalHits total = mock(TotalHits.class);
        
        List<Hit<SellerDocument>> hits = sellers.stream()
                .map(this::createHit)
                .toList();
        
        when(response.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(hits);
        when(hitsMetadata.total()).thenReturn(total);
        when(total.value()).thenReturn(totalHits);
        when(response.took()).thenReturn(50L);
        
        return response;
    }

    @SuppressWarnings("unchecked")
    private Hit<SellerDocument> createHit(SellerDocument seller) {
        Hit<SellerDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(seller);
        return hit;
    }
}

