package com.minicloud.compute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "instances")
public class Instance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instanceId; // AWS-like i-xxxxxx
    private String tenantId;
    private String name;
    private String type; // t2.micro, etc.
    private String state; // RUNNING, STOPPED, TERMINATED
    private String ipAddress;
    private String containerId; // Docker reference
    private String imageId;
    
    private LocalDateTime createdAt;

    public Instance() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }
    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static InstanceBuilder builder() {
        return new InstanceBuilder();
    }

    public static class InstanceBuilder {
        private final Instance i = new Instance();
        public InstanceBuilder instanceId(String id) { i.instanceId = id; return this; }
        public InstanceBuilder tenantId(String id) { i.tenantId = id; return this; }
        public InstanceBuilder name(String name) { i.name = name; return this; }
        public InstanceBuilder type(String type) { i.type = type; return this; }
        public InstanceBuilder state(String state) { i.state = state; return this; }
        public InstanceBuilder containerId(String cid) { i.containerId = cid; return this; }
        public InstanceBuilder imageId(String iid) { i.imageId = iid; return this; }
        public InstanceBuilder createdAt(LocalDateTime createdAt) { i.createdAt = createdAt; return this; }
        public Instance build() {
            if (i.createdAt == null) i.createdAt = LocalDateTime.now();
            return i;
        }
    }
}
