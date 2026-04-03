package com.minicloud.compute.repository;

import com.minicloud.compute.model.InstanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstanceTypeRepository extends JpaRepository<InstanceType, Long> {
    Optional<InstanceType> findByName(String name);
}
