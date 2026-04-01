package com.minicloud.service;

import com.github.dockerjava.api.model.Statistics;
import com.minicloud.dto.StatsResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatsService {

    private final DockerService dockerService;

    public StatsService(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    public StatsResponse getContainerMetrics(String containerId) {
        Statistics stats = dockerService.getContainerStats(containerId);
        if (stats == null) {
            return StatsResponse.builder()
                    .containerId(containerId)
                    .status("ERROR")
                    .build();
        }

        // Calculate CPU percentage (simplified version)
        double cpuDelta = stats.getCpuStats().getCpuUsage().getTotalUsage();
        double systemDelta = stats.getCpuStats().getSystemCpuUsage();
        double cpuUsage = (cpuDelta / systemDelta) * 100.0;

        return StatsResponse.builder()
                .containerId(containerId)
                .cpuUsage(cpuUsage)
                .memoryUsage(stats.getMemoryStats().getUsage())
                .memoryLimit(stats.getMemoryStats().getLimit())
                .status("ACTIVE")
                .build();
    }

    public List<StatsResponse> getMultiContainerMetrics(List<String> containerIds) {
        return containerIds.stream()
                .map(this::getContainerMetrics)
                .toList();
    }
}
