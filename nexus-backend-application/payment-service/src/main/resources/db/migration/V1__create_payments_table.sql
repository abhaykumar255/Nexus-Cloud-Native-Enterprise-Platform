-- Create payments table
CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    gateway_provider VARCHAR(100),
    transaction_id VARCHAR(255),
    gateway_transaction_id VARCHAR(255),
    gateway_response TEXT,
    failure_reason VARCHAR(500),
    refund_amount DECIMAL(10, 2) DEFAULT 0.00,
    refund_status VARCHAR(50),
    refund_reason VARCHAR(500),
    refunded_at TIMESTAMP,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
CREATE INDEX idx_payments_created_at ON payments(created_at);

-- Comments
COMMENT ON TABLE payments IS 'Payment transactions for orders';
COMMENT ON COLUMN payments.amount IS 'Payment amount in smallest currency unit';
COMMENT ON COLUMN payments.status IS 'Payment status: PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED';
COMMENT ON COLUMN payments.payment_method IS 'Payment method: UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING, WALLET, COD, EMI';

