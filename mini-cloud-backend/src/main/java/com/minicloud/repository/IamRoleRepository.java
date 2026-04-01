package com.minicloud.repository;

import com.minicloud.model.IamRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface IamRoleRepository extends JpaRepository<IamRole, Long> {
    Optional<IamRole> findByName(String name);
    List<IamRole> findByOwner(String owner);
}
