package com.minicloud.service;

import com.minicloud.model.ComputeInstance;
import com.minicloud.repository.ComputeInstanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HealthCheckService {

    private final DockerService dockerService;
    private final ComputeInstanceRepository computeInstanceRepository;

    public HealthCheckService(DockerService dockerService, ComputeInstanceRepository computeInstanceRepository) {
        this.dockerService = dockerService;
        this.computeInstanceRepository = computeInstanceRepository;
    }

    /**
     * Week 3 Core: Background Health Daemon.
     * Polls Docker stats every 10 seconds to detect crashed containers.
     */
    @Scheduled(fixedRate = 10000)
    public void performHealthChecks() {
        List<ComputeInstance> instances = computeInstanceRepository.findAll();
        
        for (ComputeInstance instance : instances) {
            String currentStatus = dockerService.getContainerStatus(instance.getContainerId());
            
            if ("exited".equalsIgnoreCase(currentStatus) || "stopped".equalsIgnoreCase(currentStatus)) {
                System.out.println("HealthCheck: Instance " + instance.getName() + " is UNHEALTHY (Status: " + currentStatus + ")");
                
                // Auto-Healing: Attempt to restart the container
                try {
                    System.out.println("Auto-Healing: Restarting " + instance.getName() + "...");
                    dockerService.restartContainer(instance.getContainerId());
                    instance.setStatus("RUNNING");
                } catch (Exception e) {
                    System.err.println("Auto-Healing Failed for " + instance.getName() + ": " + e.getMessage());
                    instance.setStatus("CRITICAL_FAILURE");
                }
                computeInstanceRepository.save(instance);
            } else if ("running".equalsIgnoreCase(currentStatus)) {
                if (!"RUNNING".equalsIgnoreCase(instance.getStatus())) {
                    instance.setStatus("RUNNING");
                    computeInstanceRepository.save(instance);
                }
            }
        }
    }
}
