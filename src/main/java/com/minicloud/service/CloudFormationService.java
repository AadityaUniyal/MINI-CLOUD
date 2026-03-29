package com.minicloud.service;

import com.minicloud.model.CfStack;
import com.minicloud.repository.CfStackRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CloudFormationService {

    private final CfStackRepository stackRepository;
    private final ComputeService computeService;
    private final BucketService bucketService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CloudFormationService(CfStackRepository stackRepository, 
                                 ComputeService computeService, 
                                 BucketService bucketService) {
        this.stackRepository = stackRepository;
        this.computeService = computeService;
        this.bucketService = bucketService;
    }

    @Transactional
    public CfStack createStack(String owner, String stackName, String templateJson) {
        CfStack stack = new CfStack();
        stack.setStackName(stackName);
        stack.setTemplateJson(templateJson);
        stack.setOwner(owner);
        stack.setStatus("CREATE_COMPLETE"); // Mock: instant creation
        stack.setCreatedAt(LocalDateTime.now());
        
        try {
            JsonNode root = objectMapper.readTree(templateJson);
            JsonNode resources = root.get("Resources");
            if (resources != null) {
                resources.fields().forEachRemaining(entry -> {
                    String logicalId = entry.getKey();
                    JsonNode resource = entry.getValue();
                    String type = resource.get("Type").asText();
                    
                    if ("AWS::EC2::Instance".equals(type)) {
                        String name = resource.get("Properties").get("Name").asText();
                        com.minicloud.model.ComputeInstance instance = computeService.launchInstance(owner, name, "vpc-default", "subnet-default", null);
                        stack.getResourceMapping().put(logicalId, instance.getContainerId());
                    } else if ("AWS::S3::Bucket".equals(type)) {
                        String name = resource.get("Properties").get("BucketName").asText();
                        try {
                            bucketService.createBucket(name, owner, "us-east-1");
                        } catch (java.io.IOException e) {
                            throw new RuntimeException("Failed to create bucket: " + name, e);
                        }
                        stack.getResourceMapping().put(logicalId, name);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse template: " + e.getMessage());
        }

        return stackRepository.save(stack);
    }

    public List<CfStack> getStacksByOwner(String owner) {
        return stackRepository.findByOwner(owner);
    }

    public void deleteStack(String stackName) {
        stackRepository.findByStackName(stackName).ifPresent(stackRepository::delete);
    }
}
