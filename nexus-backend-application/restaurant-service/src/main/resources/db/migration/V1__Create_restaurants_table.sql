-- Restaurant Service Database Schema
-- Database: nexus_restaurants

CREATE TABLE IF NOT EXISTS restaurants (
    id VARCHAR(36) PRIMARY KEY,
    seller_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    cuisine_type VARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(255),
    address VARCHAR(500),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    opening_time VARCHAR(10),
    closing_time VARCHAR(10),
    is_open BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    rating DECIMAL(3, 2),
    total_reviews BIGINT DEFAULT 0,
    logo_url VARCHAR(500),
    banner_url VARCHAR(500),
    min_order_amount DECIMAL(10, 2) DEFAULT 0.00,
    delivery_fee DECIMAL(10, 2) DEFAULT 0.00,
    average_delivery_time_minutes INTEGER DEFAULT 30,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_restaurant_seller_id ON restaurants(seller_id);
CREATE INDEX idx_restaurant_status ON restaurants(status);
CREATE INDEX idx_restaurant_cuisine ON restaurants(cuisine_type);
CREATE INDEX idx_restaurant_is_open ON restaurants(is_open);
CREATE INDEX idx_restaurant_location ON restaurants(latitude, longitude);

-- Add comments
COMMENT ON TABLE restaurants IS 'Restaurant profiles for food delivery vertical';
COMMENT ON COLUMN restaurants.status IS 'Restaurant status: ACTIVE, INACTIVE, SUSPENDED';
COMMENT ON COLUMN restaurants.is_open IS 'Current operational status (open/closed for orders)';
COMMENT ON COLUMN restaurants.cuisine_type IS 'Type of cuisine: INDIAN, CHINESE, ITALIAN, etc.';

