package com.minicloud.backend.controller;

import com.minicloud.backend.model.StatsResponse;
import com.minicloud.backend.service.OrchestrationService;
import com.minicloud.backend.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;
    private final OrchestrationService orchestrationService;

    public StatsController(StatsService statsService, OrchestrationService orchestrationService) {
        this.statsService = statsService;
        this.orchestrationService = orchestrationService;
    }

    @GetMapping("/{containerId}")
    public ResponseEntity<StatsResponse> getStats(@PathVariable String containerId) {
        return ResponseEntity.ok(statsService.getContainerMetrics(containerId));
    }

    @GetMapping("/stack/{stackId}")
    public ResponseEntity<List<StatsResponse>> getStackStats(@PathVariable String stackId) {
        List<String> containerIds = orchestrationService.getStackContainerIds(stackId);
        return ResponseEntity.ok(statsService.getMultiContainerMetrics(containerIds));
    }

    @GetMapping(value = "/stream/{containerId}", produces = "text/event-stream")
    public SseEmitter getStatsStream(@PathVariable String containerId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                while (true) {
                    StatsResponse stats = statsService.getContainerMetrics(containerId);
                    emitter.send(stats);
                    Thread.sleep(2000); // 2-second polling interval for push
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
