package com.minicloud.common.dto;

public class VpcDto {
    private String vpcId;
    private String name;
    private String cidrBlock;
    private String state;

    public VpcDto() {}

    public VpcDto(String vpcId, String name, String cidrBlock, String state) {
        this.vpcId = vpcId;
        this.name = name;
        this.cidrBlock = cidrBlock;
        this.state = state;
    }

    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
