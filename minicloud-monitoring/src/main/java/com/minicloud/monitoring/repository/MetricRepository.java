package com.minicloud.monitoring.repository;

import com.minicloud.monitoring.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByResourceId(String resourceId);
}
