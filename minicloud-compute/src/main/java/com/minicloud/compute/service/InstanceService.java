package com.minicloud.compute.service;

import com.minicloud.compute.model.Image;
import com.minicloud.compute.model.Instance;
import com.minicloud.compute.model.InstanceType;
import com.minicloud.compute.repository.ImageRepository;
import com.minicloud.compute.repository.InstanceRepository;
import com.minicloud.compute.repository.InstanceTypeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InstanceService {

    private final InstanceRepository instanceRepository;
    private final ImageRepository imageRepository;
    private final InstanceTypeRepository instanceTypeRepository;
    private final DockerService dockerService;

    public InstanceService(InstanceRepository instanceRepository, 
                           ImageRepository imageRepository, 
                           InstanceTypeRepository instanceTypeRepository, 
                           DockerService dockerService) {
        this.instanceRepository = instanceRepository;
        this.imageRepository = imageRepository;
        this.instanceTypeRepository = instanceTypeRepository;
        this.dockerService = dockerService;
    }

    public List<Instance> getAllInstances(String tenantId) {
        return instanceRepository.findByTenantId(tenantId);
    }

    public Instance createInstance(String tenantId, String name, String typeName, String imageId) {
        Image image = imageRepository.findByImageId(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found: " + imageId));
        
        instanceTypeRepository.findByName(typeName)
                .orElseGet(() -> instanceTypeRepository.save(InstanceType.builder()
                        .name(typeName).vcpu(1).memoryGb(1).diskGb(10).pricePerHour(0.01).build()));

        String instanceId = "i-" + UUID.randomUUID().toString().substring(0, 8);
        String containerId = dockerService.createInstance(image.getDockerImage(), instanceId);

        Instance instance = Instance.builder()
                .instanceId(instanceId)
                .tenantId(tenantId)
                .name(name)
                .type(typeName)
                .imageId(imageId)
                .containerId(containerId)
                .state("STOPPED")
                .createdAt(LocalDateTime.now())
                .build();
        
        return instanceRepository.save(instance);
    }

    public void startInstance(String instanceId) {
        Instance instance = instanceRepository.findByInstanceId(instanceId)
                .orElseThrow(() -> new RuntimeException("Instance not found"));
        
        dockerService.startInstance(instance.getContainerId());
        instance.setState("RUNNING");
        instance.setIpAddress(dockerService.getIpAddress(instance.getContainerId()));
        instanceRepository.save(instance);
    }

    public void stopInstance(String instanceId) {
        Instance instance = instanceRepository.findByInstanceId(instanceId)
                .orElseThrow(() -> new RuntimeException("Instance not found"));
        
        dockerService.stopInstance(instance.getContainerId());
        instance.setState("STOPPED");
        instanceRepository.save(instance);
    }

    public void terminateInstance(String instanceId) {
        Instance instance = instanceRepository.findByInstanceId(instanceId)
                .orElseThrow(() -> new RuntimeException("Instance not found"));
        
        dockerService.terminateInstance(instance.getContainerId());
        instance.setState("TERMINATED");
        instanceRepository.save(instance);
    }
}
