package com.minicloud.billing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private Double totalAmount;
    private String status; // PENDING, PAID, OVERDUE
    private LocalDateTime billingPeriodStart;
    private LocalDateTime billingPeriodEnd;
    private LocalDateTime createdAt;

    public Invoice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getBillingPeriodStart() { return billingPeriodStart; }
    public void setBillingPeriodStart(LocalDateTime billingPeriodStart) { this.billingPeriodStart = billingPeriodStart; }
    public LocalDateTime getBillingPeriodEnd() { return billingPeriodEnd; }
    public void setBillingPeriodEnd(LocalDateTime billingPeriodEnd) { this.billingPeriodEnd = billingPeriodEnd; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static InvoiceBuilder builder() {
        return new InvoiceBuilder();
    }

    public static class InvoiceBuilder {
        private final Invoice i = new Invoice();
        public InvoiceBuilder tenantId(String tenantId) { i.tenantId = tenantId; return this; }
        public InvoiceBuilder totalAmount(Double totalAmount) { i.totalAmount = totalAmount; return this; }
        public InvoiceBuilder status(String status) { i.status = status; return this; }
        public InvoiceBuilder billingPeriodStart(LocalDateTime start) { i.billingPeriodStart = start; return this; }
        public InvoiceBuilder billingPeriodEnd(LocalDateTime end) { i.billingPeriodEnd = end; return this; }
        public Invoice build() {
            if (i.createdAt == null) i.createdAt = LocalDateTime.now();
            return i;
        }
    }
}
