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
    private String image; // e.g., tomcat:latest
    private String status; // RUNNING, STOPPED, TERMINATED
    private String owner; // Username of the owner
    
    private String instanceType; // e.g., t2.micro, t2.small
    private String region; // e.g., us-east-1, ap-south-1
    private String availabilityZone; // e.g., us-east-1a
    private String publicIp; // Simulated public IP
    private String privateIp; // Simulated private IP (172.31.x.x)
    private String vpcId; // simulated vpc-xxxxx
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
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getContainerId() { return containerId; }
    public void setContainerId(String v) { this.containerId = v; }
    public String getImage() { return image; }
    public void setImage(String v) { this.image = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String v) { this.instanceType = v; }
    public String getPublicIp() { return publicIp; }
    public void setPublicIp(String v) { this.publicIp = v; }
    public Integer getHostPort() { return hostPort; }
    public void setHostPort(Integer v) { this.hostPort = v; }
    public String getRegion() { return region; }
    public void setRegion(String v) { this.region = v; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String v) { this.availabilityZone = v; }
    public String getPrivateIp() { return privateIp; }
    public void setPrivateIp(String v) { this.privateIp = v; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String v) { this.vpcId = v; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String v) { this.subnetId = v; }
    public String getAmiId() { return amiId; }
    public void setAmiId(String v) { this.amiId = v; }
    public boolean isEbsOptimized() { return ebsOptimized; }
    public void setEbsOptimized(boolean v) { this.ebsOptimized = v; }
    public List<String> getSecurityGroups() { return securityGroups; }
    public void setSecurityGroups(List<String> v) { this.securityGroups = v; }
    public String getKeyPairName() { return keyPairName; }
    public void setKeyPairName(String v) { this.keyPairName = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ComputeInstance inst = new ComputeInstance();
        public Builder name(String v) { inst.name = v; return this; }
        public Builder containerId(String v) { inst.containerId = v; return this; }
        public Builder image(String v) { inst.image = v; return this; }
        public Builder status(String v) { inst.status = v; return this; }
        public Builder owner(String v) { inst.owner = v; return this; }
        public Builder instanceType(String v) { inst.instanceType = v; return this; }
        public Builder region(String v) { inst.region = v; return this; }
        public Builder availabilityZone(String v) { inst.availabilityZone = v; return this; }
        public Builder publicIp(String v) { inst.publicIp = v; return this; }
        public Builder privateIp(String v) { inst.privateIp = v; return this; }
        public Builder vpcId(String v) { inst.vpcId = v; return this; }
        public Builder subnetId(String v) { inst.subnetId = v; return this; }
        public Builder amiId(String v) { inst.amiId = v; return this; }
        public Builder ebsOptimized(boolean v) { inst.ebsOptimized = v; return this; }
        public Builder securityGroups(List<String> v) { inst.securityGroups = v; return this; }
        public Builder keyPairName(String v) { inst.keyPairName = v; return this; }
        public Builder hostPort(Integer v) { inst.hostPort = v; return this; }
        public Builder createdAt(LocalDateTime v) { inst.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v) { inst.updatedAt = v; return this; }
        public ComputeInstance build() { return inst; }
    }
}
