package com.minicloud.controller;

import com.minicloud.model.Metric;
import com.minicloud.service.monitoring.MetricService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MetricService metricService;

    public MonitoringController(MetricService metricService) {
        this.metricService = metricService;
    }

    @GetMapping("/metrics/{resourceId}")
    public List<Metric> getMetrics(@PathVariable String resourceId, 
                                  @RequestParam String metricName,
                                  @RequestParam(defaultValue = "60") int minutes) {
        return metricService.getMetrics(resourceId, metricName, minutes);
    }

    @GetMapping("/metrics/{resourceId}/latest")
    public List<Metric> getLatestMetrics(@PathVariable String resourceId, 
                                        @RequestParam String metricName) {
        return metricService.getLatestMetrics(resourceId, metricName);
    }
}
