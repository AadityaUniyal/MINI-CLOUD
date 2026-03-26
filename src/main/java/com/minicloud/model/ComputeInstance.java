package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compute_instances")
public class ComputeInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String containerId;
    private String image;
    private String status;
    private String owner;
    private String instanceType;
    private String region;
    private String availabilityZone;
    private String publicIp;
    private String privateIp;
    private String vpcId;
    private String subnetId;
    private String amiId;
    private boolean ebsOptimized;

    @ElementCollection
    private List<String> securityGroups;
    private String keyPairName;
    private Integer hostPort;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ComputeInstance() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
    public String getPublicIp() { return publicIp; }
    public void setPublicIp(String publicIp) { this.publicIp = publicIp; }
    public String getPrivateIp() { return privateIp; }
    public void setPrivateIp(String privateIp) { this.privateIp = privateIp; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public String getAmiId() { return amiId; }
    public void setAmiId(String amiId) { this.amiId = amiId; }
    public boolean isEbsOptimized() { return ebsOptimized; }
    public void setEbsOptimized(boolean ebsOptimized) { this.ebsOptimized = ebsOptimized; }
    public List<String> getSecurityGroups() { return securityGroups; }
    public void setSecurityGroups(List<String> securityGroups) { this.securityGroups = securityGroups; }
    public String getKeyPairName() { return keyPairName; }
    public void setKeyPairName(String keyPairName) { this.keyPairName = keyPairName; }
    public Integer getHostPort() { return hostPort; }
    public void setHostPort(Integer hostPort) { this.hostPort = hostPort; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ComputeInstance c = new ComputeInstance();
        public Builder name(String v) { c.name = v; return this; }
        public Builder containerId(String v) { c.containerId = v; return this; }
        public Builder image(String v) { c.image = v; return this; }
        public Builder status(String v) { c.status = v; return this; }
        public Builder owner(String v) { c.owner = v; return this; }
        public Builder instanceType(String v) { c.instanceType = v; return this; }
        public Builder region(String v) { c.region = v; return this; }
        public Builder availabilityZone(String v) { c.availabilityZone = v; return this; }
        public Builder publicIp(String v) { c.publicIp = v; return this; }
        public Builder privateIp(String v) { c.privateIp = v; return this; }
        public Builder vpcId(String v) { c.vpcId = v; return this; }
        public Builder subnetId(String v) { c.subnetId = v; return this; }
        public Builder amiId(String v) { c.amiId = v; return this; }
        public Builder ebsOptimized(boolean v) { c.ebsOptimized = v; return this; }
        public Builder securityGroups(List<String> v) { c.securityGroups = v; return this; }
        public Builder keyPairName(String v) { c.keyPairName = v; return this; }
        public Builder hostPort(Integer v) { c.hostPort = v; return this; }
        public Builder createdAt(LocalDateTime v) { c.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v) { c.updatedAt = v; return this; }
        public ComputeInstance build() { return c; }
    }
}
