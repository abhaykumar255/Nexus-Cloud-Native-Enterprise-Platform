package com.nexus.file.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.PaginationInfo;
import com.nexus.file.dto.FileMetadataDto;
import com.nexus.file.dto.FileUploadRequest;
import com.nexus.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * File REST Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    /**
     * Upload a file
     * POST /api/v1/files/upload
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetadataDto>> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "metadata", required = false) FileUploadRequest request,
            @RequestHeader("X-User-Id") String userId) {

        log.info("File upload request from user: {}, filename: {}", userId, file.getOriginalFilename());

        if (request == null) {
            request = new FileUploadRequest();
        }

        FileMetadataDto metadata = fileStorageService.uploadFile(file, request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("File uploaded successfully", metadata));
    }

    /**
     * Download a file
     * GET /api/v1/files/download/{fileId}
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileId,
            @RequestHeader("X-User-Id") String userId) {

        log.info("File download request for file: {} by user: {}", fileId, userId);

        FileMetadataDto metadata = fileStorageService.getFileMetadata(fileId);
        Resource resource = fileStorageService.downloadFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + metadata.getOriginalFilename() + "\"")
                .body(resource);
    }

    /**
     * Get file metadata
     * GET /api/v1/files/{fileId}/metadata
     */
    @GetMapping("/{fileId}/metadata")
    public ResponseEntity<ApiResponse<FileMetadataDto>> getFileMetadata(
            @PathVariable String fileId,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Get file metadata request for file: {}", fileId);

        FileMetadataDto metadata = fileStorageService.getFileMetadata(fileId);

        return ResponseEntity.ok(ApiResponse.success(metadata));
    }

    /**
     * Get my files
     * GET /api/v1/files/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<FileMetadataDto>>> getMyFiles(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Get my files request from user: {}", userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FileMetadataDto> files = fileStorageService.getUserFiles(userId, pageable);

        return ResponseEntity.ok(ApiResponse.paginated(
                files.getContent(),
                PaginationInfo.from(files)));
    }

    /**
     * Delete file
     * DELETE /api/v1/files/{fileId}
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable String fileId,
            @RequestHeader("X-User-Id") String userId) {

        log.info("Delete file request for file: {} by user: {}", fileId, userId);

        fileStorageService.deleteFile(fileId, userId);

        return ResponseEntity.ok(ApiResponse.success("File deleted successfully", null));
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("File Service is running");
    }
}

