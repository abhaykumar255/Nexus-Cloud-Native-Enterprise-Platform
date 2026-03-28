-- Review Service Database Schema
-- Database: nexus_reviews

CREATE TABLE IF NOT EXISTS reviews (
    id VARCHAR(36) PRIMARY KEY,
    target_type VARCHAR(50) NOT NULL,
    target_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    user_name VARCHAR(255),
    order_id VARCHAR(36),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(100),
    comment TEXT NOT NULL,
    verified_purchase BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    helpful_count INTEGER DEFAULT 0,
    unhelpful_count INTEGER DEFAULT 0,
    reported_count INTEGER DEFAULT 0,
    moderation_notes TEXT,
    moderated_by VARCHAR(36),
    moderated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS review_images (
    review_id VARCHAR(36) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS review_helpfulness (
    id VARCHAR(36) PRIMARY KEY,
    review_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    helpful BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (review_id, user_id)
);

CREATE INDEX idx_review_target ON reviews(target_type, target_id);
CREATE INDEX idx_review_user ON reviews(user_id);
CREATE INDEX idx_review_rating ON reviews(rating);
CREATE INDEX idx_review_status ON reviews(status);
CREATE INDEX idx_review_created ON reviews(created_at);
CREATE INDEX idx_helpfulness_review_user ON review_helpfulness(review_id, user_id);

-- Comments
COMMENT ON TABLE reviews IS 'Product and restaurant reviews with ratings';
COMMENT ON TABLE review_images IS 'Images uploaded with reviews';
COMMENT ON TABLE review_helpfulness IS 'User votes on review helpfulness';
COMMENT ON COLUMN reviews.target_type IS 'PRODUCT, RESTAURANT, SELLER, DELIVERY';
COMMENT ON COLUMN reviews.status IS 'PENDING, APPROVED, REJECTED, FLAGGED, HIDDEN';
COMMENT ON COLUMN reviews.verified_purchase IS 'Whether review is from verified order';

