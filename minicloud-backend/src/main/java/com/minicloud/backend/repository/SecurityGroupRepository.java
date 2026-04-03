package com.minicloud.backend.repository;

import com.minicloud.backend.model.SecurityGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecurityGroupRepository extends JpaRepository<SecurityGroup, Long> {
    List<SecurityGroup> findByOwner(String owner);
    SecurityGroup findByNameAndOwner(String name, String owner);
}
