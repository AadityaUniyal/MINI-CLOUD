package com.minicloud.backend.repository;

import com.minicloud.backend.model.LoadBalancer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadBalancerRepository extends JpaRepository<LoadBalancer, Long> {
    Optional<LoadBalancer> findByName(String name);
    List<LoadBalancer> findByOwner(String owner);
}
