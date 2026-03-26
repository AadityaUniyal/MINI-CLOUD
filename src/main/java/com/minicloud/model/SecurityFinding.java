package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_findings")
public class SecurityFinding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // e.g. BRUTE_FORCE, UNAUTHORIZED_ACCESS
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String resourceId;
    private String owner;
    private String description;
    private LocalDateTime timestamp;

    public SecurityFinding() {}

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String v) { this.type = v; }
    public String getSeverity() { return severity; }
    public void setSeverity(String v) { this.severity = v; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String v) { this.resourceId = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime v) { this.timestamp = v; }
}
