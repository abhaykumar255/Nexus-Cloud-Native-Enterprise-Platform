package com.nexus.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * File Upload Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequest {

    private String description;
    private String category;
    private String projectId;
    private String taskId;
    private Map<String, Object> metadata;
    private Integer expiresInDays; // for temporary files
}

