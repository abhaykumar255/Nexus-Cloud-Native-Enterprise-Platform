package com.nexus.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.nexus.search.document.RestaurantDocument;
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
class RestaurantSearchServiceTest {

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @InjectMocks
    private RestaurantSearchService restaurantSearchService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(restaurantSearchService, "indexName", "nexus-restaurants");
    }

    @Test
    void testIndexRestaurant_Success() throws IOException {
        RestaurantDocument restaurant = RestaurantDocument.builder()
                .id("rest-1")
                .name("Test Restaurant")
                .cuisineType("Italian")
                .build();

        IndexResponse indexResponse = mock(IndexResponse.class);
        when(elasticsearchClient.index(any(IndexRequest.class))).thenReturn(indexResponse);

        assertDoesNotThrow(() -> restaurantSearchService.indexRestaurant(restaurant));
        verify(elasticsearchClient, times(1)).index(any(IndexRequest.class));
    }

    @Test
    void testIndexRestaurant_Failure() throws IOException {
        RestaurantDocument restaurant = RestaurantDocument.builder()
                .id("rest-1")
                .name("Test Restaurant")
                .build();

        when(elasticsearchClient.index(any(IndexRequest.class)))
                .thenThrow(new IOException("Connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> restaurantSearchService.indexRestaurant(restaurant));
        
        assertTrue(exception.getMessage().contains("Failed to index restaurant"));
    }

    @Test
    void testSearchRestaurants_Success() throws IOException {
        RestaurantDocument restaurant = RestaurantDocument.builder()
                .id("rest-1")
                .name("Pizza Palace")
                .cuisineType("Italian")
                .build();

        SearchResponse<RestaurantDocument> mockResponse = mockSearchResponse(Arrays.asList(restaurant), 1L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(RestaurantDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<RestaurantDocument> result = 
                restaurantSearchService.searchRestaurants("Pizza", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        assertEquals("rest-1", result.getResults().get(0).getId());
    }

    @Test
    void testSearchRestaurants_Failure() throws IOException {
        when(elasticsearchClient.search(any(SearchRequest.class), eq(RestaurantDocument.class)))
                .thenThrow(new IOException("Search failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> restaurantSearchService.searchRestaurants("test", 0, 10));

        assertTrue(exception.getMessage().contains("Failed to search restaurants"));
    }

    @Test
    void testSearchRestaurantsByCuisine_WithAllFilters() throws IOException {
        RestaurantDocument restaurant = RestaurantDocument.builder()
                .id("rest-1")
                .name("Italian Bistro")
                .cuisineType("Italian")
                .isOpen(true)
                .build();

        SearchResponse<RestaurantDocument> mockResponse = mockSearchResponse(Arrays.asList(restaurant), 1L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(RestaurantDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<RestaurantDocument> result = 
                restaurantSearchService.searchRestaurantsByCuisine("Italian", "Bistro", true, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
    }

    @Test
    void testSearchRestaurantsByCuisine_WithNullFilters() throws IOException {
        SearchResponse<RestaurantDocument> mockResponse = mockSearchResponse(Arrays.asList(), 0L);
        when(elasticsearchClient.search(any(SearchRequest.class), eq(RestaurantDocument.class)))
                .thenReturn(mockResponse);

        com.nexus.search.dto.SearchResponse<RestaurantDocument> result = 
                restaurantSearchService.searchRestaurantsByCuisine(null, null, null, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getResults().size());
    }

    @SuppressWarnings("unchecked")
    private SearchResponse<RestaurantDocument> mockSearchResponse(List<RestaurantDocument> restaurants, Long totalHits) {
        SearchResponse<RestaurantDocument> response = mock(SearchResponse.class);
        HitsMetadata<RestaurantDocument> hitsMetadata = mock(HitsMetadata.class);
        TotalHits total = mock(TotalHits.class);
        
        List<Hit<RestaurantDocument>> hits = restaurants.stream()
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
    private Hit<RestaurantDocument> createHit(RestaurantDocument restaurant) {
        Hit<RestaurantDocument> hit = mock(Hit.class);
        when(hit.source()).thenReturn(restaurant);
        return hit;
    }
}

