package com.minicloud.console.dto;

public class SubnetDto {
    private String subnetId;
    private String vpcId;
    private String cidrBlock;
    private String availabilityZone;
    private int availableIpAddresses;

    public SubnetDto() {}

    public SubnetDto(String subnetId, String vpcId, String cidrBlock, String availabilityZone, int availableIpAddresses) {
        this.subnetId = subnetId;
        this.vpcId = vpcId;
        this.cidrBlock = cidrBlock;
        this.availabilityZone = availabilityZone;
        this.availableIpAddresses = availableIpAddresses;
    }

    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
    public int getAvailableIpAddresses() { return availableIpAddresses; }
    public void setAvailableIpAddresses(int availableIpAddresses) { this.availableIpAddresses = availableIpAddresses; }
}
