-- Create databases for all services
CREATE DATABASE nexus_users;
CREATE DATABASE nexus_tasks;
CREATE DATABASE workflow_db;
CREATE DATABASE file_db;
CREATE DATABASE ai_db;

-- Note: nexus_auth database is created by POSTGRES_DB env variable
-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE nexus_auth TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_users TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE nexus_tasks TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE workflow_db TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE file_db TO nexus_user;
GRANT ALL PRIVILEGES ON DATABASE ai_db TO nexus_user;

