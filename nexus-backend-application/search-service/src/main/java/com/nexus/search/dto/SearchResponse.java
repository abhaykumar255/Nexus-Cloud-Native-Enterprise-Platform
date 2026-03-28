package com.nexus.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Search Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse<T> {

    private List<T> results;
    private long totalHits;
    private int from;
    private int size;
    private long took; // time in ms
}

