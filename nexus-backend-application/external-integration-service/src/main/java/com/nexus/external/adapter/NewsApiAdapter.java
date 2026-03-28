package com.nexus.external.adapter;

import com.nexus.external.dto.NewsItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * News API Adapter Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsApiAdapter implements NewsApiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${external.api.news.base-url}")
    private String baseUrl;

    @Value("${external.api.news.api-key}")
    private String apiKey;

    @Override
    public List<NewsItem> getLatestNews(String category, int limit) {
        try {
            log.info("Fetching news: category={}, limit={}", category, limit);

            Map<String, Object> response = webClientBuilder.build()
                    .get()
                    .uri(baseUrl + "/top-headlines?category={category}&pageSize={limit}&apiKey={apiKey}", 
                         category, limit, apiKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("articles")) {
                return new ArrayList<>();
            }

            List<Map<String, Object>> articles = (List<Map<String, Object>>) response.get("articles");
            List<NewsItem> newsItems = new ArrayList<>();

            for (Map<String, Object> article : articles) {
                Map<String, Object> source = (Map<String, Object>) article.get("source");
                
                NewsItem item = NewsItem.builder()
                        .title((String) article.get("title"))
                        .description((String) article.get("description"))
                        .url((String) article.get("url"))
                        .imageUrl((String) article.get("urlToImage"))
                        .source((String) source.get("name"))
                        .author((String) article.get("author"))
                        .publishedAt((String) article.get("publishedAt"))
                        .build();
                
                newsItems.add(item);
            }

            return newsItems;

        } catch (Exception e) {
            log.error("Failed to fetch news", e);
            throw new RuntimeException("News API call failed", e);
        }
    }
}

