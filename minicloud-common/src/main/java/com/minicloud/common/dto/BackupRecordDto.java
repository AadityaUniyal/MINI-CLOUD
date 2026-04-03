package com.minicloud.common.dto;

import java.time.LocalDateTime;

public class BackupRecordDto {
    private Long id;
    private String fileName;
    private String serviceName;
    private String databaseEngine;
    private String status;
    private String errorCode;
    private LocalDateTime timestamp;
    private Long sizeBytes;

    public BackupRecordDto() {}

    public BackupRecordDto(Long id, String fileName, String serviceName, String databaseEngine, String status, String errorCode, LocalDateTime timestamp, Long sizeBytes) {
        this.id = id;
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
}
