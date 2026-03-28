-- User Service Database Schema
-- PostgreSQL migration script

-- User profiles table
CREATE TABLE user_profiles (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    date_of_birth DATE,
    bio VARCHAR(500),
    avatar_url VARCHAR(255),
    department VARCHAR(100),
    job_title VARCHAR(100),
    location VARCHAR(100),
    timezone VARCHAR(10),
    language VARCHAR(10),
    profile_complete BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_id ON user_profiles(user_id);
CREATE INDEX idx_email ON user_profiles(email);
CREATE INDEX idx_username ON user_profiles(username);

-- User roles table
CREATE TABLE user_roles (
    user_profile_id VARCHAR(36) NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    UNIQUE (user_profile_id, role)
);

CREATE INDEX idx_user_roles_profile_id ON user_roles(user_profile_id);

