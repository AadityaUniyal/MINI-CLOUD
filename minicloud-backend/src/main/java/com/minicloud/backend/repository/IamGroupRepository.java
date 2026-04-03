package com.minicloud.backend.repository;

import com.minicloud.backend.model.IamGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface IamGroupRepository extends JpaRepository<IamGroup, Long> {
    Optional<IamGroup> findByGroupName(String groupName);
    List<IamGroup> findByOwner(String owner);
}
