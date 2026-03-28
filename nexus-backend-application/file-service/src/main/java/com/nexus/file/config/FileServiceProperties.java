package com.nexus.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for File Service
 * Storage type and URL pattern configuration
 */
@Configuration
@ConfigurationProperties(prefix = "file-service")
@Data
public class FileServiceProperties {

    private StorageConfig storage = new StorageConfig();
    private UrlConfig url = new UrlConfig();

    @Data
    public static class StorageConfig {
        private String defaultStorageType = "LOCAL";
    }

    @Data
    public static class UrlConfig {
        private String downloadUrlPrefix = "/api/v1/files/download/";
    }
}

