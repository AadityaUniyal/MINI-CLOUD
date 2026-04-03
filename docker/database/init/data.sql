-- Seed Data for MiniCloud Database

-- 1. Initial Users
INSERT INTO users (username, email, password, role, full_name, account_id, is_root_user, balance, status)
VALUES 
('admin', 'admin@minicloud.com', 'admin123', 'ADMIN', 'MiniCloud Root Admin', '1234-5678-9012', TRUE, 1000.00, 'ACTIVE'),
('aaditya', 'aaditya@example.com', 'user123', 'USER', 'Aaditya Uniyal', '5566-7788-9900', FALSE, 250.00, 'ACTIVE');

-- 2. IAM Groups & Policies
INSERT INTO iam_groups (name, description) VALUES ('Admins', 'Full administrative access'), ('Developers', 'Power users with cloud development access');
INSERT INTO iam_policies (name, description, policy_json) VALUES 
('AdministratorAccess', 'Provides full access to all services', '{"Version": "2012-10-17", "Statement": [{"Effect": "Allow", "Action": "*", "Resource": "*"}]}'),
('ReadOnlyAccess', 'Provides read-only access to all services', '{"Version": "2012-10-17", "Statement": [{"Effect": "Allow", "Action": ["Get*", "List*"], "Resource": "*"}]}');

-- 3. VPCs and Subnets
INSERT INTO vpcs (vpc_id, name, cidr_block, status, is_default, owner)
VALUES ('vpc-0a1b2c3d', 'Default-VPC', '172.31.0.0/16', 'available', TRUE, 'admin');

INSERT INTO subnets (subnet_id, vpc_id, name, cidr_block, availability_zone, available_ips, owner)
VALUES 
('subnet-012345', 'vpc-0a1b2c3d', 'Public-Subnet-1', '172.31.1.0/24', 'us-east-1a', 251, 'admin'),
('subnet-678910', 'vpc-0a1b2c3d', 'Private-Subnet-1', '172.31.2.0/24', 'us-east-1b', 251, 'admin');

-- 4. Sample Compute Instances (EC2)
INSERT INTO compute_instances (name, image, status, owner, instance_type, region, public_ip, private_ip, vpc_id, subnet_id, ami_id)
VALUES 
('Web-Server-01', 'nginx:latest', 'RUNNING', 'aaditya', 't2.micro', 'us-east-1', '3.88.1.55', '172.31.1.10', 'vpc-0a1b2c3d', 'subnet-012345', 'ami-0c55b159cbfafe1f0'),
('DB-Worker-01', 'mysql:8.0', 'STOPPED', 'aaditya', 't2.small', 'us-east-1', NULL, '172.31.1.20', 'vpc-0a1b2c3d', 'subnet-012345', 'ami-0c55b159cbfafe1f0');

-- 5. Sample Storage Buckets (S3)
INSERT INTO buckets (name, region, owner, is_public, versioning_enabled)
VALUES 
('minicloud-assets-prod', 'us-east-1', 'admin', FALSE, TRUE),
('user-uploads-temp', 'us-east-1', 'aaditya', TRUE, FALSE);

-- 6. Sample Database Instances (RDS)
INSERT INTO database_instances (name, engine, instance_class, status, endpoint, port, db_name, region)
VALUES 
('mysql-master-01', 'mysql', 'db.t3.micro', 'available', 'minicloud-db.cluster-xxxx.us-east-1.rds.amazonaws.com', 3306, 'inventory', 'us-east-1');

-- 7. Sample Messaging (SQS/SNS)
INSERT INTO sqs_queues (name, url, type, message_count, owner)
VALUES ('Order-Processing-Queue', 'https://sqs.us-east-1.amazonaws.com/1234/Order-Processing-Queue', 'Standard', 15, 'aaditya');

INSERT INTO sns_topics (name, arn, display_name, owner)
VALUES ('System-Alerts', 'arn:aws:sns:us-east-1:1234:System-Alerts', 'Alert-System', 'admin');

-- 8. Sample Audit Logs
INSERT INTO audit_logs (event_name, event_source, username, status, source_ip)
VALUES 
('RunInstances', 'ec2.amazonaws.com', 'aaditya', 'Success', '192.168.1.1'),
('CreateBucket', 's3.amazonaws.com', 'admin', 'Success', '10.0.0.5'),
('DeleteInstance', 'ec2.amazonaws.com', 'aaditya', 'Failure', '192.168.1.1');

-- 9. Sample Billing Data
INSERT INTO billing_records (service_name, resource_id, usage_quantity, unit, cost, billing_month)
VALUES 
('EC2', 'i-0fb8394e', 744, 'Hrs', 14.88, 'March 2026'),
('S3', 'minicloud-assets', 50, 'GB', 1.15, 'March 2026'),
('RDS', 'db-mysql-01', 744, 'Hrs', 12.00, 'March 2026');
