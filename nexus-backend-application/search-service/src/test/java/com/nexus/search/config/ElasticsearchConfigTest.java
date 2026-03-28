package com.nexus.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ElasticsearchConfig Tests")
class ElasticsearchConfigTest {

    private ElasticsearchConfig elasticsearchConfig;

    @BeforeEach
    void setUp() {
        elasticsearchConfig = new ElasticsearchConfig();
        ReflectionTestUtils.setField(elasticsearchConfig, "host", "localhost");
        ReflectionTestUtils.setField(elasticsearchConfig, "port", 9200);
        ReflectionTestUtils.setField(elasticsearchConfig, "scheme", "http");
    }

    @Test
    @DisplayName("RestClient bean is created")
    void restClient_beanIsCreated() {
        // Act
        RestClient restClient = elasticsearchConfig.restClient();

        // Assert
        assertThat(restClient).isNotNull();
        
        // Cleanup
        try {
            restClient.close();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    @DisplayName("ElasticsearchTransport bean is created")
    void elasticsearchTransport_beanIsCreated() {
        // Arrange
        RestClient restClient = elasticsearchConfig.restClient();

        // Act
        ElasticsearchTransport transport = elasticsearchConfig.elasticsearchTransport(restClient);

        // Assert
        assertThat(transport).isNotNull();
        
        // Cleanup
        try {
            transport.close();
            restClient.close();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    @DisplayName("ElasticsearchClient bean is created")
    void elasticsearchClient_beanIsCreated() {
        // Arrange
        RestClient restClient = elasticsearchConfig.restClient();
        ElasticsearchTransport transport = elasticsearchConfig.elasticsearchTransport(restClient);

        // Act
        ElasticsearchClient client = elasticsearchConfig.elasticsearchClient(transport);

        // Assert
        assertThat(client).isNotNull();
        
        // Cleanup
        try {
            transport.close();
            restClient.close();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
}

