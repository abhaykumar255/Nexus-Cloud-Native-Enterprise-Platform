package com.nexus.file.dto;

import com.nexus.file.domain.entity.FileMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FileMetadataDto Tests")
class FileMetadataDtoTest {

    @Test
    @DisplayName("Builder creates DTO with all fields")
    void builder_createsDtoWithAllFields() {
        // Arrange & Act
        FileMetadataDto dto = FileMetadataDto.builder()
            .id("file-123")
            .filename("test.pdf")
            .originalFilename("original.pdf")
            .contentType("application/pdf")
            .size(1024L)
            .extension("pdf")
            .uploadedBy("user-123")
            .storageType("LOCAL")
            .downloadUrl("/download/test.pdf")
            .status(FileMetadata.FileStatus.READY)
            .description("Test file")
            .category("DOCUMENT")
            .createdAt(LocalDateTime.now())
            .build();

        // Assert
        assertThat(dto.getId()).isEqualTo("file-123");
        assertThat(dto.getFilename()).isEqualTo("test.pdf");
        assertThat(dto.getOriginalFilename()).isEqualTo("original.pdf");
        assertThat(dto.getContentType()).isEqualTo("application/pdf");
        assertThat(dto.getSize()).isEqualTo(1024L);
        assertThat(dto.getExtension()).isEqualTo("pdf");
        assertThat(dto.getUploadedBy()).isEqualTo("user-123");
        assertThat(dto.getStorageType()).isEqualTo("LOCAL");
        assertThat(dto.getStatus()).isEqualTo(FileMetadata.FileStatus.READY);
    }

    @Test
    @DisplayName("No-args constructor creates empty DTO")
    void noArgsConstructor_createsEmptyDto() {
        // Act
        FileMetadataDto dto = new FileMetadataDto();

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isNull();
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        // Arrange
        FileMetadataDto dto = new FileMetadataDto();

        // Act
        dto.setId("file-456");
        dto.setFilename("another.pdf");
        dto.setSize(2048L);

        // Assert
        assertThat(dto.getId()).isEqualTo("file-456");
        assertThat(dto.getFilename()).isEqualTo("another.pdf");
        assertThat(dto.getSize()).isEqualTo(2048L);
    }

    @Test
    @DisplayName("All setters and getters work")
    void allSettersAndGetters_work() {
        // Arrange
        FileMetadataDto dto = new FileMetadataDto();
        LocalDateTime now = LocalDateTime.now();

        // Act
        dto.setId("id-1");
        dto.setFilename("file.txt");
        dto.setOriginalFilename("orig.txt");
        dto.setContentType("text/plain");
        dto.setSize(100L);
        dto.setExtension("txt");
        dto.setUploadedBy("user-1");
        dto.setStorageType("S3");
        dto.setDownloadUrl("/download");
        dto.setStatus(FileMetadata.FileStatus.PROCESSING);
        dto.setChecksumMd5("md5");
        dto.setChecksumSha256("sha256");
        dto.setDescription("desc");
        dto.setCategory("cat");
        dto.setProjectId("proj-1");
        dto.setTaskId("task-1");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        dto.setExpiresAt(now);

        // Assert - use all getters
        assertThat(dto.getId()).isEqualTo("id-1");
        assertThat(dto.getFilename()).isEqualTo("file.txt");
        assertThat(dto.getOriginalFilename()).isEqualTo("orig.txt");
        assertThat(dto.getContentType()).isEqualTo("text/plain");
        assertThat(dto.getSize()).isEqualTo(100L);
        assertThat(dto.getExtension()).isEqualTo("txt");
        assertThat(dto.getUploadedBy()).isEqualTo("user-1");
        assertThat(dto.getStorageType()).isEqualTo("S3");
        assertThat(dto.getDownloadUrl()).isEqualTo("/download");
        assertThat(dto.getStatus()).isEqualTo(FileMetadata.FileStatus.PROCESSING);
        assertThat(dto.getChecksumMd5()).isEqualTo("md5");
        assertThat(dto.getChecksumSha256()).isEqualTo("sha256");
        assertThat(dto.getDescription()).isEqualTo("desc");
        assertThat(dto.getCategory()).isEqualTo("cat");
        assertThat(dto.getProjectId()).isEqualTo("proj-1");
        assertThat(dto.getTaskId()).isEqualTo("task-1");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
        assertThat(dto.getExpiresAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        // Arrange
        FileMetadataDto dto = FileMetadataDto.builder()
            .id("id-123")
            .filename("test.pdf")
            .build();

        // Act
        String result = dto.toString();

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Equals and hashCode work")
    void equalsAndHashCode_work() {
        // Arrange
        FileMetadataDto dto1 = FileMetadataDto.builder()
            .id("same-id")
            .filename("file.txt")
            .build();

        FileMetadataDto dto2 = FileMetadataDto.builder()
            .id("same-id")
            .filename("file.txt")
            .build();

        FileMetadataDto dto3 = FileMetadataDto.builder()
            .id("different-id")
            .build();

        // Assert
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }
}

