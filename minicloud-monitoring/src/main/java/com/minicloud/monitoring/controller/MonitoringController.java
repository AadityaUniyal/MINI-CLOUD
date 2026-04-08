package com.minicloud.monitoring.controller;

import com.minicloud.monitoring.model.Metric;
import com.minicloud.monitoring.service.MonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Fix #11: REST endpoints for metric recording and retrieval.
 */
@RestController
@RequestMapping("/api/v1/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    /**
     * POST /api/v1/monitoring/record
     * Records a metric reading for a resource.
     *
     * Body: { "resourceId": "i-abc123", "name": "cpu_usage", "value": 72.5, "unit": "percent" }
     */
    @PostMapping("/record")
    public ResponseEntity<Void> record(@RequestBody Map<String, Object> body) {
        String resourceId = (String) body.get("resourceId");
        String name       = (String) body.get("name");
        Double value      = Double.parseDouble(body.getOrDefault("value", 0.0).toString());
        String unit       = (String) body.getOrDefault("unit", "");
        monitoringService.recordMetric(resourceId, name, value, unit);
        return ResponseEntity.accepted().build();
    }

    /**
     * GET /api/v1/monitoring/{resourceId}
     * Returns all recorded metrics for the given resource.
     */
    @GetMapping("/{resourceId}")
    public ResponseEntity<List<Metric>> getMetrics(@PathVariable String resourceId) {
        return ResponseEntity.ok(monitoringService.getResourceMetrics(resourceId));
    }
}
