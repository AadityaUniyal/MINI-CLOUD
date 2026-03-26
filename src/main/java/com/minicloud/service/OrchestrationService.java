package com.minicloud.service;

import com.minicloud.dto.DeployStackRequest;
import com.minicloud.dto.DeployStackResponse;
import com.minicloud.dto.ProvisionDbRequest;
import com.minicloud.model.ComputeInstance;
import com.minicloud.model.DatabaseInstance;
import com.minicloud.model.LoadBalancer;
import com.minicloud.model.Stack;
import com.minicloud.repository.StackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
public class OrchestrationService {

    private final ComputeService computeService;
    private final DatabaseService databaseService;
    private final LoadBalancerService loadBalancerService;
    private final StackRepository stackRepository;

    public OrchestrationService(ComputeService computeService, 
                                DatabaseService databaseService, 
                                LoadBalancerService loadBalancerService,
                                StackRepository stackRepository) {
        this.computeService = computeService;
        this.databaseService = databaseService;
        this.loadBalancerService = loadBalancerService;
        this.stackRepository = stackRepository;
    }

    /**
     * Week 3 Core: Orchestration Engine (One-Click Deploy).
     * Deploys a stack consisting of N Compute instances, a DB, and an LB.
     */
    @Transactional
    public DeployStackResponse deployFullStack(String owner, DeployStackRequest request) {
        // If template is provided, override request fields
        if (request.getTemplate() != null && !request.getTemplate().isEmpty()) {
            applyTemplate(request);
        }

        String stackId = UUID.randomUUID().toString();
        List<Long> instanceIds = new ArrayList<>();

        // 1. Provision Database
        ProvisionDbRequest dbReq = new ProvisionDbRequest();
        dbReq.setName(request.getStackName() + "-db");
        dbReq.setDbName(request.getDbName());
        dbReq.setRootPassword(request.getDbPassword());
        dbReq.setVpcId(request.getVpcId());
        dbReq.setSubnetId(request.getSubnetId());
        dbReq.setEngine("mysql"); // Default for stack

        DatabaseInstance db = databaseService.provisionDatabase(owner, dbReq);

        // 2. Provision Compute Instances
        for (int i = 1; i <= request.getInstanceCount(); i++) {
            ComputeInstance instance = computeService.launchInstance(
                owner, 
                request.getStackName() + "-app-" + i,
                request.getVpcId(),
                request.getSubnetId(),
                null); // No AMI support in Stack Request yet
            instanceIds.add(instance.getId());
        }

        // 3. Provision Load Balancer
        LoadBalancer lb = loadBalancerService.createLoadBalancer(owner, 
            request.getStackName() + "-lb", 
            80, 
            instanceIds,
            request.getVpcId());

        // 4. Persist Stack Metadata
        Stack stack = Stack.builder()
                .stackId(stackId)
                .name(request.getStackName())
                .owner(owner)
                .databaseId(db.getId())
                .loadBalancerId(lb.getId())
                .computeInstanceIds(instanceIds)
                .createdAt(LocalDateTime.now())
                .build();
        stackRepository.save(stack);

        return DeployStackResponse.builder()
                .stackId(stackId)
                .status("DEPLOYED")
                .loadBalancerUrl("http://localhost:" + lb.getPublicPort())
                .message("Stack " + request.getStackName() + " deployed successfully with " + request.getInstanceCount() + " instances.")
                .build();
    }

    public List<Stack> listStacks(String owner) {
        return stackRepository.findByOwner(owner);
    }

    @Transactional
    public void deleteStack(String owner, String stackId) {
        stackRepository.findByStackId(stackId).ifPresent(stack -> {
            if (!stack.getOwner().equals(owner)) {
                throw new RuntimeException("Unauthorized to delete this stack");
            }

            // 1. Terminate Load Balancer
            loadBalancerService.getLoadBalancerByName(stack.getName() + "-lb")
                    .ifPresent(lb -> loadBalancerService.deleteLoadBalancer(lb.getName()));

            // 2. Terminate Database
            databaseService.terminateDatabase(stack.getName() + "-db");

            // 3. Terminate Instances
            for (int i = 1; i <= stack.getComputeInstanceIds().size(); i++) {
                computeService.terminateInstance(stack.getName() + "-app-" + i);
            }

            // 4. Remove Stack Record
            stackRepository.delete(stack);
        });
    }

    public List<String> getStackContainerIds(String stackId) {
        return stackRepository.findByStackId(stackId).map(stack -> {
            List<String> ids = new ArrayList<>();
            // Get DB container ID
            databaseService.getDatabasesByOwner(stack.getOwner()).stream()
                    .filter(db -> db.getName().equals(stack.getName() + "-db"))
                    .findFirst()
                    .ifPresent(db -> ids.add(db.getContainerId()));
            
            // Get Compute container IDs
            computeService.getInstancesByOwner(stack.getOwner()).stream()
                    .filter(inst -> inst.getName().startsWith(stack.getName() + "-app-"))
                    .forEach(inst -> ids.add(inst.getContainerId()));
            
            return ids;
        }).orElse(new ArrayList<>());
    }

    private void applyTemplate(DeployStackRequest request) {
        switch (request.getTemplate().toUpperCase()) {
            case "JAVA_WEB_APP":
                request.setInstanceCount(1);
                break;
            case "HA_WEB_CLUSTER":
                request.setInstanceCount(3);
                break;
            case "MICROSERVICE_STACK":
                request.setInstanceCount(2);
                break;
            default:
                // Keep original request values
                break;
        }
    }

    public Map<String, String> getAvailableTemplates() {
        Map<String, String> templates = new HashMap<>();
        templates.put("JAVA_WEB_APP", "Standard Java Web App (1 Tomcat, 1 MySQL, 1 Load Balancer)");
        templates.put("HA_WEB_CLUSTER", "High Availability Cluster (3 Tomcats, 1 MySQL, 1 Load Balancer)");
        templates.put("MICROSERVICE_STACK", "Microservice Stack (2 Tomcats, 1 Database, 1 Load Balancer)");
        return templates;
    }
}
