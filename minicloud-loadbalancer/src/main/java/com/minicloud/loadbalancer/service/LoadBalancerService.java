package com.minicloud.loadbalancer.service;

import com.minicloud.loadbalancer.model.LoadBalancer;
import com.minicloud.loadbalancer.repository.LoadBalancerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoadBalancerService {

    private final LoadBalancerRepository loadBalancerRepository;

    public LoadBalancerService(LoadBalancerRepository loadBalancerRepository) {
        this.loadBalancerRepository = loadBalancerRepository;
    }

    public LoadBalancer createLoadBalancer(String tenantId, String name, String type) {
        LoadBalancer lb = LoadBalancer.builder()
                .tenantId(tenantId)
                .name(name)
                .type(type)
                .ipAddress("10.0.0." + (int)(Math.random() * 255))
                .status("ACTIVE")
                .build();
        return loadBalancerRepository.save(lb);
    }

    public List<LoadBalancer> getTenantLoadBalancers(String tenantId) {
        return loadBalancerRepository.findByTenantId(tenantId);
    }
}
