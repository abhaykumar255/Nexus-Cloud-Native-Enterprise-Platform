package com.nexus.task.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.task.domain.enums.TaskEvent;
import com.nexus.task.domain.enums.TaskPriority;
import com.nexus.task.domain.enums.TaskStatus;
import com.nexus.task.dto.CreateTaskRequest;
import com.nexus.task.dto.TaskDto;
import com.nexus.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskController Unit Tests")
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController controller;

    private TaskDto sampleTaskDto;
    private CreateTaskRequest createRequest;

    @BeforeEach
    void setUp() {
        Set<String> tags = new HashSet<>();
        tags.add("backend");

        sampleTaskDto = TaskDto.builder()
            .id("task-123")
            .title("Test Task")
            .description("Test Description")
            .status(TaskStatus.TODO)
            .priority(TaskPriority.HIGH)
            .projectId("project-123")
            .assigneeId("user-456")
            .creatorId("user-123")
            .estimatedHours(5)
            .category("Development")
            .tags(tags)
            .build();

        createRequest = new CreateTaskRequest();
        createRequest.setTitle("Test Task");
        createRequest.setDescription("Test Description");
        createRequest.setPriority(TaskPriority.HIGH);
    }

    @Test
    @DisplayName("Create task - Success")
    void createTask_success() {
        // Arrange
        when(taskService.createTask(any(CreateTaskRequest.class), eq("user-123")))
            .thenReturn(sampleTaskDto);

        // Act
        ResponseEntity<ApiResponse<TaskDto>> response = controller.createTask(createRequest, "user-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getId()).isEqualTo("task-123");

        verify(taskService).createTask(any(CreateTaskRequest.class), eq("user-123"));
    }

    @Test
    @DisplayName("Get task - Success")
    void getTask_success() {
        // Arrange
        when(taskService.getTask("task-123")).thenReturn(sampleTaskDto);

        // Act
        ResponseEntity<ApiResponse<TaskDto>> response = controller.getTask("task-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getId()).isEqualTo("task-123");

        verify(taskService).getTask("task-123");
    }

    @Test
    @DisplayName("Update task - Success")
    void updateTask_success() {
        // Arrange
        when(taskService.updateTask(eq("task-123"), any(CreateTaskRequest.class)))
            .thenReturn(sampleTaskDto);

        // Act
        ResponseEntity<ApiResponse<TaskDto>> response = controller.updateTask("task-123", createRequest);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("updated");

        verify(taskService).updateTask(eq("task-123"), any(CreateTaskRequest.class));
    }

    @Test
    @DisplayName("Delete task - Success")
    void deleteTask_success() {
        // Arrange
        doNothing().when(taskService).deleteTask("task-123");

        // Act
        ResponseEntity<ApiResponse<Void>> response = controller.deleteTask("task-123");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).contains("deleted");

        verify(taskService).deleteTask("task-123");
    }

    @Test
    @DisplayName("Transition task - Success")
    void transitionTask_success() {
        // Arrange
        TaskDto transitionedTask = TaskDto.builder()
            .id("task-123")
            .title("Test Task")
            .status(TaskStatus.IN_PROGRESS)
            .build();

        when(taskService.transitionTask("task-123", TaskEvent.START))
            .thenReturn(transitionedTask);

        // Act
        ResponseEntity<ApiResponse<TaskDto>> response = controller.transitionTask("task-123", TaskEvent.START);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        verify(taskService).transitionTask("task-123", TaskEvent.START);
    }

    @Test
    @DisplayName("Get my tasks - Success")
    void getMyTasks_success() {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(sampleTaskDto);
        Page<TaskDto> taskPage = new PageImpl<>(tasks);

        when(taskService.getTasksByAssignee(eq("user-123"), any(Pageable.class)))
            .thenReturn(taskPage);

        // Act
        ResponseEntity<ApiResponse<List<TaskDto>>> response =
            controller.getMyTasks("user-123", 0, 20, "createdAt", "DESC");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        verify(taskService).getTasksByAssignee(eq("user-123"), any(Pageable.class));
    }

    @Test
    @DisplayName("Get tasks by assignee - Success")
    void getTasksByAssignee_success() {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(sampleTaskDto);
        Page<TaskDto> taskPage = new PageImpl<>(tasks);

        when(taskService.getTasksByAssignee(eq("user-456"), any(Pageable.class)))
            .thenReturn(taskPage);

        // Act
        ResponseEntity<ApiResponse<List<TaskDto>>> response =
            controller.getTasksByAssignee("user-456", 0, 20);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        verify(taskService).getTasksByAssignee(eq("user-456"), any(Pageable.class));
    }

    @Test
    @DisplayName("Get tasks by project - Success")
    void getTasksByProject_success() {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(sampleTaskDto);
        Page<TaskDto> taskPage = new PageImpl<>(tasks);

        when(taskService.getTasksByProject(eq("project-123"), any(Pageable.class)))
            .thenReturn(taskPage);

        // Act
        ResponseEntity<ApiResponse<List<TaskDto>>> response =
            controller.getTasksByProject("project-123", 0, 20, "createdAt", "DESC");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        verify(taskService).getTasksByProject(eq("project-123"), any(Pageable.class));
    }

    @Test
    @DisplayName("Search tasks - Success")
    void searchTasks_success() {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(sampleTaskDto);
        Page<TaskDto> taskPage = new PageImpl<>(tasks);

        when(taskService.searchTasks(eq("test"), any(Pageable.class)))
            .thenReturn(taskPage);

        // Act
        ResponseEntity<ApiResponse<List<TaskDto>>> response =
            controller.searchTasks("test", 0, 20);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        verify(taskService).searchTasks(eq("test"), any(Pageable.class));
    }
}

