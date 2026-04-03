package com.minicloud.billing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_records")
public class UsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String resourceType; // COMPUTE, STORAGE, DATABASE
    private String resourceId;
    private Double quantity;
    private LocalDateTime timestamp;

    public UsageRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static UsageRecordBuilder builder() {
        return new UsageRecordBuilder();
    }

    public static class UsageRecordBuilder {
        private final UsageRecord ur = new UsageRecord();
        public UsageRecordBuilder tenantId(String tenantId) { ur.tenantId = tenantId; return this; }
        public UsageRecordBuilder resourceType(String resourceType) { ur.resourceType = resourceType; return this; }
        public UsageRecordBuilder resourceId(String resourceId) { ur.resourceId = resourceId; return this; }
        public UsageRecordBuilder quantity(Double quantity) { ur.quantity = quantity; return this; }
        public UsageRecordBuilder timestamp(LocalDateTime timestamp) { ur.timestamp = timestamp; return this; }
        public UsageRecord build() {
            if (ur.timestamp == null) ur.timestamp = LocalDateTime.now();
            return ur;
        }
    }
}
