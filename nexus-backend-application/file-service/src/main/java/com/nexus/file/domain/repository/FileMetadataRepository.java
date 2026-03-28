package com.nexus.file.domain.repository;

import com.nexus.file.domain.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for FileMetadata
 */
@Repository
public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {

    Page<FileMetadata> findByUploadedByAndDeletedFalse(String uploadedBy, Pageable pageable);

    Page<FileMetadata> findByProjectIdAndDeletedFalse(String projectId, Pageable pageable);

    Page<FileMetadata> findByTaskIdAndDeletedFalse(String taskId, Pageable pageable);

    Page<FileMetadata> findByCategoryAndDeletedFalse(String category, Pageable pageable);

    List<FileMetadata> findByExpiresAtBeforeAndDeletedFalse(LocalDateTime dateTime);

    Optional<FileMetadata> findByIdAndDeletedFalse(String id);

    long countByUploadedByAndDeletedFalse(String uploadedBy);

    long sumSizeByUploadedByAndDeletedFalse(String uploadedBy);
}

