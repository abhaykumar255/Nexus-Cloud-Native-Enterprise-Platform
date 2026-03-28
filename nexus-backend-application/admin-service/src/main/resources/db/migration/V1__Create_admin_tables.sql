-- User Moderation Table
CREATE TABLE user_moderation (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    violation_type VARCHAR(50),
    reason TEXT,
    violation_count INTEGER NOT NULL DEFAULT 0,
    suspension_start_date TIMESTAMP,
    suspension_end_date TIMESTAMP,
    moderator_id BIGINT NOT NULL,
    moderator_name VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_user_id ON user_moderation(user_id);
CREATE INDEX idx_status ON user_moderation(status);

-- Seller Verification Table
CREATE TABLE seller_verification (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL UNIQUE,
    business_name VARCHAR(200) NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20),
    business_registration_number VARCHAR(100),
    tax_id VARCHAR(50),
    status VARCHAR(30) NOT NULL,
    documents TEXT,
    rejection_reason TEXT,
    document_count INTEGER NOT NULL DEFAULT 0,
    submitted_at TIMESTAMP,
    reviewed_at TIMESTAMP,
    approved_at TIMESTAMP,
    expiry_date TIMESTAMP,
    reviewer_id BIGINT,
    reviewer_name VARCHAR(100),
    review_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_seller_id ON seller_verification(seller_id);
CREATE INDEX idx_seller_status ON seller_verification(status);

-- Content Moderation Table
CREATE TABLE content_moderation (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(30) NOT NULL,
    entity_id BIGINT NOT NULL,
    reported_by_user_id BIGINT NOT NULL,
    reason VARCHAR(30) NOT NULL,
    description TEXT,
    status VARCHAR(30) NOT NULL,
    report_count INTEGER NOT NULL DEFAULT 1,
    moderator_id BIGINT,
    moderator_name VARCHAR(100),
    moderation_notes TEXT,
    reviewed_at TIMESTAMP,
    action_taken VARCHAR(30),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_entity_type_id ON content_moderation(entity_type, entity_id);
CREATE INDEX idx_content_status ON content_moderation(status);

