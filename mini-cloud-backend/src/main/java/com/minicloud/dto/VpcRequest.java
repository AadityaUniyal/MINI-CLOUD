package com.minicloud.dto;

public class VpcRequest {
    private String cidrBlock;
    private boolean enableDnsSupport;
    private boolean enableDnsHostnames;

    public VpcRequest() {}

    public String getCidrBlock() { return cidrBlock; }
    public void setCidrBlock(String cidrBlock) { this.cidrBlock = cidrBlock; }
    public boolean isEnableDnsSupport() { return enableDnsSupport; }
    public void setEnableDnsSupport(boolean enableDnsSupport) { this.enableDnsSupport = enableDnsSupport; }
    public boolean isEnableDnsHostnames() { return enableDnsHostnames; }
    public void setEnableDnsHostnames(boolean enableDnsHostnames) { this.enableDnsHostnames = enableDnsHostnames; }
}
