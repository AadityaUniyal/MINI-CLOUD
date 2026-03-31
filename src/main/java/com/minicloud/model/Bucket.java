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

    private String owner;
    private String region;
    private String accessControl;
    private String arn;
    private boolean versioningEnabled;
    private boolean websiteEnabled;
    private String indexDocument;
    private String errorDocument;
    private LocalDateTime createdAt;

    public Bucket() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getRegion() { return region; }
    public void setRegion(String v) { this.region = v; }
    public String getAccessControl() { return accessControl; }
    public void setAccessControl(String v) { this.accessControl = v; }
    public String getArn() { return arn; }
    public void setArn(String v) { this.arn = v; }
    public boolean isVersioningEnabled() { return versioningEnabled; }
    public void setVersioningEnabled(boolean v) { this.versioningEnabled = v; }
    public boolean isWebsiteEnabled() { return websiteEnabled; }
    public void setWebsiteEnabled(boolean v) { this.websiteEnabled = v; }
    public String getIndexDocument() { return indexDocument; }
    public void setIndexDocument(String v) { this.indexDocument = v; }
    public String getErrorDocument() { return errorDocument; }
    public void setErrorDocument(String v) { this.errorDocument = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Bucket b = new Bucket();
        public Builder name(String v) { b.name = v; return this; }
        public Builder owner(String v) { b.owner = v; return this; }
        public Builder region(String v) { b.region = v; return this; }
        public Builder accessControl(String v) { b.accessControl = v; return this; }
        public Builder arn(String v) { b.arn = v; return this; }
        public Builder versioningEnabled(boolean v) { b.versioningEnabled = v; return this; }
        public Builder websiteEnabled(boolean v) { b.websiteEnabled = v; return this; }
        public Builder indexDocument(String v) { b.indexDocument = v; return this; }
        public Builder errorDocument(String v) { b.errorDocument = v; return this; }
        public Builder createdAt(LocalDateTime v) { b.createdAt = v; return this; }
        public Bucket build() { return b; }
    }
}
