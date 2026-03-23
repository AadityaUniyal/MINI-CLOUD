package com.minicloud.repository;

import com.minicloud.model.LoadBalancer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface LoadBalancerRepository extends JpaRepository<LoadBalancer, Long> {
    Optional<LoadBalancer> findByName(String name);
    List<LoadBalancer> findByOwner(String owner);
}
