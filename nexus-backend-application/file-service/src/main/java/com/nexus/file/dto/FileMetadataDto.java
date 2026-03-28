package com.nexus.file.dto;

import com.nexus.file.domain.entity.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * File Metadata DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataDto {

    private String id;
    private String filename;
    private String originalFilename;
    private String contentType;
    private Long size;
    private String extension;
    private String uploadedBy;
    private String storageType;
    private String downloadUrl;
    private FileMetadata.FileStatus status;
    private String checksumMd5;
    private String checksumSha256;
    private Map<String, Object> metadata;
    private String description;
    private String category;
    private String projectId;
    private String taskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
}

