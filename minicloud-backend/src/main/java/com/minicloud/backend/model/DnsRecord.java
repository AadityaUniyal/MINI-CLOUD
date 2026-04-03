package com.minicloud.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dns_records")
public class DnsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hosted_zone_id")
    private HostedZone hostedZone;

    private String name; // e.g. www.example.com
    private String type; // A, CNAME, TXT, MX
    private String recordValue; // IP or Target DNS
    private Integer ttl;

    public DnsRecord() {}

    public Long getId() { return id; }
    public HostedZone getHostedZone() { return hostedZone; }
    public void setHostedZone(HostedZone v) { this.hostedZone = v; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getType() { return type; }
    public void setType(String v) { this.type = v; }
    public String getRecordValue() { return recordValue; }
    public void setRecordValue(String v) { this.recordValue = v; }
    public Integer getTtl() { return ttl; }
    public void setTtl(Integer v) { this.ttl = v; }
}
