package com.nexus.task.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.task.domain.entity.Task;
import com.nexus.task.domain.enums.TaskEvent;
import com.nexus.task.domain.enums.TaskStatus;
import com.nexus.task.domain.repository.TaskRepository;
import com.nexus.task.dto.CreateTaskRequest;
import com.nexus.task.dto.TaskDto;
import com.nexus.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

/**
 * Task service with state machine, CRUD operations, and event publishing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final KafkaTemplate<String, com.nexus.task.event.TaskEvent> kafkaTemplate;
    private final StateMachineFactory<TaskStatus, TaskEvent> stateMachineFactory;
    
    private static final String TASK_CREATED_TOPIC = "task.created";
    private static final String TASK_UPDATED_TOPIC = "task.updated";
    private static final String TASK_DELETED_TOPIC = "task.deleted";
    private static final String TASK_ASSIGNED_TOPIC = "task.assigned";
    private static final String TASK_STATUS_CHANGED_TOPIC = "task.status.changed";
    
    /**
     * Create a new task
     */
    @Transactional
    public TaskDto createTask(CreateTaskRequest request, String creatorId) {
        log.info("Creating task: {} by user: {}", request.getTitle(), creatorId);
        
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .projectId(request.getProjectId())
                .assigneeId(request.getAssigneeId())
                .creatorId(creatorId)
                .reporterId(creatorId)
                .dueDate(request.getDueDate())
                .estimatedHours(request.getEstimatedHours() != null ? request.getEstimatedHours() : 0)
                .category(request.getCategory())
                .tags(request.getTags() != null ? request.getTags() : new HashSet<>())
                .parentTaskId(request.getParentTaskId())
                .status(TaskStatus.TODO)
                .build();
        
        task = taskRepository.save(task);
        
        // Publish task created event
        publishTaskEvent(com.nexus.task.event.TaskEvent.EventType.TASK_CREATED, task);
        
        // If task is assigned, publish assignment event
        if (task.getAssigneeId() != null) {
            publishTaskEvent(com.nexus.task.event.TaskEvent.EventType.TASK_ASSIGNED, task);
        }
        
        log.info("Task created successfully: {}", task.getId());
        
        return taskMapper.toDto(task);
    }
    
    /**
     * Get task by ID (with caching)
     */
    @Cacheable(value = "tasks", key = "#taskId")
    @Transactional(readOnly = true)
    public TaskDto getTask(String taskId) {
        log.debug("Fetching task: {}", taskId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        
        return taskMapper.toDto(task);
    }
    
    /**
     * Update task
     */
    @CacheEvict(value = "tasks", key = "#taskId")
    @Transactional
    public TaskDto updateTask(String taskId, CreateTaskRequest request) {
        log.info("Updating task: {}", taskId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        
        String oldAssigneeId = task.getAssigneeId();
        
        // Update fields
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getProjectId() != null) task.setProjectId(request.getProjectId());
        if (request.getAssigneeId() != null) task.setAssigneeId(request.getAssigneeId());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());
        if (request.getCategory() != null) task.setCategory(request.getCategory());
        if (request.getTags() != null) task.setTags(request.getTags());
        
        task = taskRepository.save(task);
        
        // Publish task updated event
        publishTaskEvent(com.nexus.task.event.TaskEvent.EventType.TASK_UPDATED, task);
        
        // If assignee changed, publish assignment event
        if (request.getAssigneeId() != null && !request.getAssigneeId().equals(oldAssigneeId)) {
            publishTaskEvent(com.nexus.task.event.TaskEvent.EventType.TASK_ASSIGNED, task);
        }
        
        log.info("Task updated successfully: {}", task.getId());
        
        return taskMapper.toDto(task);
    }
    
    /**
     * Delete task
     */
    @CacheEvict(value = "tasks", key = "#taskId")
    @Transactional
    public void deleteTask(String taskId) {
        log.info("Deleting task: {}", taskId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));
        
        taskRepository.delete(task);
        
        // Publish task deleted event
        publishTaskEvent(com.nexus.task.event.TaskEvent.EventType.TASK_DELETED, task);
        
        log.info("Task deleted successfully: {}", taskId);
    }
    
    /**
     * Transition task state using state machine
     */
    @CacheEvict(value = "tasks", key = "#taskId")
    @Transactional
    public TaskDto transitionTask(String taskId, TaskEvent event) {
        log.info("Transitioning task: {} with event: {}", taskId, event);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        TaskStatus oldStatus = task.getStatus();
        final TaskStatus currentStatus = task.getStatus();

        // Create state machine and restore state
        StateMachine<TaskStatus, TaskEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(access -> access.resetStateMachineReactively(
                        new DefaultStateMachineContext<>(currentStatus, null, null, null)
                ).block());
        stateMachine.startReactively().block();
        
        // Send event
        boolean success = stateMachine.sendEvent(event);
        
        if (!success) {
            throw new IllegalStateException(
                    String.format("Invalid state transition: %s -> %s", oldStatus, event));
        }
        
        // Update task status
        TaskStatus newStatus = stateMachine.getState().getId();
        task.setStatus(newStatus);
        
        // Update timestamps based on status
        if (newStatus == TaskStatus.IN_PROGRESS && task.getStartDate() == null) {
            task.setStartDate(LocalDateTime.now());
        } else if (newStatus == TaskStatus.COMPLETED && task.getCompletedDate() == null) {
            task.setCompletedDate(LocalDateTime.now());
        }
        
        task = taskRepository.save(task);
        
        // Publish status changed event
        publishTaskEvent(com.nexus.task.event.TaskEvent.EventType.TASK_STATUS_CHANGED, task);
        
        log.info("Task transitioned: {} -> {}", oldStatus, newStatus);
        
        return taskMapper.toDto(task);
    }
    
    /**
     * Get tasks by assignee
     */
    @Transactional(readOnly = true)
    public Page<TaskDto> getTasksByAssignee(String assigneeId, Pageable pageable) {
        log.debug("Fetching tasks for assignee: {}", assigneeId);
        return taskRepository.findByAssigneeId(assigneeId, pageable).map(taskMapper::toDto);
    }
    
    /**
     * Get tasks by project
     */
    @Transactional(readOnly = true)
    public Page<TaskDto> getTasksByProject(String projectId, Pageable pageable) {
        log.debug("Fetching tasks for project: {}", projectId);
        return taskRepository.findByProjectId(projectId, pageable).map(taskMapper::toDto);
    }
    
    /**
     * Search tasks
     */
    @Transactional(readOnly = true)
    public Page<TaskDto> searchTasks(String search, Pageable pageable) {
        log.debug("Searching tasks with query: {}", search);
        return taskRepository.searchTasks(search, pageable).map(taskMapper::toDto);
    }
    
    /**
     * Publish task event to Kafka
     */
    private void publishTaskEvent(com.nexus.task.event.TaskEvent.EventType eventType, Task task) {
        com.nexus.task.event.TaskEvent event = com.nexus.task.event.TaskEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .taskId(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .assigneeId(task.getAssigneeId())
                .creatorId(task.getCreatorId())
                .projectId(task.getProjectId())
                .timestamp(Instant.now())
                .build();
        
        String topic = switch (eventType) {
            case TASK_CREATED -> TASK_CREATED_TOPIC;
            case TASK_UPDATED -> TASK_UPDATED_TOPIC;
            case TASK_DELETED -> TASK_DELETED_TOPIC;
            case TASK_ASSIGNED -> TASK_ASSIGNED_TOPIC;
            case TASK_STATUS_CHANGED -> TASK_STATUS_CHANGED_TOPIC;
        };
        
        kafkaTemplate.send(topic, task.getId(), event);
        log.info("Published {} event for task: {}", eventType, task.getId());
    }
}

