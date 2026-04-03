package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String action;
    private String resourceId;
    private String details;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User v) { this.user = v; }
    public String getAction() { return action; }
    public void setAction(String v) { this.action = v; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String v) { this.resourceId = v; }
    public String getDetails() { return details; }
    public void setDetails(String v) { this.details = v; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime v) { this.timestamp = v; }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }

    public static class AuditLogBuilder {
        private final AuditLog log = new AuditLog();
        public AuditLogBuilder user(User val) { log.user = val; return this; }
        public AuditLogBuilder action(String val) { log.action = val; return this; }
        public AuditLogBuilder resourceId(String val) { log.resourceId = val; return this; }
        public AuditLogBuilder details(String val) { log.details = val; return this; }
        public AuditLogBuilder timestamp(LocalDateTime val) { log.timestamp = val; return this; }
        public AuditLog build() { 
            if (log.timestamp == null) log.timestamp = LocalDateTime.now();
            return log; 
        }
    }
}
