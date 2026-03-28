-- Seller Service Database Schema
-- Database: nexus_sellers

CREATE TABLE IF NOT EXISTS sellers (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE,
    business_name VARCHAR(255) NOT NULL,
    business_type VARCHAR(100),
    business_email VARCHAR(255) NOT NULL,
    business_phone VARCHAR(20) NOT NULL,
    business_address VARCHAR(500),
    gstin VARCHAR(15),
    pan VARCHAR(10),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    verification_status VARCHAR(50) DEFAULT 'NOT_VERIFIED',
    kyc_verified BOOLEAN DEFAULT FALSE,
    bank_account_number VARCHAR(50),
    bank_ifsc_code VARCHAR(11),
    bank_account_holder_name VARCHAR(255),
    commission_rate DECIMAL(5, 2) DEFAULT 10.00,
    total_products INTEGER DEFAULT 0,
    total_orders INTEGER DEFAULT 0,
    total_revenue DECIMAL(15, 2) DEFAULT 0.00,
    rating DECIMAL(3, 2),
    total_reviews INTEGER DEFAULT 0,
    logo_url VARCHAR(500),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_seller_user_id ON sellers(user_id);
CREATE INDEX idx_seller_status ON sellers(status);
CREATE INDEX idx_seller_verification_status ON sellers(verification_status);
CREATE INDEX idx_seller_business_name ON sellers(business_name);

-- Add comments
COMMENT ON TABLE sellers IS 'Seller/Vendor profiles and business information';
COMMENT ON COLUMN sellers.status IS 'Seller account status: ACTIVE, PENDING, SUSPENDED, INACTIVE';
COMMENT ON COLUMN sellers.verification_status IS 'KYC verification: NOT_VERIFIED, PENDING, VERIFIED, REJECTED';
COMMENT ON COLUMN sellers.gstin IS 'Goods and Services Tax Identification Number (India)';
COMMENT ON COLUMN sellers.pan IS 'Permanent Account Number (India)';

