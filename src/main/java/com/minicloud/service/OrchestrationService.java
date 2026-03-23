package com.minicloud.service;

import com.minicloud.dto.DeployStackRequest;
import com.minicloud.dto.DeployStackResponse;
import com.minicloud.model.ComputeInstance;
import com.minicloud.model.DatabaseInstance;
import com.minicloud.model.LoadBalancer;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrchestrationService {

    private final ComputeService computeService;
    private final DatabaseService databaseService;
    private final LoadBalancerService loadBalancerService;

    public OrchestrationService(ComputeService computeService, DatabaseService databaseService, LoadBalancerService loadBalancerService) {
        this.computeService = computeService;
        this.databaseService = databaseService;
        this.loadBalancerService = loadBalancerService;
    }

    /**
     * Week 3 Core: Orchestration Engine (One-Click Deploy).
     * Deploys a stack consisting of N Compute instances, a DB, and an LB.
     */
    public DeployStackResponse deployFullStack(String owner, DeployStackRequest request) {
        String stackId = UUID.randomUUID().toString();
        List<Long> instanceIds = new ArrayList<>();

        // 1. Provision Database
        DatabaseInstance db = databaseService.provisionDatabase(owner, 
            request.getStackName() + "-db", 
            request.getDbName(), 
            request.getDbPassword());

        // 2. Provision Compute Instances
        for (int i = 1; i <= request.getInstanceCount(); i++) {
            ComputeInstance instance = computeService.launchInstance(owner, request.getStackName() + "-app-" + i);
            instanceIds.add(instance.getId());
        }

        // 3. Provision Load Balancer
        LoadBalancer lb = loadBalancerService.createLoadBalancer(owner, 
            request.getStackName() + "-lb", 
            80, 
            instanceIds);

        return DeployStackResponse.builder()
                .stackId(stackId)
                .status("DEPLOYED")
                .loadBalancerUrl("http://localhost:" + lb.getPublicPort())
                .message("Stack " + request.getStackName() + " deployed successfully with " + request.getInstanceCount() + " instances.")
                .build();
    }
}
