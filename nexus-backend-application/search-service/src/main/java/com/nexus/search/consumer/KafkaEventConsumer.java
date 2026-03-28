package com.nexus.search.consumer;

import com.nexus.search.document.TaskDocument;
import com.nexus.search.service.TaskSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Kafka Event Consumer for indexing
 * Listens to events from other services and updates Elasticsearch
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final TaskSearchService taskSearchService;

    /**
     * Index tasks when created or updated
     */
    @KafkaListener(topics = {"task.created", "task.updated"}, groupId = "search-service-group")
    public void consumeTaskEvent(Map<String, Object> event) {
        try {
            log.info("Received task event: {}", event);

            TaskDocument taskDocument = TaskDocument.builder()
                    .id(String.valueOf(event.get("taskId")))
                    .title((String) event.get("title"))
                    .description((String) event.get("description"))
                    .status((String) event.get("status"))
                    .priority((String) event.get("priority"))
                    .assigneeId((String) event.get("assigneeId"))
                    .creatorId((String) event.get("creatorId"))
                    .projectId((String) event.get("projectId"))
                    .category((String) event.get("category"))
                    .build();

            taskSearchService.indexTask(taskDocument);

        } catch (Exception e) {
            log.error("Failed to process task event", e);
        }
    }
}

