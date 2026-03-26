package com.minicloud.repository;

import com.minicloud.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByResourceIdAndTimestampBetween(String resourceId, LocalDateTime start, LocalDateTime end);
    List<Metric> findByResourceIdAndMetricNameOrderByTimestampDesc(String resourceId, String metricName);
}
