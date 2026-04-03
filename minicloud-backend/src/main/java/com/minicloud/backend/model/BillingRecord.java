package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing_records")
public class BillingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;
    private String resourceType; // EC2, RDS, S3, etc.
    private String resourceId;
    private Double amount;
    private String currency; // USD
    private LocalDateTime timestamp;

    public BillingRecord() {}

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String v) { this.resourceType = v; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String v) { this.resourceId = v; }
    public Double getAmount() { return amount; }
    public void setAmount(Double v) { this.amount = v; }
    public String getCurrency() { return currency; }
    public void setCurrency(String v) { this.currency = v; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime v) { this.timestamp = v; }
}
