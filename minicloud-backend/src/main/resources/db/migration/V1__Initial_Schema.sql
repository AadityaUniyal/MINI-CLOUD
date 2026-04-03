-- V1: Initial Schema for MiniCloud Backend

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    action VARCHAR(255),
    resource_id VARCHAR(255),
    details TEXT,
    source_ip VARCHAR(255),
    user_agent VARCHAR(255),
    request_parameters TEXT,
    timestamp TIMESTAMP
);

CREATE TABLE IF NOT EXISTS compute_instances (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    container_id VARCHAR(255),
    image VARCHAR(255),
    ami_id VARCHAR(255),
    status VARCHAR(50),
    owner VARCHAR(255),
    instance_type VARCHAR(50),
    region VARCHAR(50),
    availability_zone VARCHAR(50),
    public_ip VARCHAR(50),
    private_ip VARCHAR(50),
    vpc_id VARCHAR(255),
    subnet_id VARCHAR(255),
    host_port INTEGER,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS billing_records (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    resource_type VARCHAR(50),
    resource_id VARCHAR(255),
    cost DOUBLE PRECISION,
    status VARCHAR(50),
    timestamp TIMESTAMP
);

CREATE TABLE IF NOT EXISTS volumes (
    id BIGSERIAL PRIMARY KEY,
    volume_id VARCHAR(255) UNIQUE,
    size INTEGER,
    volume_type VARCHAR(50),
    iops INTEGER,
    state VARCHAR(50),
    availability_zone VARCHAR(50),
    attached_instance_id VARCHAR(255),
    device_name VARCHAR(255),
    delete_on_termination BOOLEAN,
    owner VARCHAR(255),
    creation_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vpcs (
    id BIGSERIAL PRIMARY KEY,
    vpc_id VARCHAR(255) UNIQUE,
    cidr_block VARCHAR(50),
    state VARCHAR(50),
    owner VARCHAR(255),
    is_default BOOLEAN,
    creation_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subnets (
    id BIGSERIAL PRIMARY KEY,
    subnet_id VARCHAR(255) UNIQUE,
    vpc_id VARCHAR(255),
    cidr_block VARCHAR(50),
    availability_zone VARCHAR(50),
    state VARCHAR(50),
    owner VARCHAR(255),
    map_public_ip_on_launch BOOLEAN,
    available_ip_address_count INTEGER,
    creation_time TIMESTAMP
);
