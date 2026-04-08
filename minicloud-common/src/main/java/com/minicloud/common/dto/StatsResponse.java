package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    private String containerId;
    private double cpuUsagePercent;
    private long memoryUsedBytes;
    private long memoryLimitBytes;
    private String networkIn;
    private String networkOut;
    private Instant timestamp;
}
