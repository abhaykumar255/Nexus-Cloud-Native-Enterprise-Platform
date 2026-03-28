-- Create saga_instances table
CREATE TABLE saga_instances (
    id UUID PRIMARY KEY,
    saga_type VARCHAR(100) NOT NULL,
    correlation_id VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    current_step VARCHAR(100) NOT NULL,
    payload TEXT,
    compensation_data TEXT,
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_saga_correlation_id ON saga_instances(correlation_id);
CREATE INDEX idx_saga_status ON saga_instances(status);
CREATE INDEX idx_saga_type ON saga_instances(saga_type);
CREATE INDEX idx_saga_created_at ON saga_instances(created_at DESC);

