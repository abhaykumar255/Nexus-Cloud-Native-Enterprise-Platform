package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.nexus.search.document.ProductDocument;
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
class ProductSearchServiceTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @InjectMocks
    private ProductSearchService productSearchService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productSearchService, "indexName", "nexus-products");
    }

    @Test
    void testIndexProduct_Success() throws IOException {
        ProductDocument product = ProductDocument.builder()
                .id("prod-1")
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .build();

        IndexResponse indexResponse = mock(IndexResponse.class);
        when(elasticsearchClient.index(any(IndexRequest.class))).thenReturn(indexResponse);

        assertDoesNotThrow(() -> productSearchService.indexProduct(product));
        verify(elasticsearchClient, times(1)).index(any(IndexRequest.class));
    }

    @Test
    void testIndexProduct_Failure() throws IOException {
        ProductDocument product = ProductDocument.builder()
                .id("prod-1")
                .name("Test Product")
                .build();

        when(elasticsearchClient.index(any(IndexRequest.class)))
                .thenThrow(new IOException("Connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> productSearchService.indexProduct(product));
        
        assertTrue(exception.getMessage().contains("Failed to index product"));
        assertEquals(IOException.class, exception.getCause().getClass());
    }

    @Test
    void testSearchProducts_Success() throws IOException {
        ProductDocument product1 = ProductDocument.builder()
                .id("prod-1")
                .name("iPhone")
                .build();
        
        ProductDocument product2 = ProductDocument.builder()
                .id("prod-2")
                .name("iPad")
                .build();

        List<ProductDocument> products = Arrays.asList(product1, product2);

        SearchResponse<ProductDocument> mockResponse = mockSearchResponse(products, 2L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<ProductDocument> result = 
                productSearchService.searchProducts("iPhone", 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getResults().size());
        assertEquals(2L, result.getTotalHits());
        verify(elasticsearchClient, times(1)).search(any(SearchRequest.class), eq(ProductDocument.class));
    }

    @Test
    void testSearchProducts_Failure() throws IOException {
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductDocument.class)))
                .thenThrow(new IOException("Search failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productSearchService.searchProducts("test", 0, 10));

        assertTrue(exception.getMessage().contains("Failed to search products"));
    }

    @Test
    void testSearchProductsByCategory_WithAllFilters() throws IOException {
        ProductDocument product = ProductDocument.builder()
                .id("prod-1")
                .name("Laptop")
                .category("Electronics")
                .price(new BigDecimal("999.99"))
                .inStock(true)
                .build();

        SearchResponse<ProductDocument> mockResponse = mockSearchResponse(Arrays.asList(product), 1L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<ProductDocument> result = 
                productSearchService.searchProductsByCategory(
                        "Electronics", "Laptop", new BigDecimal("500"), 
                        new BigDecimal("1500"), true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        assertEquals("prod-1", result.getResults().get(0).getId());
    }

    @Test
    void testSearchProductsByCategory_WithNullFilters() throws IOException {
        SearchResponse<ProductDocument> mockResponse = mockSearchResponse(Arrays.asList(), 0L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<ProductDocument> result = 
                productSearchService.searchProductsByCategory(null, null, null, null, null, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getResults().size());
    }

    @SuppressWarnings("unchecked")
    private SearchResponse<ProductDocument> mockSearchResponse(List<ProductDocument> products, Long totalHits) {
        SearchResponse<ProductDocument> response = mock(SearchResponse.class);
        HitsMetadata<ProductDocument> hitsMetadata = mock(HitsMetadata.class);
        TotalHits total = mock(TotalHits.class);
        
        List<Hit<ProductDocument>> hits = products.stream()
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
    private Hit<ProductDocument> createHit(ProductDocument product) {
        Hit<ProductDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(product);
        return hit;
    }
}

