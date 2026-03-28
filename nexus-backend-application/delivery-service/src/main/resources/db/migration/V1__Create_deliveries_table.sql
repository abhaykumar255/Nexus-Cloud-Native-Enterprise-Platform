-- Delivery Service Database Schema
-- Database: nexus_deliveries

CREATE TABLE IF NOT EXISTS deliveries (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL UNIQUE,
    delivery_partner_id VARCHAR(36),
    delivery_partner_name VARCHAR(255),
    delivery_partner_phone VARCHAR(20),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING_ASSIGNMENT',
    pickup_address VARCHAR(500) NOT NULL,
    pickup_latitude DOUBLE PRECISION,
    pickup_longitude DOUBLE PRECISION,
    delivery_address VARCHAR(500) NOT NULL,
    delivery_latitude DOUBLE PRECISION,
    delivery_longitude DOUBLE PRECISION,
    distance_km DECIMAL(10, 2),
    delivery_fee DECIMAL(10, 2),
    estimated_pickup_time TIMESTAMP,
    estimated_delivery_time TIMESTAMP,
    actual_pickup_time TIMESTAMP,
    actual_delivery_time TIMESTAMP,
    notes TEXT,
    cancellation_reason VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_delivery_order_id ON deliveries(order_id);
CREATE INDEX idx_delivery_partner_id ON deliveries(delivery_partner_id);
CREATE INDEX idx_delivery_status ON deliveries(status);
CREATE INDEX idx_delivery_created_at ON deliveries(created_at);

-- Add comments
COMMENT ON TABLE deliveries IS 'Delivery tracking and logistics management';
COMMENT ON COLUMN deliveries.status IS 'Delivery status: PENDING_ASSIGNMENT, ASSIGNED, PICKED_UP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED, FAILED';

