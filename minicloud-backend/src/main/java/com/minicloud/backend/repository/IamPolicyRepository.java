package com.minicloud.backend.repository;

import com.minicloud.backend.model.IamPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface IamPolicyRepository extends JpaRepository<IamPolicy, Long> {
    Optional<IamPolicy> findByName(String name);
    List<IamPolicy> findByOwner(String owner);
}
