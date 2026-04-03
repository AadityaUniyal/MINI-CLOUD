package com.minicloud.backend.service;

import com.minicloud.backend.model.LoadBalancer;
import com.minicloud.backend.model.ComputeInstance;
import com.minicloud.backend.repository.LoadBalancerRepository;
import com.minicloud.backend.repository.ComputeInstanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoadBalancerService {

    private final LoadBalancerRepository loadBalancerRepository;
    private final ComputeInstanceRepository computeInstanceRepository;

    public LoadBalancerService(LoadBalancerRepository loadBalancerRepository, ComputeInstanceRepository computeInstanceRepository) {
        this.loadBalancerRepository = loadBalancerRepository;
        this.computeInstanceRepository = computeInstanceRepository;
    }

    /**
     * Week 3 Core: Create a Load Balancer and link it to instances.
     */
    public LoadBalancer createLoadBalancer(String owner, String name, int publicPort, List<Long> instanceIds, String vpcId) {
        List<ComputeInstance> targets = computeInstanceRepository.findAllById(instanceIds);
        
        LoadBalancer lb = LoadBalancer.builder()
                .name(name)
                .publicPort(publicPort)
                .algorithm("ROUND_ROBIN")
                .protocol("HTTP")
                .healthCheckPath("/health")
                .healthCheckInterval(30)
                .status("ACTIVE")
                .owner(owner)
                .vpcId(vpcId)
                .targetInstances(targets)
                .build();

        return loadBalancerRepository.save(lb);
    }

    @Scheduled(fixedRate = 60000)
    public void performHealthChecks() {
        loadBalancerRepository.findAll().forEach(lb -> {
            // Mock health check logic
            System.out.println("Health checking targets for LB: " + lb.getName());
            lb.getTargetInstances().forEach(inst -> {
                if ("RUNNING".equals(inst.getStatus())) {
                    // System.out.println("Target " + inst.getName() + " is HEALTHY");
                }
            });
        });
    }

    public List<LoadBalancer> getAllLoadBalancers() {
        return loadBalancerRepository.findAll();
    }

    public void addInstanceToLoadBalancer(String lbName, Long instanceId) {
        loadBalancerRepository.findByName(lbName).ifPresent(lb -> {
            computeInstanceRepository.findById(instanceId).ifPresent(instance -> {
                lb.getTargetInstances().add(instance);
                loadBalancerRepository.save(lb);
            });
        });
    }

    public void deleteLoadBalancer(String name) {
        loadBalancerRepository.findByName(name).ifPresent(loadBalancerRepository::delete);
    }

    public Optional<LoadBalancer> getLoadBalancerByName(String name) {
        return loadBalancerRepository.findByName(name);
    }
}
