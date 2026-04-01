package com.minicloud.console.dto;

public class AuditLogDto {
    private String action;
    private String actor;
    private String timestamp;
    private String resourceId;

    public AuditLogDto() {}

    public AuditLogDto(String action, String actor, String timestamp, String resourceId) {
        this.action = action;
        this.actor = actor;
        this.timestamp = timestamp;
        this.resourceId = resourceId;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
}
