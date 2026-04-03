package com.minicloud.monitoring.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceId;
    private String metricName;
    private Double value;
    private String unit;
    private LocalDateTime timestamp;

    public Metric() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static MetricBuilder builder() {
        return new MetricBuilder();
    }

    public static class MetricBuilder {
        private final Metric m = new Metric();

        public MetricBuilder resourceId(String resourceId) { m.resourceId = resourceId; return this; }
        public MetricBuilder metricName(String metricName) { m.metricName = metricName; return this; }
        public MetricBuilder value(Double value) { m.value = value; return this; }
        public MetricBuilder unit(String unit) { m.unit = unit; return this; }
        public MetricBuilder timestamp(LocalDateTime timestamp) { m.timestamp = timestamp; return this; }

        public Metric build() {
            if (m.timestamp == null) m.timestamp = LocalDateTime.now();
            return m;
        }
    }
}
