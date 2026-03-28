package com.nexus.file.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FileServiceProperties Tests")
class FileServicePropertiesTest {

    @Test
    @DisplayName("Default values are set correctly")
    void defaultValues_areSetCorrectly() {
        // Arrange & Act
        FileServiceProperties properties = new FileServiceProperties();

        // Assert
        assertThat(properties.getStorage()).isNotNull();
        assertThat(properties.getUrl()).isNotNull();
    }

    @Test
    @DisplayName("Storage configuration works")
    void storageConfiguration_works() {
        // Arrange
        FileServiceProperties properties = new FileServiceProperties();
        FileServiceProperties.StorageConfig storage = new FileServiceProperties.StorageConfig();
        
        // Act
        storage.setDefaultStorageType("S3");
        properties.setStorage(storage);

        // Assert
        assertThat(properties.getStorage().getDefaultStorageType()).isEqualTo("S3");
    }

    @Test
    @DisplayName("URL configuration works")
    void urlConfiguration_works() {
        // Arrange
        FileServiceProperties properties = new FileServiceProperties();
        FileServiceProperties.UrlConfig url = new FileServiceProperties.UrlConfig();
        
        // Act
        url.setDownloadUrlPrefix("/custom/download/");
        properties.setUrl(url);

        // Assert
        assertThat(properties.getUrl().getDownloadUrlPrefix()).isEqualTo("/custom/download/");
    }

    @Test
    @DisplayName("Default storage type is LOCAL")
    void defaultStorageType_isLocal() {
        // Arrange & Act
        FileServiceProperties.StorageConfig storage = new FileServiceProperties.StorageConfig();

        // Assert
        assertThat(storage.getDefaultStorageType()).isEqualTo("LOCAL");
    }

    @Test
    @DisplayName("Default download URL prefix is set")
    void defaultDownloadUrlPrefix_isSet() {
        // Arrange & Act
        FileServiceProperties.UrlConfig url = new FileServiceProperties.UrlConfig();

        // Assert
        assertThat(url.getDownloadUrlPrefix()).isEqualTo("/api/v1/files/download/");
    }
}

