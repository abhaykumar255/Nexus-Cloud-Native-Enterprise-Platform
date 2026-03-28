package com.nexus.task.controller;

import com.nexus.common.dto.ApiResponse;
import com.nexus.common.dto.PaginationInfo;
import com.nexus.task.domain.enums.TaskEvent;
import com.nexus.task.dto.CreateTaskRequest;
import com.nexus.task.dto.TaskDto;
import com.nexus.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task REST controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    /**
     * Create a new task
     * POST /api/v1/tasks
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskDto>> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @RequestHeader("X-User-Id") String userId) {
        log.info("Create task request by user: {}", userId);
        
        TaskDto task = taskService.createTask(request, userId);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", task));
    }
    
    /**
     * Get task by ID
     * GET /api/v1/tasks/{taskId}
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskDto>> getTask(@PathVariable String taskId) {
        log.info("Get task request: {}", taskId);
        
        TaskDto task = taskService.getTask(taskId);
        
        return ResponseEntity.ok(ApiResponse.success(task));
    }
    
    /**
     * Update task
     * PUT /api/v1/tasks/{taskId}
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskDto>> updateTask(
            @PathVariable String taskId,
            @Valid @RequestBody CreateTaskRequest request) {
        log.info("Update task request: {}", taskId);
        
        TaskDto task = taskService.updateTask(taskId, request);
        
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", task));
    }
    
    /**
     * Delete task
     * DELETE /api/v1/tasks/{taskId}
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String taskId) {
        log.info("Delete task request: {}", taskId);
        
        taskService.deleteTask(taskId);
        
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully", null));
    }
    
    /**
     * Transition task state
     * POST /api/v1/tasks/{taskId}/transition
     */
    @PostMapping("/{taskId}/transition")
    public ResponseEntity<ApiResponse<TaskDto>> transitionTask(
            @PathVariable String taskId,
            @RequestParam TaskEvent event) {
        log.info("Transition task request: {} with event: {}", taskId, event);
        
        TaskDto task = taskService.transitionTask(taskId, event);
        
        return ResponseEntity.ok(ApiResponse.success("Task transitioned successfully", task));
    }
    
    /**
     * Get my tasks (by assignee)
     * GET /api/v1/tasks/my
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getMyTasks(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Get my tasks request for user: {}", userId);
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<TaskDto> tasks = taskService.getTasksByAssignee(userId, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(tasks.getContent(), PaginationInfo.from(tasks)));
    }
    
    /**
     * Get tasks by assignee
     * GET /api/v1/tasks/assignee/{assigneeId}
     */
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getTasksByAssignee(
            @PathVariable String assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Get tasks by assignee: {}", assigneeId);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = taskService.getTasksByAssignee(assigneeId, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(tasks.getContent(), PaginationInfo.from(tasks)));
    }
    
    /**
     * Get tasks by project
     * GET /api/v1/tasks/project/{projectId}
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskDto>>> getTasksByProject(
            @PathVariable String projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        log.info("Get tasks by project: {}", projectId);
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<TaskDto> tasks = taskService.getTasksByProject(projectId, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(tasks.getContent(), PaginationInfo.from(tasks)));
    }
    
    /**
     * Search tasks
     * GET /api/v1/tasks/search
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TaskDto>>> searchTasks(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Search tasks request - query: {}", q);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskDto> tasks = taskService.searchTasks(q, pageable);
        
        return ResponseEntity.ok(ApiResponse.paginated(tasks.getContent(), PaginationInfo.from(tasks)));
    }
}

