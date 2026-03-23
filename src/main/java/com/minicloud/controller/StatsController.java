package com.minicloud.controller;

import com.minicloud.dto.StatsResponse;
import com.minicloud.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<StatsResponse> getStats(@PathVariable String containerId) {
        return ResponseEntity.ok(statsService.getContainerMetrics(containerId));
    }
}
