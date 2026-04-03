package com.minicloud.compute.repository;

import com.minicloud.compute.model.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
    Optional<Instance> findByInstanceId(String instanceId);
    List<Instance> findByTenantId(String tenantId);
}
