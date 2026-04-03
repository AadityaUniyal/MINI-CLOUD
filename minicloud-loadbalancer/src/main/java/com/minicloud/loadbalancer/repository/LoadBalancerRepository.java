package com.minicloud.loadbalancer.repository;

import com.minicloud.loadbalancer.model.LoadBalancer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoadBalancerRepository extends JpaRepository<LoadBalancer, Long> {
    List<LoadBalancer> findByTenantId(String tenantId);
}
