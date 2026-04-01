package com.minicloud.repository;

import com.minicloud.model.Subnet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubnetRepository extends JpaRepository<Subnet, Long> {
    Optional<Subnet> findBySubnetId(String subnetId);
    List<Subnet> findByVpcId(String vpcId);
    List<Subnet> findByOwner(String owner);
}
