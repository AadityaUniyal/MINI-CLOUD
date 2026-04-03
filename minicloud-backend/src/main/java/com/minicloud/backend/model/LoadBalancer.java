package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "load_balancers")
public class LoadBalancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private Integer publicPort;
    private String algorithm; // ROUND_ROBIN
    private String protocol; // HTTP, TCP
    private String status; // ACTIVE, PENDING
    private String healthCheckPath; // e.g. /health
    private Integer healthCheckInterval; // in seconds
    private String owner;
    private String vpcId;

    @ManyToMany
    @JoinTable(
        name = "load_balancer_instances",
        joinColumns = @JoinColumn(name = "load_balancer_id"),
        inverseJoinColumns = @JoinColumn(name = "compute_instance_id")
    )
    private List<ComputeInstance> targetInstances;

    public LoadBalancer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPublicPort() { return publicPort; }
    public void setPublicPort(Integer publicPort) { this.publicPort = publicPort; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getHealthCheckPath() { return healthCheckPath; }
    public void setHealthCheckPath(String healthCheckPath) { this.healthCheckPath = healthCheckPath; }
    public Integer getHealthCheckInterval() { return healthCheckInterval; }
    public void setHealthCheckInterval(Integer healthCheckInterval) { this.healthCheckInterval = healthCheckInterval; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public List<ComputeInstance> getTargetInstances() { return targetInstances; }
    public void setTargetInstances(List<ComputeInstance> targetInstances) { this.targetInstances = targetInstances; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final LoadBalancer lb = new LoadBalancer();
        public Builder name(String v) { lb.name = v; return this; }
        public Builder publicPort(Integer v) { lb.publicPort = v; return this; }
        public Builder algorithm(String v) { lb.algorithm = v; return this; }
        public Builder protocol(String v) { lb.protocol = v; return this; }
        public Builder status(String v) { lb.status = v; return this; }
        public Builder healthCheckPath(String v) { lb.healthCheckPath = v; return this; }
        public Builder healthCheckInterval(Integer v) { lb.healthCheckInterval = v; return this; }
        public Builder owner(String v) { lb.owner = v; return this; }
        public Builder vpcId(String v) { lb.vpcId = v; return this; }
        public Builder targetInstances(List<ComputeInstance> v) { lb.targetInstances = v; return this; }
        public LoadBalancer build() { return lb; }
    }
}
