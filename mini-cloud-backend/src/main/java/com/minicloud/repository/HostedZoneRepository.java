package com.minicloud.repository;

import com.minicloud.model.HostedZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HostedZoneRepository extends JpaRepository<HostedZone, Long> {
    Optional<HostedZone> findByName(String name);
    List<HostedZone> findByOwner(String owner);
}
