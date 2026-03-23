package com.minicloud.service;

import com.minicloud.model.ComputeInstance;
import com.minicloud.repository.ComputeInstanceRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComputeService {

    private final DockerService dockerService;
    private final ComputeInstanceRepository computeInstanceRepository;

    public ComputeService(DockerService dockerService, ComputeInstanceRepository computeInstanceRepository) {
        this.dockerService = dockerService;
        this.computeInstanceRepository = computeInstanceRepository;
    }

    /**
     * Week 1 Core: Launch a container and track it in H2.
     */
    public ComputeInstance launchInstance(String owner, String name) {
        String containerId = dockerService.launchTomcatContainer(name);
        
        ComputeInstance instance = ComputeInstance.builder()
                .name(name)
                .containerId(containerId)
                .image("tomcat:latest")
                .status("RUNNING")
                .owner(owner)
                .publicIp("localhost:8080")
                .createdAt(LocalDateTime.now())
                .build();
        
        return computeInstanceRepository.save(instance);
    }

    public List<ComputeInstance> getAllInstances() {
        return computeInstanceRepository.findAll();
    }

    public void terminateInstance(String name) {
        computeInstanceRepository.findByName(name).ifPresent(instance -> {
            dockerService.stopContainer(instance.getContainerId());
            dockerService.removeContainer(instance.getContainerId());
            instance.setStatus("TERMINATED");
            computeInstanceRepository.save(instance);
        });
    }
}
