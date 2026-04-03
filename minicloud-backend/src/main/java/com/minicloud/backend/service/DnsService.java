package com.minicloud.backend.service;

import com.minicloud.backend.model.HostedZone;
import com.minicloud.backend.model.DnsRecord;
import com.minicloud.backend.repository.HostedZoneRepository;
import com.minicloud.backend.repository.DnsRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DnsService {

    private final HostedZoneRepository hostedZoneRepository;
    private final DnsRecordRepository dnsRecordRepository;

    public DnsService(HostedZoneRepository hostedZoneRepository, DnsRecordRepository dnsRecordRepository) {
        this.hostedZoneRepository = hostedZoneRepository;
        this.dnsRecordRepository = dnsRecordRepository;
    }

    public HostedZone createHostedZone(String owner, String name, String comment) {
        HostedZone zone = new HostedZone();
        zone.setName(name);
        zone.setOwner(owner);
        zone.setComment(comment);
        zone.setCreatedAt(LocalDateTime.now());
        zone.setResourceRecordCount(0);
        return hostedZoneRepository.save(zone);
    }

    @Transactional
    public DnsRecord createResourceRecord(String zoneName, String name, String type, String value, Integer ttl) {
        HostedZone zone = hostedZoneRepository.findByName(zoneName)
                .orElseThrow(() -> new RuntimeException("Hosted Zone not found"));
        
        DnsRecord record = new DnsRecord();
        record.setHostedZone(zone);
        record.setName(name);
        record.setType(type);
        record.setRecordValue(value);
        record.setTtl(ttl != null ? ttl : 300);
        
        DnsRecord saved = dnsRecordRepository.save(record);
        zone.setResourceRecordCount(zone.getResourceRecordCount() + 1);
        hostedZoneRepository.save(zone);
        return saved;
    }

    @Transactional
    public DnsRecord createAliasRecord(String zoneName, String name, String targetResourceName) {
        return createResourceRecord(zoneName, name, "ALIAS", targetResourceName, 60);
    }

    public List<HostedZone> getHostedZonesByOwner(String owner) {
        return hostedZoneRepository.findByOwner(owner);
    }

    public List<DnsRecord> getRecordsByZone(String zoneName) {
        return hostedZoneRepository.findByName(zoneName)
                .map(dnsRecordRepository::findByHostedZone)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    @Transactional
    public void deleteHostedZone(String name) {
        hostedZoneRepository.findByName(name).ifPresent(zone -> {
            dnsRecordRepository.findByHostedZone(zone).forEach(dnsRecordRepository::delete);
            hostedZoneRepository.delete(zone);
        });
    }
}
