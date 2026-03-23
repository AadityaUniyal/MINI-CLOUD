package com.minicloud.repository;

import com.minicloud.model.ComputeInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ComputeInstanceRepository extends JpaRepository<ComputeInstance, Long> {
    Optional<ComputeInstance> findByName(String name);
    Optional<ComputeInstance> findByContainerId(String containerId);
    List<ComputeInstance> findByOwner(String owner);
}
