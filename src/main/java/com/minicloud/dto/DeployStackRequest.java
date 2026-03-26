package com.minicloud.dto;

public class DeployStackRequest {
    private String stackName;
    private int instanceCount;
    private String dbName;
    private String dbPassword;
    private String template;
    private String vpcId;
    private String subnetId;

    public DeployStackRequest() {}
    public String getStackName() { return stackName; }
    public void setStackName(String stackName) { this.stackName = stackName; }
    public int getInstanceCount() { return instanceCount; }
    public void setInstanceCount(int instanceCount) { this.instanceCount = instanceCount; }
    public String getDbName() { return dbName; }
    public void setDbName(String dbName) { this.dbName = dbName; }
    public String getDbPassword() { return dbPassword; }
    public void setDbPassword(String dbPassword) { this.dbPassword = dbPassword; }
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
}
