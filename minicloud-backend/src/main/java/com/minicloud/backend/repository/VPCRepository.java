package com.minicloud.backend.repository;

import com.minicloud.backend.model.VPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VPCRepository extends JpaRepository<VPC, Long> {
    Optional<VPC> findByVpcId(String vpcId);
    List<VPC> findByOwner(String owner);
}
