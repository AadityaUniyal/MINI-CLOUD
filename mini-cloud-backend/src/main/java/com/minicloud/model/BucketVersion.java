package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bucket_versions")
public class BucketVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bucketName;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String versionId;

    private String filePath; // Path to the actual file content in storage
    private long size;
    private String contentType;
    private boolean isLatest;
    private LocalDateTime createdAt;

    public BucketVersion() {}

    private BucketVersion(Builder builder) {
        this.id = builder.id;
        this.bucketName = builder.bucketName;
        this.fileName = builder.fileName;
        this.versionId = builder.versionId;
        this.filePath = builder.filePath;
        this.size = builder.size;
        this.contentType = builder.contentType;
        this.isLatest = builder.isLatest;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() { return new Builder(); }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getVersionId() { return versionId; }
    public void setVersionId(String versionId) { this.versionId = versionId; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public boolean isLatest() { return isLatest; }
    public void setLatest(boolean latest) { isLatest = latest; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class Builder {
        private Long id;
        private String bucketName;
        private String fileName;
        private String versionId;
        private String filePath;
        private long size;
        private String contentType;
        private boolean isLatest;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder bucketName(String bucketName) { this.bucketName = bucketName; return this; }
        public Builder fileName(String fileName) { this.fileName = fileName; return this; }
        public Builder versionId(String versionId) { this.versionId = versionId; return this; }
        public Builder filePath(String filePath) { this.filePath = filePath; return this; }
        public Builder size(long size) { this.size = size; return this; }
        public Builder contentType(String contentType) { this.contentType = contentType; return this; }
        public Builder isLatest(boolean isLatest) { this.isLatest = isLatest; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BucketVersion build() { return new BucketVersion(this); }
    }
}
