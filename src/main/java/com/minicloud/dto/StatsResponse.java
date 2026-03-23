package com.minicloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsResponse {
    private String containerId;
    private double cpuUsage; 
    private long memoryUsage; 
    private long memoryLimit; 
    private String status;
}
