package com.minicloud.service;

import com.minicloud.model.LoadBalancer;
import com.minicloud.model.ComputeInstance;
import com.minicloud.repository.LoadBalancerRepository;
import com.minicloud.repository.ComputeInstanceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public LoadBalancer createLoadBalancer(String owner, String name, int publicPort, List<Long> instanceIds) {
        List<ComputeInstance> targets = computeInstanceRepository.findAllById(instanceIds);
        
        LoadBalancer lb = LoadBalancer.builder()
                .name(name)
                .publicPort(publicPort)
                .algorithm("ROUND_ROBIN")
                .status("ACTIVE")
                .owner(owner)
                .targetInstances(targets)
                .build();

        return loadBalancerRepository.save(lb);
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
}
