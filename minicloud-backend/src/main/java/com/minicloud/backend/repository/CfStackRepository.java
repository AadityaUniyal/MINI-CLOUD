package com.minicloud.backend.repository;

import com.minicloud.backend.model.CfStack;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CfStackRepository extends JpaRepository<CfStack, Long> {
    Optional<CfStack> findByStackName(String stackName);
    List<CfStack> findByOwner(String owner);
}
