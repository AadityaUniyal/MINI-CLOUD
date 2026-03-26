package com.minicloud.repository;

import com.minicloud.model.SecurityFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecurityFindingRepository extends JpaRepository<SecurityFinding, Long> {
    List<SecurityFinding> findByOwner(String owner);
    List<SecurityFinding> findBySeverity(String severity);
}
