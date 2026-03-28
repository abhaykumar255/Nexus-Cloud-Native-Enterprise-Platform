package com.nexus.external.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for NewsItem DTO
 */
class NewsItemTest {

    @Test
    @DisplayName("Should create NewsItem with builder")
    void builderCreatesNewsItem() {
        // Act
        NewsItem news = NewsItem.builder()
                .title("Breaking News")
                .description("Important update")
                .url("https://example.com/news")
                .imageUrl("https://example.com/image.jpg")
                .source("BBC")
                .author("John Doe")
                .publishedAt("2026-03-28")
                .build();

        // Assert
        assertThat(news.getTitle()).isEqualTo("Breaking News");
        assertThat(news.getSource()).isEqualTo("BBC");
        assertThat(news.getAuthor()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should create NewsItem with no-args constructor")
    void noArgsConstructorWorks() {
        // Act
        NewsItem news = new NewsItem();
        news.setTitle("Tech News");
        news.setSource("CNN");

        // Assert
        assertThat(news.getTitle()).isEqualTo("Tech News");
        assertThat(news.getSource()).isEqualTo("CNN");
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCodeWork() {
        // Arrange
        NewsItem news1 = NewsItem.builder()
                .title("Same News")
                .source("Reuters")
                .build();

        NewsItem news2 = NewsItem.builder()
                .title("Same News")
                .source("Reuters")
                .build();

        // Assert
        assertThat(news1).isEqualTo(news2);
        assertThat(news1.hashCode()).isEqualTo(news2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toStringWorks() {
        // Arrange
        NewsItem news = NewsItem.builder()
                .title("Test Article")
                .source("AP")
                .build();

        // Act
        String result = news.toString();

        // Assert
        assertThat(result).contains("Test Article", "AP");
    }
}

