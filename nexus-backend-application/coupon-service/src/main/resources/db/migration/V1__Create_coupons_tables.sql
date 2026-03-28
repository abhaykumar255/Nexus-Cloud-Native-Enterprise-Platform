-- Coupon Service Database Schema
-- Database: nexus_coupons

CREATE TABLE IF NOT EXISTS coupons (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    type VARCHAR(50) NOT NULL,
    discount_type VARCHAR(50) NOT NULL,
    discount_value DECIMAL(10, 2) NOT NULL,
    max_discount_amount DECIMAL(10, 2),
    min_order_amount DECIMAL(10, 2),
    usage_limit INTEGER NOT NULL DEFAULT 0,
    usage_limit_per_user INTEGER NOT NULL DEFAULT 1,
    used_count INTEGER NOT NULL DEFAULT 0,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    applicable_category VARCHAR(100),
    applicable_product_type VARCHAR(100),
    applicable_seller_id VARCHAR(36),
    first_order_only BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_by VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coupon_usage (
    id VARCHAR(36) PRIMARY KEY,
    coupon_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    order_id VARCHAR(36) NOT NULL,
    discount_amount DECIMAL(10, 2) NOT NULL,
    order_amount DECIMAL(10, 2) NOT NULL,
    used_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_coupon_code ON coupons(code);
CREATE INDEX idx_coupon_type ON coupons(type);
CREATE INDEX idx_coupon_status ON coupons(status);
CREATE INDEX idx_coupon_valid_dates ON coupons(valid_from, valid_until);

CREATE INDEX idx_usage_coupon_id ON coupon_usage(coupon_id);
CREATE INDEX idx_usage_user_id ON coupon_usage(user_id);
CREATE INDEX idx_usage_order_id ON coupon_usage(order_id);
CREATE INDEX idx_usage_coupon_user ON coupon_usage(coupon_id, user_id);

-- Comments
COMMENT ON TABLE coupons IS 'Discount coupons and promotional codes';
COMMENT ON TABLE coupon_usage IS 'Coupon usage tracking for orders';
COMMENT ON COLUMN coupons.type IS 'GENERAL, USER_SPECIFIC, FIRST_ORDER, CATEGORY_SPECIFIC, SELLER_SPECIFIC, CASHBACK';
COMMENT ON COLUMN coupons.discount_type IS 'PERCENTAGE or FIXED_AMOUNT';
COMMENT ON COLUMN coupons.status IS 'ACTIVE, INACTIVE, EXPIRED, EXHAUSTED';

