package com.minicloud.backend.repository;

import com.minicloud.backend.model.ComputeInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputeInstanceRepository extends JpaRepository<ComputeInstance, Long> {
    Optional<ComputeInstance> findByName(String name);
    Optional<ComputeInstance> findByContainerId(String containerId);
    List<ComputeInstance> findByOwner(String owner);
}
