package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "storage_files")
public class StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    private LocalDateTime uploadedAt;

    public StorageFile() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public Bucket getBucket() { return bucket; }
    public void setBucket(Bucket bucket) { this.bucket = bucket; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final StorageFile f = new StorageFile();
        public Builder fileName(String v) { f.fileName = v; return this; }
        public Builder size(Long v) { f.size = v; return this; }
        public Builder contentType(String v) { f.contentType = v; return this; }
        public Builder bucket(Bucket v) { f.bucket = v; return this; }
        public Builder uploadedAt(LocalDateTime v) { f.uploadedAt = v; return this; }
        public StorageFile build() { return f; }
    }
}
