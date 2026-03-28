package com.nexus.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Search Service Application
 * Provides full-text search, autocomplete, and faceted search via Elasticsearch
 */
@SpringBootApplication(scanBasePackages = {"com.nexus.search", "com.nexus.common"})
@EnableDiscoveryClient
public class SearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchServiceApplication.class, args);
    }
}

