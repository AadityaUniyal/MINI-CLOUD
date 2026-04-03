package com.minicloud.backend.controller;

import com.minicloud.backend.model.HostedZone;
import com.minicloud.backend.model.DnsRecord;
import com.minicloud.backend.service.DnsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dns")
public class DnsController {

    private final DnsService dnsService;

    public DnsController(DnsService dnsService) {
        this.dnsService = dnsService;
    }

    @PostMapping("/zone")
    public ResponseEntity<HostedZone> createHostedZone(@RequestBody Map<String, String> request, Principal principal) {
        return ResponseEntity.ok(dnsService.createHostedZone(
                principal.getName(),
                request.get("name"),
                request.get("comment")
        ));
    }

    @GetMapping("/zones")
    public List<HostedZone> listHostedZones(Principal principal) {
        return dnsService.getHostedZonesByOwner(principal.getName());
    }

    @PostMapping("/zone/{zoneName}/record")
    public ResponseEntity<DnsRecord> createRecord(@PathVariable String zoneName, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(dnsService.createResourceRecord(
                zoneName,
                (String) request.get("name"),
                (String) request.get("type"),
                (String) request.get("value"),
                request.get("ttl") != null ? ((Number) request.get("ttl")).intValue() : null
        ));
    }

    @GetMapping("/zone/{zoneName}/records")
    public List<DnsRecord> listRecords(@PathVariable String zoneName) {
        return dnsService.getRecordsByZone(zoneName);
    }

    @DeleteMapping("/zone/{zoneName}")
    public ResponseEntity<Void> deleteZone(@PathVariable String zoneName) {
        dnsService.deleteHostedZone(zoneName);
        return ResponseEntity.ok().build();
    }
}
