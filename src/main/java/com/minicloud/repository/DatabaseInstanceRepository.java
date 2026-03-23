package com.minicloud.repository;

import com.minicloud.model.DatabaseInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DatabaseInstanceRepository extends JpaRepository<DatabaseInstance, Long> {
    Optional<DatabaseInstance> findByName(String name);
}
