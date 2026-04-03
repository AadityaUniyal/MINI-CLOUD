package com.minicloud.backend.repository;

import com.minicloud.backend.model.DnsRecord;
import com.minicloud.backend.model.HostedZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DnsRecordRepository extends JpaRepository<DnsRecord, Long> {
    List<DnsRecord> findByHostedZone(HostedZone hostedZone);
    List<DnsRecord> findByNameAndType(String name, String type);
}
