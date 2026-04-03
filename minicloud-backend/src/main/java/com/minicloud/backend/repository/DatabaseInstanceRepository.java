package com.minicloud.backend.repository;

import com.minicloud.backend.model.DatabaseInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseInstanceRepository extends JpaRepository<DatabaseInstance, Long> {
    Optional<DatabaseInstance> findByName(String name);
    List<DatabaseInstance> findByOwner(String owner);
}
