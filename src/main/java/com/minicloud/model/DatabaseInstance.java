package com.minicloud.model;

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

    private String containerId;
    private String dbName;
    private String rootPassword;
    private int hostPort;
    private String status;
    private String owner;
    private String engine;
    private String engineVersion;
    private String dbInstanceClass;
    private String readReplicaSourceId;
    private String vpcId;
    private String subnetId;
    private String availabilityZone;
    private String storageType;
    private Integer allocatedStorage;
    private boolean multiAz;
    private boolean publiclyAccessible;
    private Integer backupRetention;
    private LocalDateTime createdAt;

    public DatabaseInstance() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }
    public String getDbName() { return dbName; }
    public void setDbName(String dbName) { this.dbName = dbName; }
    public String getRootPassword() { return rootPassword; }
    public void setRootPassword(String rootPassword) { this.rootPassword = rootPassword; }
    public int getHostPort() { return hostPort; }
    public void setHostPort(int hostPort) { this.hostPort = hostPort; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }
    public String getEngineVersion() { return engineVersion; }
    public void setEngineVersion(String engineVersion) { this.engineVersion = engineVersion; }
    public String getDbInstanceClass() { return dbInstanceClass; }
    public void setDbInstanceClass(String dbInstanceClass) { this.dbInstanceClass = dbInstanceClass; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getSubnetId() { return subnetId; }
    public void setSubnetId(String subnetId) { this.subnetId = subnetId; }
    public String getReadReplicaSourceId() { return readReplicaSourceId; }
    public void setReadReplicaSourceId(String readReplicaSourceId) { this.readReplicaSourceId = readReplicaSourceId; }
    public String getAvailabilityZone() { return availabilityZone; }
    public void setAvailabilityZone(String availabilityZone) { this.availabilityZone = availabilityZone; }
    public String getStorageType() { return storageType; }
    public void setStorageType(String storageType) { this.storageType = storageType; }
    public Integer getAllocatedStorage() { return allocatedStorage; }
    public void setAllocatedStorage(Integer allocatedStorage) { this.allocatedStorage = allocatedStorage; }
    public boolean isMultiAz() { return multiAz; }
    public void setMultiAz(boolean multiAz) { this.multiAz = multiAz; }
    public boolean isPubliclyAccessible() { return publiclyAccessible; }
    public void setPubliclyAccessible(boolean publiclyAccessible) { this.publiclyAccessible = publiclyAccessible; }
    public Integer getBackupRetention() { return backupRetention; }
    public void setBackupRetention(Integer backupRetention) { this.backupRetention = backupRetention; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final DatabaseInstance d = new DatabaseInstance();
        public Builder name(String v) { d.name = v; return this; }
        public Builder containerId(String v) { d.containerId = v; return this; }
        public Builder dbName(String v) { d.dbName = v; return this; }
        public Builder rootPassword(String v) { d.rootPassword = v; return this; }
        public Builder hostPort(int v) { d.hostPort = v; return this; }
        public Builder status(String v) { d.status = v; return this; }
        public Builder owner(String v) { d.owner = v; return this; }
        public Builder engine(String v) { d.engine = v; return this; }
        public Builder engineVersion(String v) { d.engineVersion = v; return this; }
        public Builder dbInstanceClass(String v) { d.dbInstanceClass = v; return this; }
        public Builder readReplicaSourceId(String v) { d.readReplicaSourceId = v; return this; }
        public Builder vpcId(String v) { d.vpcId = v; return this; }
        public Builder subnetId(String v) { d.subnetId = v; return this; }
        public Builder availabilityZone(String v) { d.availabilityZone = v; return this; }
        public Builder storageType(String v) { d.storageType = v; return this; }
        public Builder allocatedStorage(Integer v) { d.allocatedStorage = v; return this; }
        public Builder multiAz(boolean v) { d.multiAz = v; return this; }
        public Builder publiclyAccessible(boolean v) { d.publiclyAccessible = v; return this; }
        public Builder backupRetention(Integer v) { d.backupRetention = v; return this; }
        public Builder createdAt(LocalDateTime v) { d.createdAt = v; return this; }
        public DatabaseInstance build() { return d; }
    }
}
