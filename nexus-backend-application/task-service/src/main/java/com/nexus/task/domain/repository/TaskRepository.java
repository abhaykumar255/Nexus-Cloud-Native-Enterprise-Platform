package com.nexus.task.domain.repository;

import com.nexus.task.domain.entity.Task;
import com.nexus.task.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    
    Page<Task> findByAssigneeId(String assigneeId, Pageable pageable);
    
    Page<Task> findByCreatorId(String creatorId, Pageable pageable);
    
    Page<Task> findByProjectId(String projectId, Pageable pageable);
    
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    
    Page<Task> findByAssigneeIdAndStatus(String assigneeId, TaskStatus status, Pageable pageable);
    
    List<Task> findByParentTaskId(String parentTaskId);
    
    @Query("SELECT t FROM Task t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Task> searchTasks(String search, Pageable pageable);
    
    long countByAssigneeIdAndStatus(String assigneeId, TaskStatus status);
}

