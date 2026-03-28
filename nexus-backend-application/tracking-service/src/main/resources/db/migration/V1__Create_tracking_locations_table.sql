-- Tracking Service Database Schema
-- Database: nexus_tracking

CREATE TABLE IF NOT EXISTS tracking_locations (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    delivery_partner_id VARCHAR(36),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    accuracy DOUBLE PRECISION,
    altitude DOUBLE PRECISION,
    speed DOUBLE PRECISION,
    bearing DOUBLE PRECISION,
    status VARCHAR(50),
    address VARCHAR(500),
    notes VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tracking_order_id ON tracking_locations(order_id);
CREATE INDEX idx_tracking_partner_id ON tracking_locations(delivery_partner_id);
CREATE INDEX idx_tracking_created_at ON tracking_locations(created_at);
CREATE INDEX idx_tracking_order_created ON tracking_locations(order_id, created_at DESC);

-- Add comments
COMMENT ON TABLE tracking_locations IS 'Real-time GPS tracking locations for deliveries';
COMMENT ON COLUMN tracking_locations.accuracy IS 'GPS accuracy in meters';
COMMENT ON COLUMN tracking_locations.speed IS 'Speed in meters per second';
COMMENT ON COLUMN tracking_locations.bearing IS 'Direction in degrees (0-360)';

