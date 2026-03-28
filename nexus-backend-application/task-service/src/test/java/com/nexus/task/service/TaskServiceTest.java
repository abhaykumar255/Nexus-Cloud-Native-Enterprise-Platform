package com.nexus.task.service;

import com.nexus.common.exception.ResourceNotFoundException;
import com.nexus.task.domain.entity.Task;
import com.nexus.task.domain.enums.TaskEvent;
import com.nexus.task.domain.enums.TaskPriority;
import com.nexus.task.domain.enums.TaskStatus;
import com.nexus.task.domain.repository.TaskRepository;
import com.nexus.task.dto.CreateTaskRequest;
import com.nexus.task.dto.TaskDto;
import com.nexus.task.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Unit Tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private KafkaTemplate<String, com.nexus.task.event.TaskEvent> kafkaTemplate;

    @Mock
    private StateMachineFactory<TaskStatus, TaskEvent> stateMachineFactory;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;
    private TaskDto sampleTaskDto;
    private CreateTaskRequest createRequest;

    @BeforeEach
    void setUp() {
        Set<String> tags = new HashSet<>();
        tags.add("backend");
        tags.add("urgent");

        sampleTask = Task.builder()
            .id("task-123")
            .title("Test Task")
            .description("Test Description")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.HIGH)
            .projectId("project-123")
            .assigneeId("user-456")
            .creatorId("user-123")
            .reporterId("user-123")
            .estimatedHours(5)
            .category("Development")
            .tags(tags)
            .archived(false)
            .build();

        sampleTaskDto = TaskDto.builder()
            .id("task-123")
            .title("Test Task")
            .description("Test Description")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.HIGH)
            .projectId("project-123")
            .assigneeId("user-456")
            .creatorId("user-123")
            .reporterId("user-123")
            .estimatedHours(5)
            .category("Development")
            .tags(tags)
            .archived(false)
            .build();

        createRequest = new CreateTaskRequest();
        createRequest.setTitle("Test Task");
        createRequest.setDescription("Test Description");
        createRequest.setPriority(TaskPriority.HIGH);
        createRequest.setProjectId("project-123");
        createRequest.setAssigneeId("user-456");
        createRequest.setEstimatedHours(5);
        createRequest.setCategory("Development");
        createRequest.setTags(tags);
    }

    @Test
    @DisplayName("Create task - Success")
    void createTask_success() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        // Act
        TaskDto result = taskService.createTask(createRequest, "user-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getCreatorId()).isEqualTo("user-123");

        verify(taskRepository).save(any(Task.class));
        verify(kafkaTemplate, times(2)).send(anyString(), anyString(), any(com.nexus.task.event.TaskEvent.class));
    }

    @Test
    @DisplayName("Create task - Without assignee")
    void createTask_withoutAssignee() {
        // Arrange
        createRequest.setAssigneeId(null);
        Task taskWithoutAssignee = Task.builder()
            .id("task-123")
            .title("Test Task")
            .creatorId("user-123")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.MEDIUM)
            .estimatedHours(0)
            .tags(new HashSet<>())
            .build();

        when(taskRepository.save(any(Task.class))).thenReturn(taskWithoutAssignee);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        // Act
        TaskDto result = taskService.createTask(createRequest, "user-123");

        // Assert
        assertThat(result).isNotNull();
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any(com.nexus.task.event.TaskEvent.class));
    }

    @Test
    @DisplayName("Get task - Success")
    void getTask_success() {
        // Arrange
        when(taskRepository.findById("task-123")).thenReturn(Optional.of(sampleTask));
        when(taskMapper.toDto(sampleTask)).thenReturn(sampleTaskDto);

        // Act
        TaskDto result = taskService.getTask("task-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("task-123");
        verify(taskRepository).findById("task-123");
    }

    @Test
    @DisplayName("Get task - Not found")
    void getTask_notFound() {
        // Arrange
        when(taskRepository.findById("non-existent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.getTask("non-existent"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Task");
    }

    @Test
    @DisplayName("Update task - Success")
    void updateTask_success() {
        // Arrange
        when(taskRepository.findById("task-123")).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        CreateTaskRequest updateRequest = new CreateTaskRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setDescription("Updated Description");

        // Act
        TaskDto result = taskService.updateTask("task-123", updateRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
        verify(kafkaTemplate, atLeastOnce()).send(anyString(), anyString(), any(com.nexus.task.event.TaskEvent.class));
    }

    @Test
    @DisplayName("Update task - Change assignee")
    void updateTask_changeAssignee() {
        // Arrange
        when(taskRepository.findById("task-123")).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        CreateTaskRequest updateRequest = new CreateTaskRequest();
        updateRequest.setAssigneeId("new-user-789");

        // Act
        TaskDto result = taskService.updateTask("task-123", updateRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(kafkaTemplate, times(2)).send(anyString(), anyString(), any(com.nexus.task.event.TaskEvent.class));
    }

    @Test
    @DisplayName("Delete task - Success")
    void deleteTask_success() {
        // Arrange
        when(taskRepository.findById("task-123")).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).delete(sampleTask);

        // Act
        taskService.deleteTask("task-123");

        // Assert
        verify(taskRepository).delete(sampleTask);
        verify(kafkaTemplate).send(anyString(), anyString(), any(com.nexus.task.event.TaskEvent.class));
    }

    @Test
    @DisplayName("Delete task - Not found")
    void deleteTask_notFound() {
        // Arrange
        when(taskRepository.findById("non-existent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.deleteTask("non-existent"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Get tasks by assignee - Success")
    void getTasksByAssignee_success() {
        // Arrange
        List<Task> tasks = Arrays.asList(sampleTask);
        Page<Task> taskPage = new PageImpl<>(tasks);
        Pageable pageable = PageRequest.of(0, 20);

        when(taskRepository.findByAssigneeId("user-456", pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        // Act
        Page<TaskDto> result = taskService.getTasksByAssignee("user-456", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(taskRepository).findByAssigneeId("user-456", pageable);
    }

    @Test
    @DisplayName("Get tasks by project - Success")
    void getTasksByProject_success() {
        // Arrange
        List<Task> tasks = Arrays.asList(sampleTask);
        Page<Task> taskPage = new PageImpl<>(tasks);
        Pageable pageable = PageRequest.of(0, 20);

        when(taskRepository.findByProjectId("project-123", pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        // Act
        Page<TaskDto> result = taskService.getTasksByProject("project-123", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(taskRepository).findByProjectId("project-123", pageable);
    }

    @Test
    @DisplayName("Search tasks - Success")
    void searchTasks_success() {
        // Arrange
        List<Task> tasks = Arrays.asList(sampleTask);
        Page<Task> taskPage = new PageImpl<>(tasks);
        Pageable pageable = PageRequest.of(0, 20);

        when(taskRepository.searchTasks("test", pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(any(Task.class))).thenReturn(sampleTaskDto);

        // Act
        Page<TaskDto> result = taskService.searchTasks("test", pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(taskRepository).searchTasks("test", pageable);
    }
}

