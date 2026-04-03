-- IAM Schema
CREATE DATABASE IF NOT EXISTS cloudforge_iam;
USE cloudforge_iam;

CREATE TABLE tenants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    account_id VARCHAR(12) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE role_permissions (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    tenant_id BIGINT,
    is_root_user BOOLEAN DEFAULT FALSE,
    account_id VARCHAR(12),
    iam_username VARCHAR(255),
    owner VARCHAR(255),
    balance DOUBLE DEFAULT 0.0,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Billing Schema
CREATE DATABASE IF NOT EXISTS cloudforge_billing;
USE cloudforge_billing;

CREATE TABLE usage_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(12) NOT NULL,
    resource_type VARCHAR(50),
    resource_id VARCHAR(255),
    quantity DOUBLE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(12) NOT NULL,
    total_amount DOUBLE,
    status VARCHAR(20) DEFAULT 'PENDING',
    billing_period_start DATE,
    billing_period_end DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Storage Schema
CREATE DATABASE IF NOT EXISTS cloudforge_storage;
USE cloudforge_storage;

CREATE TABLE buckets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id VARCHAR(12) NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    region VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
