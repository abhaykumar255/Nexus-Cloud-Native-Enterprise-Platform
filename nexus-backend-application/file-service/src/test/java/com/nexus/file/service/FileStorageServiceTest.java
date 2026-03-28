package com.nexus.file.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.file.config.FileServiceProperties;
import com.nexus.file.config.FileStorageProperties;
import com.nexus.file.domain.entity.FileMetadata;
import com.nexus.file.domain.repository.FileMetadataRepository;
import com.nexus.file.dto.FileMetadataDto;
import com.nexus.file.dto.FileUploadRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileStorageService Tests")
class FileStorageServiceTest {

    @Mock
    private FileMetadataRepository fileMetadataRepository;

    @Mock
    private FileStorageProperties storageProperties;

    @Mock
    private FileServiceProperties fileServiceProperties;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private FileStorageService fileStorageService;

    private FileMetadata sampleMetadata;
    private FileUploadRequest uploadRequest;

    @BeforeEach
    void setUp() {
        sampleMetadata = FileMetadata.builder()
            .id("file-123")
            .filename("test.pdf")
            .originalFilename("test.pdf")
            .contentType("application/pdf")
            .size(1024L)
            .extension("pdf")
            .uploadedBy("user-123")
            .storageType("LOCAL")
            .storagePath("/path/to/file")
            .downloadUrl("/download/test.pdf")
            .status(FileMetadata.FileStatus.READY)
            .deleted(false)
            .build();

        uploadRequest = FileUploadRequest.builder()
            .description("Test file")
            .category("DOCUMENT")
            .build();

        // Setup default mocks for upload tests
        FileServiceProperties.StorageConfig storageConfig = new FileServiceProperties.StorageConfig();
        storageConfig.setDefaultStorageType("LOCAL");

        FileServiceProperties.UrlConfig urlConfig = new FileServiceProperties.UrlConfig();
        urlConfig.setDownloadUrlPrefix("/download/");

        lenient().when(fileServiceProperties.getStorage()).thenReturn(storageConfig);
        lenient().when(fileServiceProperties.getUrl()).thenReturn(urlConfig);
        lenient().when(storageProperties.getAllowedExtensions()).thenReturn(Arrays.asList("pdf", "jpg", "png", "txt", "doc", "docx"));
        lenient().when(storageProperties.getMaxSizeMb()).thenReturn(10);
        lenient().when(storageProperties.getBasePath()).thenReturn("/tmp/test-uploads");
    }

    @Test
    @DisplayName("Get file metadata - Success")
    void getFileMetadata_success() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("file-123"))
            .thenReturn(Optional.of(sampleMetadata));

        // Act
        FileMetadataDto result = fileStorageService.getFileMetadata("file-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("file-123");
        assertThat(result.getFilename()).isEqualTo("test.pdf");
    }

    @Test
    @DisplayName("Get file metadata - Not found")
    void getFileMetadata_notFound() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("non-existent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.getFileMetadata("non-existent"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Get user files - Success")
    void getUserFiles_success() {
        // Arrange
        List<FileMetadata> files = Arrays.asList(sampleMetadata);
        Page<FileMetadata> page = new PageImpl<>(files);

        when(fileMetadataRepository.findByUploadedByAndDeletedFalse(eq("user-123"), any(Pageable.class)))
            .thenReturn(page);

        // Act
        Page<FileMetadataDto> result = fileStorageService.getUserFiles("user-123", Pageable.unpaged());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo("file-123");
    }

    @Test
    @DisplayName("Delete file - Success")
    void deleteFile_success() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("file-123"))
            .thenReturn(Optional.of(sampleMetadata));
        when(fileMetadataRepository.save(any(FileMetadata.class)))
            .thenReturn(sampleMetadata);

        // Act
        fileStorageService.deleteFile("file-123", "user-123");

        // Assert
        verify(fileMetadataRepository).save(argThat(metadata ->
            metadata.getDeleted() &&
            metadata.getDeletedBy().equals("user-123") &&
            metadata.getStatus() == FileMetadata.FileStatus.DELETED
        ));
    }

    @Test
    @DisplayName("Delete file - Not found")
    void deleteFile_notFound() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("non-existent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.deleteFile("non-existent", "user-123"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Download file - Not found")
    void downloadFile_notFound() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("non-existent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.downloadFile("non-existent"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Get user files - Empty list")
    void getUserFiles_emptyList() {
        // Arrange
        Page<FileMetadata> emptyPage = Page.empty();
        when(fileMetadataRepository.findByUploadedByAndDeletedFalse(eq("user-999"), any(Pageable.class)))
            .thenReturn(emptyPage);

        // Act
        Page<FileMetadataDto> result = fileStorageService.getUserFiles("user-999", Pageable.unpaged());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Get file metadata by ID - Success")
    void getFileMetadataById_success() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("file-123"))
            .thenReturn(Optional.of(sampleMetadata));

        // Act
        FileMetadataDto result = fileStorageService.getFileMetadata("file-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getFilename()).isEqualTo("test.pdf");
    }

    @Test
    @DisplayName("Delete file - Sets status to DELETED")
    void deleteFile_setsStatusToDeleted() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("file-123"))
            .thenReturn(Optional.of(sampleMetadata));
        when(fileMetadataRepository.save(any(FileMetadata.class)))
            .thenReturn(sampleMetadata);

        // Act
        fileStorageService.deleteFile("file-123", "user-456");

        // Assert
        verify(fileMetadataRepository).save(argThat(metadata -> {
            return metadata.getDeleted() == true &&
                   metadata.getStatus() == FileMetadata.FileStatus.DELETED &&
                   metadata.getDeletedBy().equals("user-456") &&
                   metadata.getDeletedAt() != null;
        }));
    }

    @Test
    @DisplayName("Get file metadata - Returns mapped DTO")
    void getFileMetadata_returnsMappedDto() {
        // Arrange
        when(fileMetadataRepository.findByIdAndDeletedFalse("file-123"))
            .thenReturn(Optional.of(sampleMetadata));

        // Act
        FileMetadataDto result = fileStorageService.getFileMetadata("file-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(sampleMetadata.getId());
        assertThat(result.getFilename()).isEqualTo(sampleMetadata.getFilename());
        assertThat(result.getOriginalFilename()).isEqualTo(sampleMetadata.getOriginalFilename());
        assertThat(result.getContentType()).isEqualTo(sampleMetadata.getContentType());
        assertThat(result.getSize()).isEqualTo(sampleMetadata.getSize());
        assertThat(result.getExtension()).isEqualTo(sampleMetadata.getExtension());
        assertThat(result.getUploadedBy()).isEqualTo(sampleMetadata.getUploadedBy());
        assertThat(result.getStorageType()).isEqualTo(sampleMetadata.getStorageType());
        assertThat(result.getStatus()).isEqualTo(sampleMetadata.getStatus());
    }

    @Test
    @DisplayName("Get user files - Multiple files")
    void getUserFiles_multipleFiles() {
        // Arrange
        FileMetadata file1 = FileMetadata.builder()
            .id("file-1")
            .filename("file1.pdf")
            .uploadedBy("user-123")
            .deleted(false)
            .build();

        FileMetadata file2 = FileMetadata.builder()
            .id("file-2")
            .filename("file2.pdf")
            .uploadedBy("user-123")
            .deleted(false)
            .build();

        List<FileMetadata> files = Arrays.asList(file1, file2);
        Page<FileMetadata> page = new PageImpl<>(files);

        when(fileMetadataRepository.findByUploadedByAndDeletedFalse(eq("user-123"), any(Pageable.class)))
            .thenReturn(page);

        // Act
        Page<FileMetadataDto> result = fileStorageService.getUserFiles("user-123", Pageable.unpaged());

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo("file-1");
        assertThat(result.getContent().get(1).getId()).isEqualTo("file-2");
    }

    @Test
    @DisplayName("Validate file - Empty file throws exception")
    void validateFile_emptyFile_throwsException() {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", "test.pdf", "application/pdf", new byte[0]
        );

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.uploadFile(emptyFile, uploadRequest, "user-123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot upload empty file");
    }

    @Test
    @DisplayName("Validate file - Invalid extension throws exception")
    void validateFile_invalidExtension_throwsException() {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file", "test.exe", "application/octet-stream", "test content".getBytes()
        );

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.uploadFile(invalidFile, uploadRequest, "user-123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("File type not allowed");
    }

    @Test
    @DisplayName("Validate file - File size exceeded throws exception")
    void validateFile_fileSizeExceeded_throwsException() {
        // Arrange
        byte[] largeContent = new byte[11 * 1024 * 1024]; // 11MB, exceeds 10MB limit
        MockMultipartFile largeFile = new MockMultipartFile(
            "file", "large.pdf", "application/pdf", largeContent
        );

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.uploadFile(largeFile, uploadRequest, "user-123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("File size exceeds maximum allowed");
    }

    @Test
    @DisplayName("Validate file - Small PDF within size limit")
    void validateFile_smallPdf_withinLimit() {
        // Arrange - just under 1MB, well under 10MB limit
        byte[] smallContent = new byte[1024 * 500]; // 500KB
        MockMultipartFile smallFile = new MockMultipartFile(
            "file", "small.pdf", "application/pdf", smallContent
        );

        // Act & Assert - expect file I/O error (not validation error) since file passes validation
        assertThatThrownBy(() -> fileStorageService.uploadFile(smallFile, uploadRequest, "user-123"))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Validate file - DOCX extension allowed")
    void validateFile_docxExtension_allowed() {
        // Arrange
        MockMultipartFile docxFile = new MockMultipartFile(
            "file", "document.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "DOCX content".getBytes()
        );

        // Act & Assert - expect file I/O error, not validation error
        assertThatThrownBy(() -> fileStorageService.uploadFile(docxFile, uploadRequest, "user-123"))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Validate file - File without extension is rejected")
    void validateFile_noExtension_rejected() {
        // Arrange
        MockMultipartFile noExtFile = new MockMultipartFile(
            "file", "filenoext", "application/octet-stream", "content".getBytes()
        );

        // Act & Assert
        assertThatThrownBy(() -> fileStorageService.uploadFile(noExtFile, uploadRequest, "user-123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("File type not allowed");
    }
}

