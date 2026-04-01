package com.minicloud.repository;

import com.minicloud.model.DnsRecord;
import com.minicloud.model.HostedZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DnsRecordRepository extends JpaRepository<DnsRecord, Long> {
    List<DnsRecord> findByHostedZone(HostedZone hostedZone);
    List<DnsRecord> findByNameAndType(String name, String type);
}
