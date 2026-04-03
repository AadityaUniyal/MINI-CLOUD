package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BackupRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String serviceName;
    private String databaseEngine;
    private String status;
    private String errorCode;
    private LocalDateTime timestamp;
    private Long sizeBytes;

    public BackupRecord() {}

    public BackupRecord(String fileName, String serviceName, String databaseEngine, String status, String errorCode, LocalDateTime timestamp, Long sizeBytes) {
        this.fileName = fileName;
        this.serviceName = serviceName;
        this.databaseEngine = databaseEngine;
        this.status = status;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
        this.sizeBytes = sizeBytes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getDatabaseEngine() { return databaseEngine; }
    public void setDatabaseEngine(String databaseEngine) { this.databaseEngine = databaseEngine; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }

    public static BackupRecordBuilder builder() {
        return new BackupRecordBuilder();
    }

    public static class BackupRecordBuilder {
        private String fileName;
        private String serviceName;
        private String databaseEngine;
        private String status;
        private String errorCode;
        private LocalDateTime timestamp;
        private Long sizeBytes;

        public BackupRecordBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public BackupRecordBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public BackupRecordBuilder databaseEngine(String databaseEngine) { this.databaseEngine = databaseEngine; return this; }
        public BackupRecordBuilder status(String status) { this.status = status; return this; }
        public BackupRecordBuilder errorCode(String errorCode) { this.errorCode = errorCode; return this; }
        public BackupRecordBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public BackupRecordBuilder sizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; return this; }

        public BackupRecord build() {
            return new BackupRecord(fileName, serviceName, databaseEngine, status, errorCode, timestamp, sizeBytes);
        }
    }
}
