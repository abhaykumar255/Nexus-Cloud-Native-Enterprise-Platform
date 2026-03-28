package com.nexus.file.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * File Metadata entity stored in MongoDB
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "file_metadata")
public class FileMetadata {

    @Id
    private String id;

    @Indexed
    private String filename;

    private String originalFilename;

    private String contentType;

    private Long size; // in bytes

    private String extension;

    @Indexed
    private String uploadedBy; // user ID

    private String storageType; // LOCAL, S3, SFTP

    private String storagePath; // file location

    private String downloadUrl;

    @Indexed
    private FileStatus status;

    private String checksumMd5;

    private String checksumSha256;

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    private String description;

    @Indexed
    private String category; // DOCUMENT, IMAGE, SPREADSHEET, etc.

    private String projectId;

    private String taskId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime expiresAt; // for temporary files

    @Indexed
    private Boolean deleted;

    private LocalDateTime deletedAt;

    private String deletedBy;

    public enum FileStatus {
        UPLOADING,
        UPLOADED,
        PROCESSING,
        READY,
        FAILED,
        ARCHIVED,
        DELETED
    }
}

