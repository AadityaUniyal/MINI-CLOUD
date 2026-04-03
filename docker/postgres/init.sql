-- Compute Schema
CREATE SCHEMA IF NOT EXISTS cloudforge_compute;
SET search_path TO cloudforge_compute;

CREATE TABLE instances (
    id SERIAL PRIMARY KEY,
    instance_id VARCHAR(10) NOT NULL UNIQUE,
    tenant_id VARCHAR(12) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    state VARCHAR(20) NOT NULL,
    ip_address VARCHAR(45),
    container_id VARCHAR(255),
    image_id VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE instance_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    vcpu INTEGER,
    memory_gb INTEGER,
    disk_gb INTEGER,
    price_per_hour DECIMAL(12,4)
);

CREATE TABLE images (
    id SERIAL PRIMARY KEY,
    image_id VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100),
    os_type VARCHAR(20),
    version VARCHAR(20),
    docker_image VARCHAR(255),
    is_public BOOLEAN DEFAULT TRUE
);

-- Network Schema
CREATE SCHEMA IF NOT EXISTS cloudforge_network;
SET search_path TO cloudforge_network;

CREATE TABLE load_balancers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20),
    ip_address VARCHAR(45),
    status VARCHAR(20),
    tenant_id VARCHAR(12)
);

-- Monitoring Schema
CREATE SCHEMA IF NOT EXISTS cloudforge_monitoring;
SET search_path TO cloudforge_monitoring;

CREATE TABLE metrics (
    id BIGSERIAL PRIMARY KEY,
    resource_id VARCHAR(255),
    metric_name VARCHAR(100),
    value DOUBLE PRECISION,
    unit VARCHAR(20),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
