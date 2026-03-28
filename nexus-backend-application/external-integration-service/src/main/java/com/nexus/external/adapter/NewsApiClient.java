package com.nexus.external.adapter;

import com.nexus.external.dto.NewsItem;

import java.util.List;

/**
 * News API Client Interface
 */
public interface NewsApiClient {
    
    /**
     * Get latest news by category
     */
    List<NewsItem> getLatestNews(String category, int limit);
}

