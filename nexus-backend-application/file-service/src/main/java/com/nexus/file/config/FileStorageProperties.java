package com.nexus.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * File Storage Configuration Properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private Storage storage = new Storage();
    private Upload upload = new Upload();
    private Sftp sftp = new Sftp();

    @Data
    public static class Storage {
        private String type = "local"; // local, s3
        private String basePath = "./file-storage";
    }

    @Data
    public static class Upload {
        private List<String> allowedExtensions;
        private Integer maxSizeMb = 100;
    }

    @Data
    public static class Sftp {
        private Boolean enabled = false;
        private Integer port = 2222;
        private String hostKeyPath = "./sftp/hostkey.ser";
        private String uploadPath = "./sftp-uploads";
    }

    // Convenience methods
    public String getType() {
        return storage.getType();
    }

    public String getBasePath() {
        return storage.getBasePath();
    }

    public List<String> getAllowedExtensions() {
        return upload.getAllowedExtensions();
    }

    public Integer getMaxSizeMb() {
        return upload.getMaxSizeMb();
    }
}

