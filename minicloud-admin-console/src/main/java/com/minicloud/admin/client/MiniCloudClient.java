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
        return List.of();
    }

    public List<DatabaseDto> getDatabases() {
        return List.of();
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
        return List.of();
    }

    public void uploadFile(String bucket, String name) {
        System.out.println("Uploading " + name + " to " + bucket);
    }

    public void deleteFile(String bucket, String name) {
        System.out.println("Deleting " + name + " from " + bucket);
    }

    // --- Networking (VPC, Subnet, SecurityGroups, ELB) ---
    public List<VpcDto> getVpcs() {
        return List.of();
    }

    public List<SubnetDto> getSubnets() {
        return List.of();
    }

    public List<SecurityGroupDto> getSecurityGroups() {
        return List.of();
    }

    public void deleteSecurityGroup(Long id) {
        System.out.println("Deleting security group: " + id);
    }

    public List<LoadBalancerDto> getLoadBalancers() {
        return List.of();
    }

    // --- IAM ---
    public List<IamDto> getIamUsers() {
        return List.of();
    }

    public List<IamGroupDto> getIamGroups() {
        return List.of();
    }

    public List<IamRoleDto> getIamRoles() {
        return List.of();
    }

    public List<IamPolicyDto> getIamPolicies() {
        return List.of();
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
        return List.of();
    }

    public String invokeFunction(String functionName, String payload) {
        return "Invoked " + functionName;
    }

    public List<AuditLogDto> getAuditLogs() {
        return List.of();
    }

    public List<QueueDto> getQueues() {
        return List.of();
    }

    public List<TopicDto> getTopics() {
        return List.of();
    }

    public void sendMessage(String queue, String body) {
        System.out.println("Sending SQS to " + queue + ": " + body);
    }

    public void publishMessage(String topic, String body) {
        System.out.println("Publishing SNS to " + topic + ": " + body);
    }

    // --- Billing ---
    public Map<String, Object> getBillingSummary() {
        Map<String, Object> mock = new HashMap<>();
        mock.put("totalCost", 125.50);
        mock.put("byService", Map.of("EC2", 45.0, "S3", 12.0, "RDS", 68.5));
        return mock;
    }
}
