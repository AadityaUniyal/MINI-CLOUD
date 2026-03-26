package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resourceId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(columnDefinition = "TEXT")
    private String details;

    private String sourceIp;
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String requestParameters;

    public AuditLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getRequestParameters() { return requestParameters; }
    public void setRequestParameters(String requestParameters) { this.requestParameters = requestParameters; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final AuditLog a = new AuditLog();
        public Builder user(User v) { a.user = v; return this; }
        public Builder action(String v) { a.action = v; return this; }
        public Builder resourceId(String v) { a.resourceId = v; return this; }
        public Builder timestamp(LocalDateTime v) { a.timestamp = v; return this; }
        public Builder details(String v) { a.details = v; return this; }
        public Builder sourceIp(String v) { a.sourceIp = v; return this; }
        public Builder userAgent(String v) { a.userAgent = v; return this; }
        public Builder requestParameters(String v) { a.requestParameters = v; return this; }
        public AuditLog build() { return a; }
    }
}
