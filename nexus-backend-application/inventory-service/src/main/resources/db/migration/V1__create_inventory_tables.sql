-- Create inventory_stock table
CREATE TABLE inventory_stock (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    seller_id VARCHAR(36) NOT NULL,
    warehouse_location VARCHAR(100) DEFAULT 'DEFAULT',
    total_stock INTEGER NOT NULL DEFAULT 0,
    available_stock INTEGER NOT NULL DEFAULT 0,
    reserved_stock INTEGER NOT NULL DEFAULT 0,
    soft_reserved_stock INTEGER NOT NULL DEFAULT 0,
    reorder_level INTEGER NOT NULL DEFAULT 10,
    max_stock_level INTEGER NOT NULL DEFAULT 1000,
    low_stock_alert BOOLEAN DEFAULT FALSE,
    out_of_stock BOOLEAN DEFAULT FALSE,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_product_id ON inventory_stock(product_id);
CREATE INDEX idx_seller_id ON inventory_stock(seller_id);
CREATE INDEX idx_warehouse_location ON inventory_stock(warehouse_location);
CREATE INDEX idx_low_stock ON inventory_stock(low_stock_alert) WHERE low_stock_alert = TRUE;
CREATE INDEX idx_out_of_stock ON inventory_stock(out_of_stock) WHERE out_of_stock = TRUE;

-- Create inventory_transactions table
CREATE TABLE inventory_transactions (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    seller_id VARCHAR(36) NOT NULL,
    order_id VARCHAR(36),
    user_id VARCHAR(36),
    transaction_type VARCHAR(30) NOT NULL,
    quantity INTEGER NOT NULL,
    previous_stock INTEGER,
    new_stock INTEGER,
    warehouse_location VARCHAR(100),
    reservation_type VARCHAR(20),
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_product_id_tx ON inventory_transactions(product_id);
CREATE INDEX idx_order_id_tx ON inventory_transactions(order_id);
CREATE INDEX idx_transaction_type ON inventory_transactions(transaction_type);
CREATE INDEX idx_created_at ON inventory_transactions(created_at);
CREATE INDEX idx_seller_id_tx ON inventory_transactions(seller_id);

-- Add comments for documentation
COMMENT ON TABLE inventory_stock IS 'Stores real-time inventory stock levels for products';
COMMENT ON TABLE inventory_transactions IS 'Audit trail for all inventory movements and reservations';

COMMENT ON COLUMN inventory_stock.total_stock IS 'Total physical stock in warehouse';
COMMENT ON COLUMN inventory_stock.available_stock IS 'Stock available for sale';
COMMENT ON COLUMN inventory_stock.reserved_stock IS 'Hard reservations for confirmed orders';
COMMENT ON COLUMN inventory_stock.soft_reserved_stock IS 'Soft reservations for items in carts';
COMMENT ON COLUMN inventory_stock.version IS 'Optimistic locking version for concurrent updates';

