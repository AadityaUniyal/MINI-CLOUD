package com.minicloud.backend.service.monitoring;

import com.minicloud.backend.model.Metric;
import com.minicloud.backend.repository.MetricRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetricService {

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    public void recordMetric(String resourceId, String metricName, Double value, String unit) {
        Metric metric = Metric.builder()
                .resourceId(resourceId)
                .metricName(metricName)
                .value(value)
                .unit(unit)
                .timestamp(LocalDateTime.now())
                .build();
        metricRepository.save(metric);
    }

    public List<Metric> getMetrics(String resourceId, String metricName, int minutes) {
        LocalDateTime start = LocalDateTime.now().minusMinutes(minutes);
        return metricRepository.findByResourceIdAndTimestampBetween(resourceId, start, LocalDateTime.now());
    }

    public List<Metric> getLatestMetrics(String resourceId, String metricName) {
        return metricRepository.findByResourceIdAndMetricNameOrderByTimestampDesc(resourceId, metricName);
    }
}
