package com.nexus.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * News Item DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsItem implements Serializable {
    
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String source;
    private String author;
    private String publishedAt;
}

