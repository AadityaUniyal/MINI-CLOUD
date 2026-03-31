package com.minicloud.service;

import com.minicloud.model.ComputeInstance;
import com.minicloud.repository.AuditLogRepository;
import com.minicloud.repository.ComputeInstanceRepository;
import com.minicloud.repository.UserRepository;
import com.minicloud.service.storage.VolumeService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComputeService {

    private final DockerService dockerService;
    private final ComputeInstanceRepository computeInstanceRepository;
    private final BillingService billingService;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final VolumeService volumeService;
    private static int NEXT_PORT = 8081;

    public ComputeService(DockerService dockerService, 
                          ComputeInstanceRepository computeInstanceRepository,
                          BillingService billingService,
                          AuditLogRepository auditLogRepository,
                          UserRepository userRepository,
                          VolumeService volumeService) {
        this.dockerService = dockerService;
        this.computeInstanceRepository = computeInstanceRepository;
        this.billingService = billingService;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.volumeService = volumeService;
        initializeNextPort();
    }

    private void initializeNextPort() {
        computeInstanceRepository.findAll().stream()
                .mapToInt(inst -> inst.getHostPort() != null ? inst.getHostPort() : 8080)
                .max()
                .ifPresent(maxPort -> NEXT_PORT = maxPort + 1);
    }

    /**
     * Week 1 Core: Launch a container and track it in H2.
     */
    public ComputeInstance launchInstance(String owner, String name, String vpcId, String subnetId, String amiId) {
        int hostPort;
        synchronized (this) {
            hostPort = NEXT_PORT++;
        }
        String containerId = dockerService.launchTomcatContainer(name, hostPort);
        
        String region = "us-east-1";
        String az = region + (char)('a' + (int)(Math.random() * 3));
        String instanceType = hostPort % 2 == 0 ? "t2.micro" : "t2.small";
        String privateIp = "172.31." + (int)(Math.random() * 255) + "." + (int)(Math.random() * 255);

        ComputeInstance instance = ComputeInstance.builder()
                .name(name)
                .containerId(containerId)
                .image(amiId != null ? amiId : "tomcat:latest")
                .amiId(amiId)
                .status("RUNNING")
                .owner(owner)
                .instanceType(instanceType)
                .region(region)
                .availabilityZone(az)
                .publicIp("54." + (int)(Math.random() * 255) + "." + (int)(Math.random() * 255) + "." + (int)(Math.random() * 255))
                .privateIp(privateIp)
                .vpcId(vpcId)
                .subnetId(subnetId)
                .hostPort(hostPort)
                .createdAt(LocalDateTime.now())
                .build();
        
        ComputeInstance savedInstance = computeInstanceRepository.save(instance);

        // Audit Log & Billing
        userRepository.findByUsername(owner).ifPresent(user -> {
            auditLogRepository.save(com.minicloud.model.AuditLog.builder()
                    .user(user)
                    .action("LAUNCH_COMPUTE")
                    .resourceId(containerId)
                    .timestamp(LocalDateTime.now())
                    .details("Launched Tomcat instance: " + name)
                    .build());
            billingService.startBilling(user, containerId, "COMPUTE");
        });

        return savedInstance;
    }

    public List<ComputeInstance> getInstancesByOwner(String owner) {
        return computeInstanceRepository.findByOwner(owner);
    }

    public List<ComputeInstance> getAllInstances() {
        return computeInstanceRepository.findAll();
    }

    public void terminateInstance(Long id, String owner) {
        computeInstanceRepository.findById(id).ifPresent(instance -> {
            if (instance.getOwner().equals(owner)) {
                dockerService.stopContainer(instance.getContainerId());
                dockerService.removeContainer(instance.getContainerId());
                instance.setStatus("TERMINATED");
                computeInstanceRepository.save(instance);

                // Phase 2: Cleanup EBS Volumes
                handleVolumeCleanup(instance.getContainerId(), owner);

                // Audit Log & Billing
                userRepository.findByUsername(owner).ifPresent(user -> {
                    auditLogRepository.save(com.minicloud.model.AuditLog.builder()
                            .user(user)
                            .action("TERMINATE_COMPUTE")
                            .resourceId(instance.getContainerId())
                            .timestamp(LocalDateTime.now())
                            .details("Terminated instance: " + instance.getName())
                            .build());
                    billingService.stopBilling(instance.getContainerId());
                });
            }
        });
    }

    public void terminateInstance(String name) {
        computeInstanceRepository.findByName(name).ifPresent(instance -> {
            dockerService.stopContainer(instance.getContainerId());
            dockerService.removeContainer(instance.getContainerId());
            instance.setStatus("TERMINATED");
            computeInstanceRepository.save(instance);

            // Phase 2: Cleanup EBS Volumes
            handleVolumeCleanup(instance.getContainerId(), instance.getOwner());

            // Audit Log & Billing
            userRepository.findByUsername(instance.getOwner()).ifPresent(user -> {
                auditLogRepository.save(com.minicloud.model.AuditLog.builder()
                        .user(user)
                        .action("TERMINATE_COMPUTE")
                        .resourceId(instance.getContainerId())
                        .timestamp(LocalDateTime.now())
                        .details("Terminated instance: " + name)
                        .build());
                billingService.stopBilling(instance.getContainerId());
            });
        });
    }

    public void stopInstance(Long id, String owner) {
        computeInstanceRepository.findById(id).ifPresent(instance -> {
            if (instance.getOwner().equals(owner)) {
                dockerService.stopContainer(instance.getContainerId());
                instance.setStatus("STOPPED");
                computeInstanceRepository.save(instance);

                userRepository.findByUsername(owner).ifPresent(user -> {
                    auditLogRepository.save(com.minicloud.model.AuditLog.builder()
                            .user(user)
                            .action("STOP_COMPUTE")
                            .resourceId(instance.getContainerId())
                            .timestamp(LocalDateTime.now())
                            .details("Stopped instance: " + instance.getName())
                            .build());
                    billingService.stopBilling(instance.getContainerId());
                });
            }
        });
    }

    public void startInstance(Long id, String owner) {
        computeInstanceRepository.findById(id).ifPresent(instance -> {
            if (instance.getOwner().equals(owner)) {
                dockerService.startContainer(instance.getContainerId());
                instance.setStatus("RUNNING");
                computeInstanceRepository.save(instance);

                userRepository.findByUsername(owner).ifPresent(user -> {
                    auditLogRepository.save(com.minicloud.model.AuditLog.builder()
                            .user(user)
                            .action("START_COMPUTE")
                            .resourceId(instance.getContainerId())
                            .timestamp(LocalDateTime.now())
                            .details("Started instance: " + instance.getName())
                            .build());
                    billingService.startBilling(user, instance.getContainerId(), "COMPUTE");
                });
            }
        });
    }

    private void handleVolumeCleanup(String instanceId, String owner) {
        volumeService.getVolumesForInstance(instanceId).forEach(volume -> {
            if (volume.isDeleteOnTermination()) {
                volumeService.deleteVolume(owner, volume.getVolumeId());
            } else {
                volumeService.detachVolume(owner, volume.getVolumeId());
            }
        });
    }
}
