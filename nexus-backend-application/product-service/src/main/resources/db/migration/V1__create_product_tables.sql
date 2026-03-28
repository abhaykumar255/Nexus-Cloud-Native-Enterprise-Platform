-- Create categories table
CREATE TABLE categories (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(150) UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    parent_id VARCHAR(36) REFERENCES categories(id),
    display_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    category_type VARCHAR(20) DEFAULT 'PRODUCT',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_parent_id ON categories(parent_id);
CREATE INDEX idx_slug ON categories(slug);

-- Create products table
CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    sku VARCHAR(100) UNIQUE,
    description TEXT,
    seller_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) REFERENCES categories(id),
    price DECIMAL(10, 2) NOT NULL,
    discount_price DECIMAL(10, 2),
    discount_percentage DECIMAL(5, 2),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    product_type VARCHAR(20) NOT NULL DEFAULT 'PRODUCT',
    brand VARCHAR(50),
    manufacturer VARCHAR(100),
    weight INTEGER,
    length INTEGER,
    width INTEGER,
    height INTEGER,
    is_vegetarian BOOLEAN,
    is_vegan BOOLEAN,
    contains_egg BOOLEAN,
    contains_dairy BOOLEAN,
    average_rating DECIMAL(3, 2) DEFAULT 0.00,
    total_reviews INTEGER DEFAULT 0,
    total_sales INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    is_trending BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_seller_id ON products(seller_id);
CREATE INDEX idx_category_id ON products(category_id);
CREATE INDEX idx_sku ON products(sku);
CREATE INDEX idx_status ON products(status);
CREATE INDEX idx_product_type ON products(product_type);

-- Create product_images table
CREATE TABLE product_images (
    product_id VARCHAR(36) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, image_url)
);

-- Create product_tags table
CREATE TABLE product_tags (
    product_id VARCHAR(36) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (product_id, tag)
);

-- Create product_attributes table
CREATE TABLE product_attributes (
    id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    attribute_name VARCHAR(50) NOT NULL,
    attribute_value VARCHAR(100) NOT NULL
);

CREATE INDEX idx_product_id ON product_attributes(product_id);

-- Insert default categories for e-commerce
INSERT INTO categories (id, name, slug, description, category_type, display_order, created_at, updated_at) VALUES
    ('cat-1', 'Electronics', 'electronics', 'Electronic devices and gadgets', 'PRODUCT', 1, NOW(), NOW()),
    ('cat-2', 'Fashion', 'fashion', 'Clothing and accessories', 'PRODUCT', 2, NOW(), NOW()),
    ('cat-3', 'Home & Kitchen', 'home-kitchen', 'Home essentials and kitchenware', 'PRODUCT', 3, NOW(), NOW()),
    ('cat-4', 'Books', 'books', 'Books and publications', 'PRODUCT', 4, NOW(), NOW()),
    ('cat-5', 'Sports', 'sports', 'Sports equipment and gear', 'PRODUCT', 5, NOW(), NOW()),
    ('cat-6', 'Food & Beverages', 'food-beverages', 'Food and drink items', 'FOOD', 6, NOW(), NOW()),
    ('cat-7', 'Groceries', 'groceries', 'Daily grocery items', 'GROCERY', 7, NOW(), NOW()),
    ('cat-8', 'Fresh Produce', 'fresh-produce', 'Fresh fruits and vegetables', 'GROCERY', 8, NOW(), NOW()),
    ('cat-9', 'Restaurants', 'restaurants', 'Restaurant food items', 'FOOD', 9, NOW(), NOW()),
    ('cat-10', 'Fast Food', 'fast-food', 'Quick service food', 'FOOD', 10, NOW(), NOW());

-- Insert subcategories
INSERT INTO categories (id, name, slug, description, category_type, parent_id, display_order, created_at, updated_at) VALUES
    ('cat-11', 'Smartphones', 'smartphones', 'Mobile phones', 'PRODUCT', 'cat-1', 1, NOW(), NOW()),
    ('cat-12', 'Laptops', 'laptops', 'Laptop computers', 'PRODUCT', 'cat-1', 2, NOW(), NOW()),
    ('cat-13', 'Men''s Clothing', 'mens-clothing', 'Clothing for men', 'PRODUCT', 'cat-2', 1, NOW(), NOW()),
    ('cat-14', 'Women''s Clothing', 'womens-clothing', 'Clothing for women', 'PRODUCT', 'cat-2', 2, NOW(), NOW()),
    ('cat-15', 'Pizza', 'pizza', 'Pizza varieties', 'FOOD', 'cat-9', 1, NOW(), NOW()),
    ('cat-16', 'Burgers', 'burgers', 'Burger varieties', 'FOOD', 'cat-10', 1, NOW(), NOW()),
    ('cat-17', 'Organic Vegetables', 'organic-vegetables', 'Organic fresh vegetables', 'GROCERY', 'cat-8', 1, NOW(), NOW()),
    ('cat-18', 'Dairy Products', 'dairy-products', 'Milk, cheese, butter etc.', 'GROCERY', 'cat-7', 1, NOW(), NOW());

