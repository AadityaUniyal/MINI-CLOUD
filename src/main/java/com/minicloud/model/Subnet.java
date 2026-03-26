package com.minicloud.model;

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

    private Subnet(Builder builder) {
        this.id = builder.id;
        this.subnetId = builder.subnetId;
        this.vpcId = builder.vpcId;
        this.cidrBlock = builder.cidrBlock;
        this.availabilityZone = builder.availabilityZone;
        this.availableIpAddressCount = builder.availableIpAddressCount;
        this.mapPublicIpOnLaunch = builder.mapPublicIpOnLaunch;
        this.state = builder.state;
        this.owner = builder.owner;
        this.creationTime = builder.creationTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
    public int getAvailableIpAddressCount() { return availableIpAddressCount; }
    public void setAvailableIpAddressCount(int availableIpAddressCount) { this.availableIpAddressCount = availableIpAddressCount; }
    public boolean isMapPublicIpOnLaunch() { return mapPublicIpOnLaunch; }
    public void setMapPublicIpOnLaunch(boolean mapPublicIpOnLaunch) { this.mapPublicIpOnLaunch = mapPublicIpOnLaunch; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }

    public static class Builder {
        private Long id;
        private String subnetId;
        private String vpcId;
        private String cidrBlock;
        private String availabilityZone;
        private int availableIpAddressCount;
        private boolean mapPublicIpOnLaunch;
        private String state;
        private String owner;
        private LocalDateTime creationTime;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder subnetId(String subnetId) { this.subnetId = subnetId; return this; }
        public Builder vpcId(String vpcId) { this.vpcId = vpcId; return this; }
        public Builder cidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; return this; }
        public Builder availabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; return this; }
        public Builder availableIpAddressCount(int availableIpAddressCount) { this.availableIpAddressCount = availableIpAddressCount; return this; }
        public Builder mapPublicIpOnLaunch(boolean mapPublicIpOnLaunch) { this.mapPublicIpOnLaunch = mapPublicIpOnLaunch; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder owner(String owner) { this.owner = owner; return this; }
        public Builder creationTime(LocalDateTime creationTime) { this.creationTime = creationTime; return this; }

        public Subnet build() {
            return new Subnet(this);
        }
    }
}
