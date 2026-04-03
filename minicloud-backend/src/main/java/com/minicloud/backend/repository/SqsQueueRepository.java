package com.minicloud.backend.repository;

import com.minicloud.backend.model.SqsQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SqsQueueRepository extends JpaRepository<SqsQueue, Long> {
    Optional<SqsQueue> findByName(String name);
    List<SqsQueue> findByOwner(String owner);
}
