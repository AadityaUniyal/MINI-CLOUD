package com.minicloud.monitoring.service;

import com.minicloud.monitoring.model.Metric;
import com.minicloud.monitoring.repository.MetricRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MonitoringService {

    private final MetricRepository metricRepository;
    private final MeterRegistry meterRegistry;
    // Stores last-known value per metric name; gauges are registered once per name
    private final ConcurrentHashMap<String, AtomicLong> gaugeValues = new ConcurrentHashMap<>();

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

        // Register gauge exactly once per metric name; subsequent calls just update the AtomicLong
        AtomicLong holder = gaugeValues.computeIfAbsent(name, k -> {
            AtomicLong al = new AtomicLong(Double.doubleToLongBits(0.0));
            meterRegistry.gauge(name, al, v -> Double.longBitsToDouble(v.get()));
            return al;
        });
        holder.set(Double.doubleToLongBits(value));
    }

    public List<Metric> getResourceMetrics(String resourceId) {
        return metricRepository.findByResourceId(resourceId);
    }
}
