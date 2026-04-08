package com.minicloud.backend.service;

// Revised to use the common DTO
import com.minicloud.common.dto.StatsResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatsService {

    private final DockerService dockerService;

    public StatsService(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    public StatsResponse getContainerMetrics(String containerId) {
        com.github.dockerjava.api.model.Statistics stats = dockerService.getContainerStats(containerId);
        if (stats == null) {
            return StatsResponse.builder()
                    .containerId(containerId)
                    .build();
        }

        // Calculate CPU percentage (simplified version)
        double cpuDelta = stats.getCpuStats().getCpuUsage().getTotalUsage();
        double systemDelta = stats.getCpuStats().getSystemCpuUsage();
        double cpuUsage = (cpuDelta / systemDelta) * 100.0;

        return StatsResponse.builder()
                .containerId(containerId)
                .cpuUsagePercent(cpuUsage)
                .memoryUsedBytes(stats.getMemoryStats().getUsage())
                .memoryLimitBytes(stats.getMemoryStats().getLimit())
                .build();
    }

    public List<StatsResponse> getMultiContainerMetrics(List<String> containerIds) {
        return containerIds.stream()
                .map(this::getContainerMetrics)
                .toList();
    }
}
