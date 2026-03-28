package com.nexus.file.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FileMetadata Entity Tests")
class FileMetadataTest {

    @Test
    @DisplayName("Builder creates entity with all fields")
    void builder_createsEntityWithAllFields() {
        // Arrange
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("author", "John Doe");

        LocalDateTime now = LocalDateTime.now();

        // Act
        FileMetadata fileMetadata = FileMetadata.builder()
            .id("file-123")
            .filename("test.pdf")
            .originalFilename("original.pdf")
            .contentType("application/pdf")
            .size(1024L)
            .extension("pdf")
            .uploadedBy("user-123")
            .storageType("LOCAL")
            .storagePath("/path/to/file")
            .downloadUrl("/download/test.pdf")
            .status(FileMetadata.FileStatus.READY)
            .checksumMd5("md5hash")
            .checksumSha256("sha256hash")
            .metadata(metadata)
            .description("Test file")
            .category("DOCUMENT")
            .projectId("project-123")
            .taskId("task-456")
            .createdAt(now)
            .deleted(false)
            .build();

        // Assert
        assertThat(fileMetadata.getId()).isEqualTo("file-123");
        assertThat(fileMetadata.getFilename()).isEqualTo("test.pdf");
        assertThat(fileMetadata.getOriginalFilename()).isEqualTo("original.pdf");
        assertThat(fileMetadata.getContentType()).isEqualTo("application/pdf");
        assertThat(fileMetadata.getSize()).isEqualTo(1024L);
        assertThat(fileMetadata.getExtension()).isEqualTo("pdf");
        assertThat(fileMetadata.getUploadedBy()).isEqualTo("user-123");
        assertThat(fileMetadata.getStorageType()).isEqualTo("LOCAL");
        assertThat(fileMetadata.getStatus()).isEqualTo(FileMetadata.FileStatus.READY);
        assertThat(fileMetadata.getMetadata()).containsKey("author");
        assertThat(fileMetadata.getDeleted()).isFalse();
    }

    @Test
    @DisplayName("FileStatus enum has all values")
    void fileStatus_hasAllValues() {
        // Assert
        assertThat(FileMetadata.FileStatus.values()).contains(
            FileMetadata.FileStatus.UPLOADING,
            FileMetadata.FileStatus.UPLOADED,
            FileMetadata.FileStatus.PROCESSING,
            FileMetadata.FileStatus.READY,
            FileMetadata.FileStatus.FAILED,
            FileMetadata.FileStatus.ARCHIVED,
            FileMetadata.FileStatus.DELETED
        );
    }

    @Test
    @DisplayName("No-args constructor creates empty entity")
    void noArgsConstructor_createsEmptyEntity() {
        // Act
        FileMetadata fileMetadata = new FileMetadata();

        // Assert
        assertThat(fileMetadata).isNotNull();
        assertThat(fileMetadata.getId()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        FileMetadata fileMetadata = new FileMetadata();

        // Act
        fileMetadata.setId("file-456");
        fileMetadata.setFilename("another.pdf");
        fileMetadata.setDeleted(true);
        fileMetadata.setStatus(FileMetadata.FileStatus.DELETED);

        // Assert
        assertThat(fileMetadata.getId()).isEqualTo("file-456");
        assertThat(fileMetadata.getFilename()).isEqualTo("another.pdf");
        assertThat(fileMetadata.getDeleted()).isTrue();
        assertThat(fileMetadata.getStatus()).isEqualTo(FileMetadata.FileStatus.DELETED);
    }

    @Test
    @DisplayName("All setters work")
    void allSetters_work() {
        // Arrange
        FileMetadata metadata = new FileMetadata();
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("key", "value");

        // Act
        metadata.setId("id-1");
        metadata.setFilename("file.txt");
        metadata.setOriginalFilename("original.txt");
        metadata.setContentType("text/plain");
        metadata.setSize(500L);
        metadata.setExtension("txt");
        metadata.setUploadedBy("user-1");
        metadata.setStorageType("S3");
        metadata.setStoragePath("/s3/path");
        metadata.setDownloadUrl("/download/file");
        metadata.setStatus(FileMetadata.FileStatus.READY);
        metadata.setChecksumMd5("md5");
        metadata.setChecksumSha256("sha256");
        metadata.setMetadata(metaMap);
        metadata.setDescription("Description");
        metadata.setCategory("CAT");
        metadata.setProjectId("proj-1");
        metadata.setTaskId("task-1");
        metadata.setExpiresAt(now);
        metadata.setDeleted(false);
        metadata.setDeletedAt(now);
        metadata.setDeletedBy("admin");

        // Assert
        assertThat(metadata.getId()).isEqualTo("id-1");
        assertThat(metadata.getFilename()).isEqualTo("file.txt");
        assertThat(metadata.getOriginalFilename()).isEqualTo("original.txt");
        assertThat(metadata.getContentType()).isEqualTo("text/plain");
        assertThat(metadata.getSize()).isEqualTo(500L);
        assertThat(metadata.getExtension()).isEqualTo("txt");
        assertThat(metadata.getUploadedBy()).isEqualTo("user-1");
        assertThat(metadata.getStorageType()).isEqualTo("S3");
        assertThat(metadata.getStoragePath()).isEqualTo("/s3/path");
        assertThat(metadata.getDownloadUrl()).isEqualTo("/download/file");
        assertThat(metadata.getStatus()).isEqualTo(FileMetadata.FileStatus.READY);
        assertThat(metadata.getChecksumMd5()).isEqualTo("md5");
        assertThat(metadata.getChecksumSha256()).isEqualTo("sha256");
        assertThat(metadata.getMetadata()).isEqualTo(metaMap);
        assertThat(metadata.getDescription()).isEqualTo("Description");
        assertThat(metadata.getCategory()).isEqualTo("CAT");
        assertThat(metadata.getProjectId()).isEqualTo("proj-1");
        assertThat(metadata.getTaskId()).isEqualTo("task-1");
        assertThat(metadata.getExpiresAt()).isEqualTo(now);
        assertThat(metadata.getDeleted()).isFalse();
        assertThat(metadata.getDeletedAt()).isEqualTo(now);
        assertThat(metadata.getDeletedBy()).isEqualTo("admin");
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        FileMetadata metadata = FileMetadata.builder()
            .id("id-123")
            .filename("test.pdf")
            .build();

        // Act
        String result = metadata.toString();

        // Assert
        assertThat(result).contains("id-123");
        assertThat(result).contains("test.pdf");
    }

    @Test
    @DisplayName("Equals and hashCode work")
    void equalsAndHashCode_work() {
        // Arrange
        FileMetadata meta1 = FileMetadata.builder()
            .id("same-id")
            .filename("file.txt")
            .build();

        FileMetadata meta2 = FileMetadata.builder()
            .id("same-id")
            .filename("file.txt")
            .build();

        FileMetadata meta3 = FileMetadata.builder()
            .id("different-id")
            .filename("file.txt")
            .build();

        // Assert
        assertThat(meta1).isEqualTo(meta2);
        assertThat(meta1.hashCode()).isEqualTo(meta2.hashCode());
        assertThat(meta1).isNotEqualTo(meta3);
    }
}

