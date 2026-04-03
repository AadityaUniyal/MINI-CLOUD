package com.minicloud.backend.repository;

import com.minicloud.backend.model.LambdaFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LambdaRepository extends JpaRepository<LambdaFunction, Long> {
    Optional<LambdaFunction> findByName(String name);
    List<LambdaFunction> findByOwner(String owner);
}
