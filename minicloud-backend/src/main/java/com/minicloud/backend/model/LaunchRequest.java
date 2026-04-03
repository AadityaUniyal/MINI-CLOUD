package com.minicloud.backend.dto;

public class LaunchRequest {
    private String imageName;
    private String instanceName;
    private String instanceType;
    private String region;
    private String keyPairName;
    private String vpcId;
    private String subnetId;
    private String amiId;

    public LaunchRequest() {}
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public String getInstanceName() { return instanceName; }
    public void setInstanceName(String instanceName) { this.instanceName = instanceName; }
    public String getInstanceType() { return instanceType; }
    public void setInstanceType(String instanceType) { this.instanceType = instanceType; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getKeyPairName() { return keyPairName; }
    public void setKeyPairName(String keyPairName) { this.keyPairName = keyPairName; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public String getAmiId() { return amiId; }
    public void setAmiId(String amiId) { this.amiId = amiId; }
}
