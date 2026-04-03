package com.minicloud.backend.dto;

public class ProvisionDbRequest {
    private String name;
    private String dbName;
    private String rootPassword;
    private String vpcId;
    private String subnetId;
    private String engine;
    private String engineVersion;
    private String dbInstanceClass;

    public ProvisionDbRequest() {}
    public ProvisionDbRequest(String name, String dbName, String rootPassword) {
        this.name = name; this.dbName = dbName; this.rootPassword = rootPassword;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDbName() { return dbName; }
    public void setDbName(String dbName) { this.dbName = dbName; }
    public String getRootPassword() { return rootPassword; }
    public void setRootPassword(String rootPassword) { this.rootPassword = rootPassword; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }
    public String getEngineVersion() { return engineVersion; }
    public void setEngineVersion(String engineVersion) { this.engineVersion = engineVersion; }
    public String getDbInstanceClass() { return dbInstanceClass; }
    public void setDbInstanceClass(String dbInstanceClass) { this.dbInstanceClass = dbInstanceClass; }
}
