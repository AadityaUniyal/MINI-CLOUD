package com.minicloud.loadbalancer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "load_balancers")
public class LoadBalancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // APPLICATION, NETWORK
    private String ipAddress;
    private String status;
    private String tenantId;

    public LoadBalancer() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public static LoadBalancerBuilder builder() {
        return new LoadBalancerBuilder();
    }

    public static class LoadBalancerBuilder {
        private final LoadBalancer lb = new LoadBalancer();

        public LoadBalancerBuilder name(String name) { lb.name = name; return this; }
        public LoadBalancerBuilder type(String type) { lb.type = type; return this; }
        public LoadBalancerBuilder ipAddress(String ipAddress) { lb.ipAddress = ipAddress; return this; }
        public LoadBalancerBuilder status(String status) { lb.status = status; return this; }
        public LoadBalancerBuilder tenantId(String tenantId) { lb.tenantId = tenantId; return this; }

        public LoadBalancer build() {
            return lb;
        }
    }
}
