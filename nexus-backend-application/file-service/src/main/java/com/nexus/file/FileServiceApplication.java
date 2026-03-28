package com.nexus.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * File Service Application
 * Handles file upload/download, SFTP transfer, and metadata management
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.file", "com.nexus.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableMongoAuditing
public class FileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
    }
}

