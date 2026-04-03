package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String resourceId; // Instance ID, DB ID, Volume ID

    @Column(nullable = false)
    private String metricName; // CPUUtilization, MemoryUsage, etc.

    @Column(nullable = false)
    private Double metricValue;

    private String unit; // Percent, Bytes, Count

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Metric() {}

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public String getResourceId() { return resourceId; }
    public String getMetricName() { return metricName; }
    public Double getMetricValue() { return metricValue; }
    public String getUnit() { return unit; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public static class Builder {
        private final Metric m = new Metric();
        public Builder resourceId(String v) { m.resourceId = v; return this; }
        public Builder metricName(String v) { m.metricName = v; return this; }
        public Builder value(Double v) { m.metricValue = v; return this; }
        public Builder unit(String v) { m.unit = v; return this; }
        public Builder timestamp(LocalDateTime v) { m.timestamp = v; return this; }
        public Metric build() { return m; }
    }
}
