package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "buckets")
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String owner;

    private String region;
    private String accessControl;
    private String arn;
    private boolean versioningEnabled;
    private LocalDateTime createdAt;

    public Bucket() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public boolean isVersioningEnabled() { return versioningEnabled; }
    public void setVersioningEnabled(boolean versioningEnabled) { this.versioningEnabled = versioningEnabled; }
    public String getAccessControl() { return accessControl; }
    public void setAccessControl(String accessControl) { this.accessControl = accessControl; }
    public String getArn() { return arn; }
    public void setArn(String arn) { this.arn = arn; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Bucket b = new Bucket();
        public Builder name(String v) { b.name = v; return this; }
        public Builder owner(String v) { b.owner = v; return this; }
        public Builder region(String v) { b.region = v; return this; }
        public Builder accessControl(String v) { b.accessControl = v; return this; }
        public Builder arn(String v) { b.arn = v; return this; }
        public Builder versioningEnabled(boolean v) { b.versioningEnabled = v; return this; }
        public Builder createdAt(LocalDateTime v) { b.createdAt = v; return this; }
        public Bucket build() { return b; }
    }
}
