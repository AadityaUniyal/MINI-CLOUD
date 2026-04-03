package com.minicloud.backend.service;

import com.minicloud.backend.model.AuditLog;
import com.minicloud.backend.model.ComputeInstance;
import com.minicloud.backend.repository.AuditLogRepository;
import com.minicloud.backend.repository.ComputeInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);
    private final DockerService dockerService;
    private final ComputeInstanceRepository computeInstanceRepository;
    private final AuditLogRepository auditLogRepository;

    public HealthCheckService(DockerService dockerService, 
                            ComputeInstanceRepository computeInstanceRepository,
                            AuditLogRepository auditLogRepository) {
        this.dockerService = dockerService;
        this.computeInstanceRepository = computeInstanceRepository;
        this.auditLogRepository = auditLogRepository;
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
            logger.debug("HealthCheck: Checking instance {} (ID: {}), Current Docker Status: {}", 
                instance.getName(), instance.getContainerId(), currentStatus);
            
            if (isUnhealthy(currentStatus)) {
                logger.warn("HealthCheck: Instance {} is UNHEALTHY (Status: {})", instance.getName(), currentStatus);
                
                // Auto-Healing: Attempt to restart the container
                try {
                    logger.info("Auto-Healing: Attempting to restart {}...", instance.getName());
                    dockerService.restartContainer(instance.getContainerId());
                    instance.setStatus("RUNNING");
                    logger.info("Auto-Healing: Successfully restarted {}.", instance.getName());
                    
                    // Log Auto-Healing Event for UI Timeline
                    AuditLog log = AuditLog.builder()
                            .username(instance.getOwner())
                            .action("AUTO_HEAL")
                            .resourceId(instance.getName())
                            .timestamp(LocalDateTime.now())
                            .details("Critically unhealthy container restarted automatically by MiniCloud Healer.")
                            .build();
                    auditLogRepository.save(log);

                } catch (Exception e) {
                    logger.error("Auto-Healing: Failed for {}: {}", instance.getName(), e.getMessage());
                    instance.setStatus("CRITICAL_FAILURE");
                }
                computeInstanceRepository.save(instance);
            } else if ("running".equalsIgnoreCase(currentStatus)) {
                if (!"RUNNING".equalsIgnoreCase(instance.getStatus())) {
                    instance.setStatus("RUNNING");
                    computeInstanceRepository.save(instance);
                    logger.info("HealthCheck: Instance {} is back to RUNNING.", instance.getName());
                }
            }
        }
    }

    private boolean isUnhealthy(String status) {
        return "exited".equalsIgnoreCase(status) || 
               "stopped".equalsIgnoreCase(status) || 
               "dead".equalsIgnoreCase(status) ||
               "UNKNOWN".equalsIgnoreCase(status);
    }
}
