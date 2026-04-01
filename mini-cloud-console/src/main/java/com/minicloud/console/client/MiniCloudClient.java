package com.minicloud.console.client;

import com.minicloud.console.dto.InstanceDto;
import com.minicloud.console.dto.BucketDto;
import com.minicloud.console.dto.IamDto;
import com.minicloud.console.dto.LambdaDto;
import com.minicloud.console.dto.DatabaseDto;
import com.minicloud.console.dto.VpcDto;
import com.minicloud.console.dto.FileDto;
import com.minicloud.console.dto.IamRoleDto;
import com.minicloud.console.dto.IamPolicyDto;
import com.minicloud.console.dto.AuditLogDto;
import com.minicloud.console.dto.DynamoTableDto;
import com.minicloud.console.dto.DynamoItemDto;
import com.minicloud.console.dto.SecurityGroupDto;
import com.minicloud.console.dto.LoadBalancerDto;
import com.minicloud.console.dto.SubnetDto;
import com.minicloud.console.dto.QueueDto;
import com.minicloud.console.dto.TopicDto;
import com.minicloud.console.dto.IamGroupDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.ArrayList;

@Component
public class MiniCloudClient {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api";

    public MiniCloudClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // EC2
    private List<InstanceDto> mockInstances = new ArrayList<>(List.of(
        new InstanceDto(1L, "web-server-01", "running"),
        new InstanceDto(2L, "db-primary", "running"),
        new InstanceDto(3L, "test-instance", "stopped")
    ));

    public List<InstanceDto> getInstances() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/compute/list", InstanceDto[].class));
        } catch (Exception e) {
            return mockInstances;
        }
    }

    public List<AuditLogDto> getAuditLogs() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/audit/logs", AuditLogDto[].class));
        } catch (Exception e) {
            return mockLogs;
        }
    }

    private List<AuditLogDto> mockLogs = new java.util.ArrayList<>(List.of(
        new AuditLogDto("ConsoleLogin", "admin_user", "2024-04-01 10:15:00", "N/A"),
        new AuditLogDto("StartInstance", "admin_user", "2024-04-01 10:20:45", "i-0abcd1234")
    ));

    public void addAuditLog(String action, String resourceId) {
        mockLogs.add(0, new AuditLogDto(action, "admin_user", java.time.LocalDateTime.now().toString(), resourceId));
    }

    public void startInstance(Long id) {
        try {
            restTemplate.postForLocation(baseUrl + "/compute/" + id + "/start", null);
        } catch (Exception e) {
            mockInstances.stream().filter(i -> i.getId().equals(id)).findFirst().ifPresent(i -> i.setState("running"));
        }
        addAuditLog("StartInstance", id.toString());
    }

    public void stopInstance(Long id) {
        try {
            restTemplate.postForLocation(baseUrl + "/compute/" + id + "/stop", null);
        } catch (Exception e) {
            mockInstances.stream().filter(i -> i.getId().equals(id)).findFirst().ifPresent(i -> i.setState("stopped"));
        }
        addAuditLog("StopInstance", id.toString());
    }

    public void terminateInstance(Long id) {
        try {
            restTemplate.delete(baseUrl + "/compute/" + id);
        } catch (Exception e) {
            mockInstances.removeIf(i -> i.getId().equals(id));
        }
        addAuditLog("TerminateInstance", id.toString());
    }

    // DynamoDB
    private List<DynamoTableDto> mockDynamoTables = new java.util.ArrayList<>(List.of(
        new DynamoTableDto("UserProfiles", "userId", 1500, "Active"),
        new DynamoTableDto("PlatformSettings", "configKey", 42, "Active")
    ));

    private java.util.Map<String, List<DynamoItemDto>> mockDynamoItems = new java.util.HashMap<>(java.util.Map.of(
        "UserProfiles", new java.util.ArrayList<>(List.of(
            new DynamoItemDto("user_101", "{ \"name\": \"John Doe\", \"plan\": \"premium\" }"),
            new DynamoItemDto("user_102", "{ \"name\": \"Jane Smith\", \"plan\": \"free\" }")
        ))
    ));

    public List<DynamoTableDto> getDynamoTables() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/dynamodb/tables", DynamoTableDto[].class));
        } catch (Exception e) {
            return mockDynamoTables;
        }
    }

    public List<DynamoItemDto> getDynamoItems(String tableName) {
        try {
            // This is a simplification; real DynamoDB has complex scan/query
            return List.of(restTemplate.getForObject(baseUrl + "/dynamodb/table/" + tableName + "/items", DynamoItemDto[].class));
        } catch (Exception e) {
            return mockDynamoItems.getOrDefault(tableName, new java.util.ArrayList<>());
        }
    }
    private java.util.Map<String, List<FileDto>> mockBucketFiles = new java.util.HashMap<>(java.util.Map.of(
        "user-assets-prod", new java.util.ArrayList<>(List.of(
            new FileDto("profile_123.jpg", "156 KB", "2024-03-31"),
            new FileDto("config.json", "2 KB", "2024-03-30")
        )),
        "logs-archive-2024", new java.util.ArrayList<>(List.of(
            new FileDto("access_log_mar24.zip", "45 MB", "2024-04-01")
        ))
    ));

    public List<BucketDto> getBuckets() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/buckets", BucketDto[].class));
        } catch (Exception e) {
            return List.of(
                new BucketDto(1L, "user-assets-prod", "us-east-1"),
                new BucketDto(2L, "logs-archive-2024", "us-west-2"),
                new BucketDto(3L, "static-website-files", "eu-central-1")
            );
        }
    }

    public List<FileDto> getFiles(String bucketName) {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/buckets/" + bucketName + "/files", FileDto[].class));
        } catch (Exception e) {
            return mockBucketFiles.getOrDefault(bucketName, new java.util.ArrayList<>());
        }
    }

    public void uploadFile(String bucketName, String fileName) {
        // Simulation: in real app, we would send a MultipartFile
        mockBucketFiles.computeIfAbsent(bucketName, k -> new java.util.ArrayList<>())
            .add(new FileDto(fileName, "0 KB", "Just now"));
    }

    public void deleteFile(String bucketName, String fileName) {
        try {
            restTemplate.delete(baseUrl + "/buckets/" + bucketName + "/files/" + fileName);
        } catch (Exception e) {
            if (mockBucketFiles.containsKey(bucketName)) {
                mockBucketFiles.get(bucketName).removeIf(f -> f.getName().equals(fileName));
            }
        }
    }

    // IAM
    public List<IamDto> getIamUsers() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/iam/users", IamDto[].class));
        } catch (Exception e) {
            return List.of(
                new IamDto(1L, "admin_user", "Administrator"),
                new IamDto(2L, "dev_lead", "Developer"),
                new IamDto(3L, "intern_01", "ReadOnly")
            );
        }
    }

    public List<IamRoleDto> getIamRoles() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/iam/roles", IamRoleDto[].class));
        } catch (Exception e) {
            return List.of(
                new IamRoleDto("AdminRole", "arn:aws:iam::123:role/Admin", "2024-01-01"),
                new IamRoleDto("DevRole", "arn:aws:iam::123:role/Dev", "2024-02-15"),
                new IamRoleDto("S3ReadOnlyRole", "arn:aws:iam::123:role/S3ReadOnly", "2024-03-20")
            );
        }
    }

    public List<IamPolicyDto> getIamPolicies() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/iam/policies", IamPolicyDto[].class));
        } catch (Exception e) {
            return List.of(
                new IamPolicyDto("S3FullAccess", "arn:aws:iam::aws:policy/S3Full", "Full S3 access"),
                new IamPolicyDto("Ec2ReadOnly", "arn:aws:iam::aws:policy/Ec2Read", "ReadOnly EC2 access"),
                new IamPolicyDto("CloudWatchReadOnly", "arn:aws:iam::aws:policy/CwRead", "ReadOnly Monitoring")
            );
        }
    }

    public List<IamGroupDto> getIamGroups() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/iam/groups", IamGroupDto[].class));
        } catch (Exception e) {
            return List.of(
                new IamGroupDto("Administrators", 2),
                new IamGroupDto("Developers", 5),
                new IamGroupDto("Interns", 3)
            );
        }
    }

    // Lambda
    public List<LambdaDto> getLambdas() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/lambda/functions", LambdaDto[].class));
        } catch (Exception e) {
            return List.of(
                new LambdaDto(1L, "image-processor", "python3.9", "Active"),
                new LambdaDto(2L, "auth-trigger", "nodejs18.x", "Active"),
                new LambdaDto(3L, "cleanup-script", "go1.x", "Inactive")
            );
        }
    }

    public String invokeFunction(String name, String payload) {
        try {
            return restTemplate.postForObject(baseUrl + "/lambda/invoke/" + name, payload, String.class);
        } catch (Exception e) {
            return "{ \"status\": \"success\", \"message\": \"Mock execution of '" + name + "' complete.\", \"received_payload\": " + (payload.isEmpty() ? "null" : payload) + " }";
        }
    }

    // RDS
    private List<DatabaseDto> mockDatabases = new ArrayList<>(List.of(
        new DatabaseDto(1L, "prod-db", "MySQL 8.0", "available"),
        new DatabaseDto(2L, "staging-db", "PostgreSQL 14", "stopped")
    ));

    public List<DatabaseDto> getDatabases() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/database/instances", DatabaseDto[].class));
        } catch (Exception e) {
            return mockDatabases;
        }
    }

    public void stopDatabase(Long id) {
        mockDatabases.stream().filter(d -> d.getId().equals(id)).findFirst().ifPresent(d -> d.setStatus("stopped"));
    }

    // VPC
    private List<VpcDto> mockVpcs = new ArrayList<>(List.of(
        new VpcDto("vpc-0123456", "primary-vpc", "10.0.0.0/16", "available"),
        new VpcDto("vpc-7890123", "secondary-vpc", "192.168.0.0/24", "available")
    ));

    public List<VpcDto> getVpcs() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/vpc/list", VpcDto[].class));
        } catch (Exception e) {
            return mockVpcs;
        }
    }
    // Firewall & Security
    private List<SecurityGroupDto> mockSecurityGroups = new java.util.ArrayList<>(List.of(
        new SecurityGroupDto(1L, "default-sec-group", "Default group for HTTP/SSH", "vpc-0123456"),
        new SecurityGroupDto(2L, "db-sec-group", "Internal database access only", "vpc-0123456")
    ));

    public List<SecurityGroupDto> getSecurityGroups() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/firewall/groups", SecurityGroupDto[].class));
        } catch (Exception e) {
            return mockSecurityGroups;
        }
    }

    public void deleteSecurityGroup(Long id) {
        try {
            restTemplate.delete(baseUrl + "/firewall/groups/" + id);
        } catch (Exception e) {
            mockSecurityGroups.removeIf(g -> g.getId().equals(id));
        }
        addAuditLog("DeleteSecurityGroup", id.toString());
    }

    @SuppressWarnings("unchecked")
    public java.util.Map<String, Object> getBillingSummary() {
        try {
            return restTemplate.getForObject(baseUrl + "/billing/summary", java.util.Map.class);
        } catch (Exception e) {
            return java.util.Map.of(
                "totalCost", 145.50,
                "byService", java.util.Map.of(
                    "EC2", 85.20,
                    "S3", 12.30,
                    "RDS", 48.00
                )
            );
        }
    }

    // Networking Details (ELB & Subnets)
    private List<LoadBalancerDto> mockLbs = new java.util.ArrayList<>(List.of(
        new LoadBalancerDto("prod-web-lb", "prod-web-lb-123.minicloud.internal", 80, List.of(1L, 2L), "vpc-0123456", "active"),
        new LoadBalancerDto("staging-lb", "staging-lb-456.minicloud.internal", 8080, List.of(3L), "vpc-0123456", "active")
    ));

    public List<LoadBalancerDto> getLoadBalancers() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/loadbalancer/list", LoadBalancerDto[].class));
        } catch (Exception e) {
            return mockLbs;
        }
    }

    public List<SubnetDto> getSubnets() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/subnet/list", SubnetDto[].class));
        } catch (Exception e) {
            return List.of(
                new SubnetDto("subnet-0abc123", "vpc-0123456", "10.0.1.0/24", "us-east-1a", 251),
                new SubnetDto("subnet-0def456", "vpc-0123456", "10.0.2.0/24", "us-east-1b", 248),
                new SubnetDto("subnet-789ghij", "vpc-7890123", "192.168.1.0/24", "us-west-2a", 254)
            );
        }
    }
    // Messaging (SQS & SNS)
    private List<QueueDto> mockQueues = new java.util.ArrayList<>(List.of(
        new QueueDto("Order-Processing-Queue", "https://sqs.us-east-1.minicloud/orders", 15),
        new QueueDto("Email-Notification-Queue", "https://sqs.us-east-1.minicloud/email", 0)
    ));

    private List<TopicDto> mockTopics = new java.util.ArrayList<>(List.of(
        new TopicDto("System-Alerts", "arn:aws:sns:us-east-1:123:Alerts", 3),
        new TopicDto("User-Signups", "arn:aws:sns:us-east-1:123:Signups", 8)
    ));

    public List<QueueDto> getQueues() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/messaging/sqs/queues", QueueDto[].class));
        } catch (Exception e) {
            return mockQueues;
        }
    }

    public List<TopicDto> getTopics() {
        try {
            return List.of(restTemplate.getForObject(baseUrl + "/messaging/sns/topics", TopicDto[].class));
        } catch (Exception e) {
            return mockTopics;
        }
    }

    public void sendMessage(String queueName, String body) {
        try {
            restTemplate.postForLocation(baseUrl + "/messaging/sqs/send?queueName=" + queueName, body);
        } catch (Exception e) {
            mockQueues.stream().filter(q -> q.getName().equals(queueName)).findFirst()
                .ifPresent(q -> q.setMessagesAvailable(q.getMessagesAvailable() + 1));
        }
        addAuditLog("SqsSendMessage", queueName);
    }

    public void publishMessage(String topicName, String body) {
        try {
            restTemplate.postForLocation(baseUrl + "/messaging/sns/publish?topicName=" + topicName, body);
        } catch (Exception e) {
            // Simulation
        }
        addAuditLog("SnsPublishMessage", topicName);
    }
}
