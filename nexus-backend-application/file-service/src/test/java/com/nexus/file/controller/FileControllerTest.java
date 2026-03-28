package com.nexus.file.controller;

import com.nexus.file.domain.entity.FileMetadata;
import com.nexus.file.dto.FileMetadataDto;
import com.nexus.file.dto.FileUploadRequest;
import com.nexus.file.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("FileController Tests")
class FileControllerTest {

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private FileController fileController;

    private FileMetadataDto sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleDto = FileMetadataDto.builder()
            .id("file-123")
            .filename("test.pdf")
            .originalFilename("test.pdf")
            .contentType("application/pdf")
            .size(1024L)
            .extension("pdf")
            .uploadedBy("user-123")
            .storageType("LOCAL")
            .downloadUrl("/download/test.pdf")
            .status(FileMetadata.FileStatus.READY)
            .build();
    }

    @Test
    @DisplayName("Upload file - Success")
    void uploadFile_success() {
        // Arrange
        FileUploadRequest request = new FileUploadRequest();
        when(fileStorageService.uploadFile(any(MultipartFile.class), any(FileUploadRequest.class), anyString()))
            .thenReturn(sampleDto);

        // Act
        ResponseEntity<?> response = fileController.uploadFile(mockFile, request, "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        verify(fileStorageService).uploadFile(mockFile, request, "user-123");
    }

    @Test
    @DisplayName("Download file - Success")
    void downloadFile_success() {
        // Arrange
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileStorageService.getFileMetadata("file-123")).thenReturn(sampleDto);
        when(fileStorageService.downloadFile("file-123")).thenReturn(resource);

        // Act
        ResponseEntity<Resource> response = fileController.downloadFile("file-123", "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(resource);
        assertThat(response.getHeaders().getContentType().toString()).contains("application/pdf");
    }

    @Test
    @DisplayName("Get file metadata - Success")
    void getFileMetadata_success() {
        // Arrange
        when(fileStorageService.getFileMetadata("file-123")).thenReturn(sampleDto);

        // Act
        ResponseEntity<?> response = fileController.getFileMetadata("file-123", "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(fileStorageService).getFileMetadata("file-123");
    }

    @Test
    @DisplayName("Get my files - Success")
    void getMyFiles_success() {
        // Arrange
        List<FileMetadataDto> files = Arrays.asList(sampleDto);
        Page<FileMetadataDto> page = new PageImpl<>(files);
        when(fileStorageService.getUserFiles(eq("user-123"), any())).thenReturn(page);

        // Act
        ResponseEntity<?> response = fileController.getMyFiles("user-123", 0, 20);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(fileStorageService).getUserFiles(eq("user-123"), any());
    }

    @Test
    @DisplayName("Delete file - Success")
    void deleteFile_success() {
        // Arrange
        doNothing().when(fileStorageService).deleteFile("file-123", "user-123");

        // Act
        ResponseEntity<?> response = fileController.deleteFile("file-123", "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(fileStorageService).deleteFile("file-123", "user-123");
    }

    @Test
    @DisplayName("Health check - Success")
    void healthCheck_success() {
        // Act
        ResponseEntity<String> response = fileController.health();

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("File Service is running");
    }

    @Test
    @DisplayName("Upload file - Null metadata request")
    void uploadFile_nullMetadata() {
        // Arrange
        when(fileStorageService.uploadFile(any(MultipartFile.class), any(FileUploadRequest.class), anyString()))
            .thenReturn(sampleDto);

        // Act
        ResponseEntity<?> response = fileController.uploadFile(mockFile, null, "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        verify(fileStorageService).uploadFile(eq(mockFile), any(FileUploadRequest.class), eq("user-123"));
    }

    @Test
    @DisplayName("Get my files - Custom pagination")
    void getMyFiles_customPagination() {
        // Arrange
        List<FileMetadataDto> files = Arrays.asList(sampleDto);
        Page<FileMetadataDto> page = new PageImpl<>(files);
        when(fileStorageService.getUserFiles(eq("user-456"), any())).thenReturn(page);

        // Act
        ResponseEntity<?> response = fileController.getMyFiles("user-456", 1, 10);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(fileStorageService).getUserFiles(eq("user-456"), any());
    }

    @Test
    @DisplayName("Upload file - Verify logging and response body")
    void uploadFile_verifyDetails() {
        // Arrange
        FileUploadRequest request = new FileUploadRequest();
        when(fileStorageService.uploadFile(any(MultipartFile.class), any(FileUploadRequest.class), anyString()))
            .thenReturn(sampleDto);

        // Act
        ResponseEntity<?> response = fileController.uploadFile(mockFile, request, "user-789");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        verify(fileStorageService).uploadFile(mockFile, request, "user-789");
    }
}

