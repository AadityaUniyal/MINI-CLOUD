package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subnets")
public class Subnet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String subnetId;

    @Column(nullable = false)
    private String vpcId;

    @Column(nullable = false)
    private String cidrBlock;

    private String availabilityZone;
    private int availableIpAddressCount;
    private boolean mapPublicIpOnLaunch;
    private String state;
    private String owner;
    private LocalDateTime creationTime;

    public Subnet() {}

    public Long getId() { return id; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String v) { this.subnetId = v; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String v) { this.vpcId = v; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String v) { this.cidrBlock = v; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String v) { this.availabilityZone = v; }
    public String getState() { return state; }
    public void setState(String v) { this.state = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime v) { this.creationTime = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Subnet s = new Subnet();
        public Builder subnetId(String val) { s.subnetId = val; return this; }
        public Builder vpcId(String val) { s.vpcId = val; return this; }
        public Builder cidrBlock(String val) { s.cidrBlock = val; return this; }
        public Builder availabilityZone(String val) { s.availabilityZone = val; return this; }
        public Builder availableIpAddressCount(int val) { s.availableIpAddressCount = val; return this; }
        public Builder mapPublicIpOnLaunch(boolean val) { s.mapPublicIpOnLaunch = val; return this; }
        public Builder state(String val) { s.state = val; return this; }
        public Builder owner(String val) { s.owner = val; return this; }
        public Builder creationTime(LocalDateTime val) { s.creationTime = val; return this; }
        public Subnet build() { return s; }
    }
}
