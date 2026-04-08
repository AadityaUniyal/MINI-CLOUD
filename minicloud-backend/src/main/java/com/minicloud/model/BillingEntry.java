package com.minicloud.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class BillingEntry {
    private String userId;
    private String resourceId;
    private String resourceType;
    private double hourlyRate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
