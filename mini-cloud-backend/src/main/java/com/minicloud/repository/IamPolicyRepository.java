package com.minicloud.repository;

import com.minicloud.model.IamPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface IamPolicyRepository extends JpaRepository<IamPolicy, Long> {
    Optional<IamPolicy> findByName(String name);
    List<IamPolicy> findByOwner(String owner);
}
