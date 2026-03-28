package com.nexus.file.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.file.config.FileServiceProperties;
import com.nexus.file.config.FileStorageProperties;
import com.nexus.file.domain.entity.FileMetadata;
import com.nexus.file.domain.repository.FileMetadataRepository;
import com.nexus.file.dto.FileMetadataDto;
import com.nexus.file.dto.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File Storage Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileMetadataRepository fileMetadataRepository;
    private final FileStorageProperties storageProperties;
    private final FileServiceProperties fileServiceProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Tika tika = new Tika();

    /**
     * Upload a file
     */
    public FileMetadataDto uploadFile(MultipartFile file, FileUploadRequest request, String userId) {
        try {
            // Validate file
            validateFile(file);

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + "." + extension;

            // Detect content type
            String contentType = tika.detect(file.getBytes());

            // Calculate checksums
            String md5 = calculateMD5(file.getBytes());
            String sha256 = calculateSHA256(file.getBytes());

            // Store file
            Path storagePath = storeFile(file, filename);

            // Create metadata
            FileMetadata metadata = FileMetadata.builder()
                    .filename(filename)
                    .originalFilename(originalFilename)
                    .contentType(contentType)
                    .size(file.getSize())
                    .extension(extension)
                    .uploadedBy(userId)
                    .storageType(fileServiceProperties.getStorage().getDefaultStorageType())
                    .storagePath(storagePath.toString())
                    .downloadUrl(fileServiceProperties.getUrl().getDownloadUrlPrefix() + filename)
                    .status(FileMetadata.FileStatus.READY)
                    .checksumMd5(md5)
                    .checksumSha256(sha256)
                    .metadata(request.getMetadata() != null ? request.getMetadata() : new HashMap<>())
                    .description(request.getDescription())
                    .category(request.getCategory())
                    .projectId(request.getProjectId())
                    .taskId(request.getTaskId())
                    .deleted(false)
                    .build();

            // Set expiration if temporary file
            if (request.getExpiresInDays() != null && request.getExpiresInDays() > 0) {
                metadata.setExpiresAt(LocalDateTime.now().plusDays(request.getExpiresInDays()));
            }

            FileMetadata savedMetadata = fileMetadataRepository.save(metadata);

            // Publish event
            publishFileUploadedEvent(savedMetadata);

            log.info("File uploaded successfully: {} by user: {}", filename, userId);

            return mapToDto(savedMetadata);

        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Download a file
     */
    public Resource downloadFile(String fileId) {
        FileMetadata metadata = fileMetadataRepository.findByIdAndDeletedFalse(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));

        try {
            Path filePath = Paths.get(metadata.getStoragePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found or not readable: " + fileId);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Get file metadata
     */
    public FileMetadataDto getFileMetadata(String fileId) {
        FileMetadata metadata = fileMetadataRepository.findByIdAndDeletedFalse(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));
        return mapToDto(metadata);
    }

    /**
     * Get user's files
     */
    public Page<FileMetadataDto> getUserFiles(String userId, Pageable pageable) {
        return fileMetadataRepository.findByUploadedByAndDeletedFalse(userId, pageable)
                .map(this::mapToDto);
    }

    /**
     * Delete file (soft delete)
     */
    public void deleteFile(String fileId, String userId) {
        FileMetadata metadata = fileMetadataRepository.findByIdAndDeletedFalse(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));

        metadata.setDeleted(true);
        metadata.setDeletedAt(LocalDateTime.now());
        metadata.setDeletedBy(userId);
        metadata.setStatus(FileMetadata.FileStatus.DELETED);

        fileMetadataRepository.save(metadata);

        log.info("File deleted: {} by user: {}", fileId, userId);
    }

    // Helper methods

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getFileExtension(filename);

        if (!storageProperties.getAllowedExtensions().contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("File type not allowed: " + extension);
        }

        long maxSizeBytes = storageProperties.getMaxSizeMb() * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException("File size exceeds maximum allowed: " + storageProperties.getMaxSizeMb() + "MB");
        }
    }

    private Path storeFile(MultipartFile file, String filename) throws IOException {
        Path uploadPath = Paths.get(storageProperties.getBasePath());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path targetLocation = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return targetLocation;
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot + 1);
    }

    private String calculateMD5(byte[] data) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            return String.format("%032x", new BigInteger(1, digest));
        } catch (Exception e) {
            return null;
        }
    }

    private String calculateSHA256(byte[] data) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            return String.format("%064x", new BigInteger(1, digest));
        } catch (Exception e) {
            return null;
        }
    }

    private void publishFileUploadedEvent(FileMetadata metadata) {
        Map<String, Object> event = new HashMap<>();
        event.put("fileId", metadata.getId());
        event.put("filename", metadata.getFilename());
        event.put("uploadedBy", metadata.getUploadedBy());
        event.put("size", metadata.getSize());
        event.put("contentType", metadata.getContentType());

        kafkaTemplate.send("file.uploaded", metadata.getId(), event);
    }

    private FileMetadataDto mapToDto(FileMetadata metadata) {
        return FileMetadataDto.builder()
                .id(metadata.getId())
                .filename(metadata.getFilename())
                .originalFilename(metadata.getOriginalFilename())
                .contentType(metadata.getContentType())
                .size(metadata.getSize())
                .extension(metadata.getExtension())
                .uploadedBy(metadata.getUploadedBy())
                .storageType(metadata.getStorageType())
                .downloadUrl(metadata.getDownloadUrl())
                .status(metadata.getStatus())
                .checksumMd5(metadata.getChecksumMd5())
                .checksumSha256(metadata.getChecksumSha256())
                .metadata(metadata.getMetadata())
                .description(metadata.getDescription())
                .category(metadata.getCategory())
                .projectId(metadata.getProjectId())
                .taskId(metadata.getTaskId())
                .createdAt(metadata.getCreatedAt())
                .updatedAt(metadata.getUpdatedAt())
                .expiresAt(metadata.getExpiresAt())
                .build();
    }
}

