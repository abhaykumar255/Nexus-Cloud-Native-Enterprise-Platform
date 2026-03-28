package com.nexus.external.adapter;

import com.nexus.external.dto.NewsItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for NewsApiAdapter
 */
@ExtendWith(MockitoExtension.class)
class NewsApiAdapterTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private NewsApiAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new NewsApiAdapter(webClientBuilder);
        ReflectionTestUtils.setField(adapter, "baseUrl", "https://newsapi.org/v2");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
    }

    @Test
    @DisplayName("Should get latest news successfully")
    void getLatestNews_success() {
        // Arrange
        Map<String, Object> article1 = new HashMap<>();
        article1.put("title", "Breaking News");
        article1.put("description", "Important update");
        article1.put("url", "https://example.com/news1");
        article1.put("urlToImage", "https://example.com/image1.jpg");
        article1.put("author", "John Doe");
        article1.put("publishedAt", "2026-03-28");
        
        Map<String, Object> source1 = new HashMap<>();
        source1.put("name", "BBC");
        article1.put("source", source1);

        Map<String, Object> article2 = new HashMap<>();
        article2.put("title", "Tech News");
        article2.put("description", "Technology update");
        article2.put("url", "https://example.com/news2");
        article2.put("urlToImage", "https://example.com/image2.jpg");
        article2.put("author", "Jane Smith");
        article2.put("publishedAt", "2026-03-27");
        
        Map<String, Object> source2 = new HashMap<>();
        source2.put("name", "CNN");
        article2.put("source", source2);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("articles", List.of(article1, article2));

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyInt(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        // Act
        List<NewsItem> result = adapter.getLatestNews("technology", 10);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Breaking News");
        assertThat(result.get(0).getSource()).isEqualTo("BBC");
        assertThat(result.get(1).getTitle()).isEqualTo("Tech News");
        assertThat(result.get(1).getSource()).isEqualTo("CNN");
    }

    @Test
    @DisplayName("Should return empty list when API returns null")
    void getLatestNews_nullResponse() {
        // Arrange
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyInt(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

        // Act
        List<NewsItem> result = adapter.getLatestNews("technology", 10);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty list when no articles in response")
    void getLatestNews_noArticles() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyInt(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        // Act
        List<NewsItem> result = adapter.getLatestNews("technology", 10);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception on API error")
    void getLatestNews_apiError() {
        // Arrange
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString(), anyInt(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new RuntimeException("API error"));

        // Act & Assert
        assertThatThrownBy(() -> adapter.getLatestNews("technology", 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("News API call failed");
    }
}

