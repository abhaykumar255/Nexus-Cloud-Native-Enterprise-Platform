package com.nexus.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Search Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String query; // search query
    private List<String> fields; // fields to search in
    private Map<String, Object> filters; // additional filters
    private String sortBy;
    private String sortOrder; // asc, desc
    private Integer from;
    private Integer size;
}

