package com.minicloud.iam.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String status;
    private LocalDateTime createdAt;

    public Tenant() {}
    public Tenant(String name) { this.name = name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static TenantBuilder builder() {
        return new TenantBuilder();
    }

    public static class TenantBuilder {
        private final Tenant t = new Tenant();
        public TenantBuilder name(String name) { t.name = name; return this; }
        public TenantBuilder status(String status) { t.status = status; return this; }
        public Tenant build() {
            if (t.createdAt == null) t.createdAt = LocalDateTime.now();
            return t;
        }
    }
}
