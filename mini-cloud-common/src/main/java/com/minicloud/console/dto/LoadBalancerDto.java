package com.minicloud.console.dto;

import java.util.List;

public class LoadBalancerDto {
    private String name;
    private String dnsName;
    private int publicPort;
    private List<Long> instanceIds;
    private String vpcId;
    private String status;

    public LoadBalancerDto() {}

    public LoadBalancerDto(String name, String dnsName, int publicPort, List<Long> instanceIds, String vpcId, String status) {
        this.name = name;
        this.dnsName = dnsName;
        this.publicPort = publicPort;
        this.instanceIds = instanceIds;
        this.vpcId = vpcId;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDnsName() { return dnsName; }
    public void setDnsName(String dnsName) { this.dnsName = dnsName; }
    public int getPublicPort() { return publicPort; }
    public void setPublicPort(int publicPort) { this.publicPort = publicPort; }
    public List<Long> getInstanceIds() { return instanceIds; }
    public void setInstanceIds(List<Long> instanceIds) { this.instanceIds = instanceIds; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
