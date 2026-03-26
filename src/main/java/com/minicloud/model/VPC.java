package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vpcs")
public class VPC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String vpcId;

    @Column(nullable = false)
    private String cidrBlock;

    private String state;
    private String tenancy;
    private boolean isDefault;
    private boolean enableDnsSupport;
    private boolean enableDnsHostnames;
    private String owner;
    private LocalDateTime creationTime;

    public VPC() {}

    private VPC(Builder builder) {
        this.id = builder.id;
        this.vpcId = builder.vpcId;
        this.cidrBlock = builder.cidrBlock;
        this.state = builder.state;
        this.tenancy = builder.tenancy;
        this.isDefault = builder.isDefault;
        this.enableDnsSupport = builder.enableDnsSupport;
        this.enableDnsHostnames = builder.enableDnsHostnames;
        this.owner = builder.owner;
        this.creationTime = builder.creationTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getTenancy() { return tenancy; }
    public void setTenancy(String tenancy) { this.tenancy = tenancy; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
    public boolean isEnableDnsSupport() { return enableDnsSupport; }
    public void setEnableDnsSupport(boolean enableDnsSupport) { this.enableDnsSupport = enableDnsSupport; }
    public boolean isEnableDnsHostnames() { return enableDnsHostnames; }
    public void setEnableDnsHostnames(boolean enableDnsHostnames) { this.enableDnsHostnames = enableDnsHostnames; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime creationTime) { this.creationTime = creationTime; }

    public static class Builder {
        private Long id;
        private String vpcId;
        private String cidrBlock;
        private String state;
        private String tenancy;
        private boolean isDefault;
        private boolean enableDnsSupport;
        private boolean enableDnsHostnames;
        private String owner;
        private LocalDateTime creationTime;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder vpcId(String vpcId) { this.vpcId = vpcId; return this; }
        public Builder cidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; return this; }
        public Builder state(String state) { this.state = state; return this; }
        public Builder tenancy(String tenancy) { this.tenancy = tenancy; return this; }
        public Builder isDefault(boolean isDefault) { this.isDefault = isDefault; return this; }
        public Builder enableDnsSupport(boolean enableDnsSupport) { this.enableDnsSupport = enableDnsSupport; return this; }
        public Builder enableDnsHostnames(boolean enableDnsHostnames) { this.enableDnsHostnames = enableDnsHostnames; return this; }
        public Builder owner(String owner) { this.owner = owner; return this; }
        public Builder creationTime(LocalDateTime creationTime) { this.creationTime = creationTime; return this; }

        public VPC build() {
            return new VPC(this);
        }
    }
}
