-- V1: Initial Schema for MiniCloud Compute

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

CREATE TABLE IF NOT EXISTS security_groups (
    id BIGSERIAL PRIMARY KEY,
    group_id VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    description TEXT,
    vpc_id VARCHAR(255),
    owner VARCHAR(255),
    creation_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS security_group_rules (
    id BIGSERIAL PRIMARY KEY,
    security_group_id BIGINT REFERENCES security_groups(id),
    protocol VARCHAR(20),
    from_port INTEGER,
    to_port INTEGER,
    cidr_ipv4 VARCHAR(50),
    description TEXT
);
