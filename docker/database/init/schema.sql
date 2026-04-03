-- MiniCloud Management Console - Comprehensive PostgreSQL Schema
-- Covers Compute, Storage, Networking, Messaging, IAM, and Governance

-- SECTION 1: IDENTITY AND ACCESS MANAGEMENT (IAM) & USERS
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    address TEXT,
    country VARCHAR(50),
    balance DOUBLE PRECISION DEFAULT 0.0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    account_id VARCHAR(20) UNIQUE,
    account_alias VARCHAR(50),
    is_root_user BOOLEAN DEFAULT FALSE,
    iam_username VARCHAR(50),
    access_key VARCHAR(50),
    secret_key VARCHAR(100),
    owner VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS iam_groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS iam_roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    assume_role_policy TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS iam_policies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    policy_json TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SECTION 2: COMPUTE SERVICES (EC2, LAMBDA)
CREATE TABLE IF NOT EXISTS compute_instances (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    container_id VARCHAR(100),
    image VARCHAR(100),
    status VARCHAR(20),
    owner VARCHAR(50),
    instance_type VARCHAR(20),
    region VARCHAR(30),
    availability_zone VARCHAR(30),
    public_ip VARCHAR(20),
    private_ip VARCHAR(20),
    vpc_id VARCHAR(50),
    subnet_id VARCHAR(50),
    ami_id VARCHAR(50),
    ebs_optimized BOOLEAN DEFAULT FALSE,
    key_pair_name VARCHAR(100),
    host_port INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lambda_functions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    runtime VARCHAR(50),
    handler VARCHAR(100),
    description TEXT,
    timeout INT DEFAULT 3,
    memory_size INT DEFAULT 128,
    status VARCHAR(20),
    code_size BIGINT,
    owner VARCHAR(50),
    arn VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SECTION 3: STORAGE SERVICES (S3, RDS, DYNAMODB)
CREATE TABLE IF NOT EXISTS buckets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    region VARCHAR(30),
    owner VARCHAR(50),
    is_public BOOLEAN DEFAULT FALSE,
    versioning_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS storage_files (
    id BIGSERIAL PRIMARY KEY,
    bucket_name VARCHAR(255),
    file_name VARCHAR(255),
    file_path TEXT,
    file_size BIGINT,
    content_type VARCHAR(100),
    last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    owner VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS database_instances (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    engine VARCHAR(20),
    engine_version VARCHAR(20),
    instance_class VARCHAR(20),
    status VARCHAR(20),
    endpoint VARCHAR(255),
    port INT,
    db_name VARCHAR(100),
    master_username VARCHAR(50),
    storage_size_gb INT,
    region VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dynamo_tables (
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(255) UNIQUE NOT NULL,
    partition_key VARCHAR(100),
    sort_key VARCHAR(100),
    status VARCHAR(20),
    item_count BIGINT DEFAULT 0,
    read_capacity INT DEFAULT 5,
    write_capacity INT DEFAULT 5,
    owner VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SECTION 4: NETWORKING (VPC, SUBNET, ELB)
CREATE TABLE IF NOT EXISTS vpcs (
    id BIGSERIAL PRIMARY KEY,
    vpc_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100),
    cidr_block VARCHAR(20),
    status VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    tenancy VARCHAR(20) DEFAULT 'default',
    owner VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subnets (
    id BIGSERIAL PRIMARY KEY,
    subnet_id VARCHAR(50) UNIQUE NOT NULL,
    vpc_id VARCHAR(50),
    name VARCHAR(100),
    cidr_block VARCHAR(20),
    availability_zone VARCHAR(30),
    available_ips INT,
    owner VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS load_balancers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    dns_name VARCHAR(255),
    type VARCHAR(20),
    scheme VARCHAR(20),
    status VARCHAR(20),
    vpc_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SECTION 5: MESSAGING (SQS, SNS)
CREATE TABLE IF NOT EXISTS sqs_queues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    url VARCHAR(255),
    type VARCHAR(20),
    message_count INT DEFAULT 0,
    visibility_timeout INT DEFAULT 30,
    owner VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sns_topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    arn VARCHAR(255) UNIQUE,
    display_name VARCHAR(100),
    owner VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SECTION 6: MONITORING AND BILLING
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGSERIAL PRIMARY KEY,
    event_name VARCHAR(100),
    event_source VARCHAR(100),
    username VARCHAR(50),
    status VARCHAR(20),
    request_id VARCHAR(100),
    source_ip VARCHAR(50),
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS billing_records (
    id BIGSERIAL PRIMARY KEY,
    service_name VARCHAR(50),
    resource_id VARCHAR(100),
    usage_quantity DOUBLE PRECISION,
    unit VARCHAR(20),
    cost DOUBLE PRECISION,
    currency VARCHAR(10) DEFAULT 'USD',
    billing_month VARCHAR(20),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
