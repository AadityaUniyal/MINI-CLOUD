-- V1: Initial Schema for MiniCloud IAM

CREATE TABLE IF NOT EXISTS tenants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    account_id VARCHAR(255) UNIQUE,
    status VARCHAR(50),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    phone_number VARCHAR(255),
    address TEXT,
    country VARCHAR(100),
    balance DOUBLE PRECISION,
    status VARCHAR(50),
    created_at TIMESTAMP,
    account_id VARCHAR(255),
    account_alias VARCHAR(255),
    is_root_user BOOLEAN,
    iam_username VARCHAR(255),
    access_key VARCHAR(255),
    secret_key VARCHAR(255),
    owner VARCHAR(255),
    tenant_id BIGINT REFERENCES tenants(id)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT REFERENCES roles(id),
    permission_id BIGINT REFERENCES permissions(id),
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS api_keys (
    id BIGSERIAL PRIMARY KEY,
    key_id VARCHAR(255) UNIQUE,
    secret_key VARCHAR(255),
    user_id BIGINT REFERENCES users(id),
    status VARCHAR(50),
    created_at TIMESTAMP,
    expires_at TIMESTAMP
);
