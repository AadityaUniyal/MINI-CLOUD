package com.minicloud.backend.repository;

import com.minicloud.backend.model.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StackRepository extends JpaRepository<Stack, Long> {
    List<Stack> findByOwner(String owner);
    Optional<Stack> findByStackId(String stackId);
}
