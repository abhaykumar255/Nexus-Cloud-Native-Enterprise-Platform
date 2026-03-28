-- Task Service Database Schema
-- PostgreSQL migration script

-- Tasks table with partitioning support
CREATE TABLE tasks (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    project_id VARCHAR(36),
    assignee_id VARCHAR(36),
    creator_id VARCHAR(36) NOT NULL,
    reporter_id VARCHAR(36),
    due_date TIMESTAMP,
    start_date TIMESTAMP,
    completed_date TIMESTAMP,
    estimated_hours INTEGER NOT NULL DEFAULT 0,
    actual_hours INTEGER NOT NULL DEFAULT 0,
    category VARCHAR(100),
    parent_task_id VARCHAR(36),
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better query performance
CREATE INDEX idx_project_id ON tasks(project_id);
CREATE INDEX idx_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_creator_id ON tasks(creator_id);
CREATE INDEX idx_status ON tasks(status);
CREATE INDEX idx_due_date ON tasks(due_date);
CREATE INDEX idx_created_at ON tasks(created_at);
CREATE INDEX idx_parent_task_id ON tasks(parent_task_id);

-- Task tags table
CREATE TABLE task_tags (
    task_id VARCHAR(36) NOT NULL,
    tag VARCHAR(50) NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    UNIQUE (task_id, tag)
);

CREATE INDEX idx_task_tags_task_id ON task_tags(task_id);
CREATE INDEX idx_task_tags_tag ON task_tags(tag);

-- Task comments table (for future enhancement)
CREATE TABLE task_comments (
    id VARCHAR(36) PRIMARY KEY,
    task_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE INDEX idx_task_comments_task_id ON task_comments(task_id);
CREATE INDEX idx_task_comments_user_id ON task_comments(user_id);

-- Task attachments table (for future enhancement)
CREATE TABLE task_attachments (
    id VARCHAR(36) PRIMARY KEY,
    task_id VARCHAR(36) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_size BIGINT,
    uploaded_by VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE INDEX idx_task_attachments_task_id ON task_attachments(task_id);

