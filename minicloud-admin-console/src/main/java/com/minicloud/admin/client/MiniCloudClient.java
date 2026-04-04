package com.minicloud.admin.client;

import com.minicloud.common.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class MiniCloudClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GATEWAY_URL = "http://localhost:8080/api/v1";

    // --- Compute (Instances) ---
    public List<InstanceDto> getInstances() {
        return restTemplate.exchange(
                GATEWAY_URL + "/compute/instances",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<InstanceDto>>() {}
        ).getBody();
    }

    public void launchInstance(String ami, String type, String subnet) {
        System.out.println("Launching " + ami + " on " + subnet);
    }

    public void startInstance(Long id) {
        System.out.println("Starting instance: " + id);
    }

    public void stopInstance(Long id) {
        System.out.println("Stopping instance: " + id);
    }

    public void terminateInstance(Long id) {
        System.out.println("Terminating instance: " + id);
    }

    // --- Database (Dynamo, RDS) ---
    public List<DynamoTableDto> getDynamoTables() {
        return restTemplate.exchange(
                GATEWAY_URL + "/database/dynamo/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DynamoTableDto>>() {}
        ).getBody();
    }

    public List<DynamoItemDto> getDynamoItems(String tableName) {
        return restTemplate.exchange(
                GATEWAY_URL + "/database/dynamo/items?table=" + tableName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DynamoItemDto>>() {}
        ).getBody();
    }

    public List<DatabaseDto> getDatabases() {
        return restTemplate.exchange(
                GATEWAY_URL + "/database/instances",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DatabaseDto>>() {}
        ).getBody();
    }

    public void stopDatabase(Long id) {
        System.out.println("Stopping database: " + id);
    }

    public void startDatabase(Long id) {
        System.out.println("Starting database: " + id);
    }

    // --- Storage (S3) ---
    public List<BucketDto> getBuckets() {
        return restTemplate.exchange(
                GATEWAY_URL + "/storage/buckets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BucketDto>>() {}
        ).getBody();
    }

    public List<FileDto> getFiles(String bucket) {
        return restTemplate.exchange(
                GATEWAY_URL + "/storage/files?bucket=" + bucket,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FileDto>>() {}
        ).getBody();
    }

    public void uploadFile(String bucket, String name) {
        System.out.println("Uploading " + name + " to " + bucket);
    }

    public void deleteFile(String bucket, String name) {
        System.out.println("Deleting " + name + " from " + bucket);
    }

    // --- Networking (VPC, Subnet, SecurityGroups, ELB) ---
    public List<VpcDto> getVpcs() {
        return restTemplate.exchange(
                GATEWAY_URL + "/networking/vpcs",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<VpcDto>>() {}
        ).getBody();
    }

    public List<SubnetDto> getSubnets() {
        return restTemplate.exchange(
                GATEWAY_URL + "/networking/subnets",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SubnetDto>>() {}
        ).getBody();
    }

    public List<SecurityGroupDto> getSecurityGroups() {
        return restTemplate.exchange(
                GATEWAY_URL + "/networking/security-groups",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SecurityGroupDto>>() {}
        ).getBody();
    }

    public void deleteSecurityGroup(Long id) {
        System.out.println("Deleting security group: " + id);
    }

    public List<LoadBalancerDto> getLoadBalancers() {
        return restTemplate.exchange(
                GATEWAY_URL + "/networking/load-balancers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LoadBalancerDto>>() {}
        ).getBody();
    }

    // --- IAM ---
    public List<IamDto> getIamUsers() {
        return restTemplate.exchange(
                GATEWAY_URL + "/iam/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IamDto>>() {}
        ).getBody();
    }

    public List<IamGroupDto> getIamGroups() {
        return restTemplate.exchange(
                GATEWAY_URL + "/iam/groups",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IamGroupDto>>() {}
        ).getBody();
    }

    public List<IamRoleDto> getIamRoles() {
        return restTemplate.exchange(
                GATEWAY_URL + "/iam/roles",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IamRoleDto>>() {}
        ).getBody();
    }

    public List<IamPolicyDto> getIamPolicies() {
        return restTemplate.exchange(
                GATEWAY_URL + "/iam/policies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IamPolicyDto>>() {}
        ).getBody();
    }

    // --- Advanced (Lambda, MQ, CloudTrail, Monitoring, Backup) ---
    public List<BackupRecordDto> getBackupHistory(String serviceName) {
        String url = GATEWAY_URL + "/backups/list?service=" + (serviceName != null ? serviceName : "");
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BackupRecordDto>>() {}
        ).getBody();
    }

    public BackupRecordDto triggerBackup(String service, String engine, String db, String user, String pass) {
        String url = GATEWAY_URL + "/backups/trigger?service=" + service + "&engine=" + engine + "&db=" + db + "&user=" + user + "&pass=" + pass;
        return restTemplate.postForObject(url, null, BackupRecordDto.class);
    }

    public List<LambdaDto> getLambdas() {
        return restTemplate.exchange(
                GATEWAY_URL + "/compute/lambdas",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LambdaDto>>() {}
        ).getBody();
    }

    public String invokeFunction(String functionName, String payload) {
        return "Invoked " + functionName;
    }

    public List<AuditLogDto> getAuditLogs() {
        return restTemplate.exchange(
                GATEWAY_URL + "/audit/logs",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AuditLogDto>>() {}
        ).getBody();
    }

    public List<QueueDto> getQueues() {
        return restTemplate.exchange(
                GATEWAY_URL + "/messaging/queues",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<QueueDto>>() {}
        ).getBody();
    }

    public List<TopicDto> getTopics() {
        return restTemplate.exchange(
                GATEWAY_URL + "/messaging/topics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TopicDto>>() {}
        ).getBody();
    }

    public void sendMessage(String queue, String body) {
        System.out.println("Sending SQS to " + queue + ": " + body);
    }

    public void publishMessage(String topic, String body) {
        System.out.println("Publishing SNS to " + topic + ": " + body);
    }

    // --- Billing ---
    public List<BillingRecordDto> getBillingRecords() {
        return List.of(
            BillingRecordDto.builder().resourceType("EC2").cost(45.0).build(),
            BillingRecordDto.builder().resourceType("S3").cost(12.0).build(),
            BillingRecordDto.builder().resourceType("RDS").cost(68.5).build()
        );
    }

    public Map<String, Object> getBillingSummary() {
        Map<String, Object> mock = new HashMap<>();
        mock.put("totalCost", 125.50);
        mock.put("byService", Map.of("EC2", 45.0, "S3", 12.0, "RDS", 68.5));
        return mock;
    }
}
