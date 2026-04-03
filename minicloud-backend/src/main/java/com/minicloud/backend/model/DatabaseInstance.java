package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "database_instances")
public class DatabaseInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String dbInstanceIdentifier;
    private String engine; // mysql, postgres, mariadb
    private String engineVersion;
    private String dbInstanceClass; // e.g., db.t3.micro
    private String status; // creating, available, deleting
    private String endpoint;
    private Integer port;
    private String masterUsername;
    private String masterPassword;
    private String dbName;
    private Integer allocatedStorage; // GB
    private String owner;
    private LocalDateTime createdAt;
    private String region;
    private String availabilityZone;
    private boolean multiAz;
    private Integer backupRetentionPeriod;
    private String containerId;
    private String rootPassword;
    private int hostPort;
    private String vpcId;
    private String subnetId;
    private String storageType;
    private String readReplicaSourceId;
    private boolean publiclyAccessible;

    public DatabaseInstance() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getEngine() { return engine; }
    public void setEngine(String v) { this.engine = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getDbInstanceIdentifier() { return dbInstanceIdentifier; }
    public void setDbInstanceIdentifier(String v) { this.dbInstanceIdentifier = v; }
    public String getDbInstanceClass() { return dbInstanceClass; }
    public void setDbInstanceClass(String v) { this.dbInstanceClass = v; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String v) { this.endpoint = v; }
    public Integer getPort() { return port; }
    public void setPort(Integer v) { this.port = v; }
    public String getContainerId() { return containerId; }
    public void setContainerId(String v) { this.containerId = v; }
    public String getDbName() { return dbName; }
    public void setDbName(String v) { this.dbName = v; }
    public String getRootPassword() { return rootPassword; }
    public void setRootPassword(String v) { this.rootPassword = v; }
    public int getHostPort() { return hostPort; }
    public void setHostPort(int v) { this.hostPort = v; }
    public String getEngineVersion() { return engineVersion; }
    public void setEngineVersion(String v) { this.engineVersion = v; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String v) { this.vpcId = v; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String v) { this.subnetId = v; }
    public boolean isPubliclyAccessible() { return publiclyAccessible; }
    public void setPubliclyAccessible(boolean v) { this.publiclyAccessible = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DatabaseInstance inst = new DatabaseInstance();
        public Builder name(String v) { inst.name = v; return this; }
        public Builder dbInstanceIdentifier(String v) { inst.dbInstanceIdentifier = v; return this; }
        public Builder engine(String v) { inst.engine = v; return this; }
        public Builder engineVersion(String v) { inst.engineVersion = v; return this; }
        public Builder dbInstanceClass(String v) { inst.dbInstanceClass = v; return this; }
        public Builder status(String v) { inst.status = v; return this; }
        public Builder endpoint(String v) { inst.endpoint = v; return this; }
        public Builder port(Integer v) { inst.port = v; return this; }
        public Builder masterUsername(String v) { inst.masterUsername = v; return this; }
        public Builder masterPassword(String v) { inst.masterPassword = v; return this; }
        public Builder dbName(String v) { inst.dbName = v; return this; }
        public Builder allocatedStorage(Integer v) { inst.allocatedStorage = v; return this; }
        public Builder owner(String v) { inst.owner = v; return this; }
        public Builder createdAt(LocalDateTime v) { inst.createdAt = v; return this; }
        public Builder region(String v) { inst.region = v; return this; }
        public Builder availabilityZone(String v) { inst.availabilityZone = v; return this; }
        public Builder multiAz(boolean v) { inst.multiAz = v; return this; }
        public Builder backupRetentionPeriod(Integer v) { inst.backupRetentionPeriod = v; return this; }
        public Builder publiclyAccessible(boolean v) { inst.publiclyAccessible = v; return this; }
        public Builder containerId(String v) { inst.containerId = v; return this; }
        public Builder rootPassword(String v) { inst.rootPassword = v; return this; }
        public Builder hostPort(int v) { inst.hostPort = v; return this; }
        public Builder vpcId(String v) { inst.vpcId = v; return this; }
        public Builder subnetId(String v) { inst.subnetId = v; return this; }
        public Builder storageType(String v) { inst.storageType = v; return this; }
        public Builder readReplicaSourceId(String v) { inst.readReplicaSourceId = v; return this; }
        public DatabaseInstance build() { return inst; }
    }
}
