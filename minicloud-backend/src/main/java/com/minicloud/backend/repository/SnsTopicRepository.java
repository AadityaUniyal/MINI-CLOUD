package com.minicloud.backend.repository;

import com.minicloud.backend.model.SnsTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SnsTopicRepository extends JpaRepository<SnsTopic, Long> {
    Optional<SnsTopic> findByName(String name);
    List<SnsTopic> findByOwner(String owner);
}
