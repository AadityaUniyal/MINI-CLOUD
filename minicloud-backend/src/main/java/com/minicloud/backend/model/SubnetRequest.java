package com.minicloud.backend.dto;

public class SubnetRequest {
    private String vpcId;
    private String cidrBlock;
    private String availabilityZone;
    private boolean mapPublicIpOnLaunch;

    public SubnetRequest() {}

    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
    public boolean isMapPublicIpOnLaunch() { return mapPublicIpOnLaunch; }
    public void setMapPublicIpOnLaunch(boolean mapPublicIpOnLaunch) { this.mapPublicIpOnLaunch = mapPublicIpOnLaunch; }
}
