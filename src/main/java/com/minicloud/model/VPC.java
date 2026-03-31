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

    public Long getId() { return id; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String v) { this.vpcId = v; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String v) { this.cidrBlock = v; }
    public String getState() { return state; }
    public void setState(String v) { this.state = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public void setCreationTime(LocalDateTime v) { this.creationTime = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final VPC v = new VPC();
        public Builder vpcId(String val) { v.vpcId = val; return this; }
        public Builder cidrBlock(String val) { v.cidrBlock = val; return this; }
        public Builder state(String val) { v.state = val; return this; }
        public Builder tenancy(String val) { v.tenancy = val; return this; }
        public Builder isDefault(boolean val) { v.isDefault = val; return this; }
        public Builder enableDnsSupport(boolean val) { v.enableDnsSupport = val; return this; }
        public Builder enableDnsHostnames(boolean val) { v.enableDnsHostnames = val; return this; }
        public Builder owner(String val) { v.owner = val; return this; }
        public Builder creationTime(LocalDateTime val) { v.creationTime = val; return this; }
        public VPC build() { return v; }
    }
}
