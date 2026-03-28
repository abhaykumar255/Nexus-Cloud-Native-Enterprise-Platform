package com.nexus.task.domain.entity;

import com.nexus.task.domain.enums.TaskPriority;
import com.nexus.task.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Task entity with state machine support
 */
@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_project_id", columnList = "project_id"),
        @Index(name = "idx_assignee_id", columnList = "assignee_id"),
        @Index(name = "idx_creator_id", columnList = "creator_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_due_date", columnList = "due_date")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    @Column(name = "project_id")
    private String projectId;
    
    @Column(name = "assignee_id")
    private String assigneeId;
    
    @Column(name = "creator_id", nullable = false)
    private String creatorId;
    
    @Column(name = "reporter_id")
    private String reporterId;
    
    private LocalDateTime dueDate;
    
    private LocalDateTime startDate;
    
    private LocalDateTime completedDate;
    
    @Column(nullable = false)
    @Builder.Default
    private int estimatedHours = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private int actualHours = 0;
    
    @Column(length = 100)
    private String category;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "tag")
    private java.util.Set<String> tags;
    
    @Column(name = "parent_task_id")
    private String parentTaskId;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean archived = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}

