package com.nexus.file.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FileStorageProperties Tests")
class FileStoragePropertiesTest {

    @Test
    @DisplayName("Default values are set correctly")
    void defaultValues_areSetCorrectly() {
        // Arrange & Act
        FileStorageProperties properties = new FileStorageProperties();

        // Assert
        assertThat(properties.getStorage()).isNotNull();
        assertThat(properties.getUpload()).isNotNull();
        assertThat(properties.getSftp()).isNotNull();
    }

    @Test
    @DisplayName("Storage configuration works")
    void storageConfiguration_works() {
        // Arrange
        FileStorageProperties properties = new FileStorageProperties();
        FileStorageProperties.Storage storage = new FileStorageProperties.Storage();
        
        // Act
        storage.setType("s3");
        storage.setBasePath("/custom/path");
        properties.setStorage(storage);

        // Assert
        assertThat(properties.getStorage().getType()).isEqualTo("s3");
        assertThat(properties.getStorage().getBasePath()).isEqualTo("/custom/path");
    }

    @Test
    @DisplayName("Upload configuration works")
    void uploadConfiguration_works() {
        // Arrange
        FileStorageProperties properties = new FileStorageProperties();
        FileStorageProperties.Upload upload = new FileStorageProperties.Upload();
        
        // Act
        upload.setAllowedExtensions(Arrays.asList("pdf", "jpg"));
        upload.setMaxSizeMb(50);
        properties.setUpload(upload);

        // Assert
        assertThat(properties.getUpload().getAllowedExtensions()).contains("pdf", "jpg");
        assertThat(properties.getUpload().getMaxSizeMb()).isEqualTo(50);
    }

    @Test
    @DisplayName("SFTP configuration works")
    void sftpConfiguration_works() {
        // Arrange
        FileStorageProperties properties = new FileStorageProperties();
        FileStorageProperties.Sftp sftp = new FileStorageProperties.Sftp();
        
        // Act
        sftp.setEnabled(true);
        sftp.setPort(2222);
        sftp.setHostKeyPath("/path/to/key");
        sftp.setUploadPath("/uploads");
        properties.setSftp(sftp);

        // Assert
        assertThat(properties.getSftp().getEnabled()).isTrue();
        assertThat(properties.getSftp().getPort()).isEqualTo(2222);
        assertThat(properties.getSftp().getHostKeyPath()).isEqualTo("/path/to/key");
        assertThat(properties.getSftp().getUploadPath()).isEqualTo("/uploads");
    }

    @Test
    @DisplayName("Default storage type is local")
    void defaultStorageType_isLocal() {
        // Arrange & Act
        FileStorageProperties.Storage storage = new FileStorageProperties.Storage();

        // Assert
        assertThat(storage.getType()).isEqualTo("local");
    }

    @Test
    @DisplayName("Default max size is 100MB")
    void defaultMaxSize_is100MB() {
        // Arrange & Act
        FileStorageProperties.Upload upload = new FileStorageProperties.Upload();

        // Assert
        assertThat(upload.getMaxSizeMb()).isEqualTo(100);
    }

    @Test
    @DisplayName("Default SFTP enabled is false")
    void defaultSftpEnabled_isFalse() {
        // Arrange & Act
        FileStorageProperties.Sftp sftp = new FileStorageProperties.Sftp();

        // Assert
        assertThat(sftp.getEnabled()).isFalse();
    }
}

