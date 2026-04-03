package com.minicloud.monitoring.service;

import com.minicloud.monitoring.model.Metric;
import com.minicloud.monitoring.repository.MetricRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MonitoringService {

    private final MetricRepository metricRepository;
    private final MeterRegistry meterRegistry;

    public MonitoringService(MetricRepository metricRepository, MeterRegistry meterRegistry) {
        this.metricRepository = metricRepository;
        this.meterRegistry = meterRegistry;
    }

    public void recordMetric(String resourceId, String name, Double value, String unit) {
        Metric metric = Metric.builder()
                .resourceId(resourceId)
                .metricName(name)
                .value(value)
                .unit(unit)
                .timestamp(LocalDateTime.now())
                .build();
        metricRepository.save(metric);
        
        // Also register with micrometer for prometheus scraping
        meterRegistry.gauge(name, value);
    }

    public List<Metric> getResourceMetrics(String resourceId) {
        return metricRepository.findByResourceId(resourceId);
    }
}
