-- Create databases for all services
CREATE DATABASE nexus_users;
CREATE DATABASE nexus_tasks;
CREATE DATABASE workflow_db;
CREATE DATABASE file_db;
CREATE DATABASE ai_db;

-- E-commerce service databases
CREATE DATABASE nexus_products;
CREATE DATABASE nexus_inventory;
CREATE DATABASE nexus_carts;
CREATE DATABASE nexus_orders;
CREATE DATABASE nexus_payments;
CREATE DATABASE nexus_deliveries;
CREATE DATABASE nexus_tracking;
CREATE DATABASE nexus_sellers;
CREATE DATABASE nexus_restaurants;
CREATE DATABASE nexus_coupons;
CREATE DATABASE nexus_reviews;
CREATE DATABASE nexus_admin;

-- Note: nexus_auth database is created by POSTGRES_DB env variable
-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE nexus_auth TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_users TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_tasks TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE workflow_db TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE file_db TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE ai_db TO nexus_user;

-- Grant privileges for e-commerce services
GRANT ALL PRIVILEGES ON DATABASE nexus_products TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_inventory TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_carts TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_orders TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_payments TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_deliveries TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_tracking TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_sellers TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_restaurants TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_coupons TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_reviews TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_admin TO nexus_user;

