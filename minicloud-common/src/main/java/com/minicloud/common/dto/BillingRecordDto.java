package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingRecordDto {
    private Long id;
    private String username;
    private String resourceType; // e.g., "EC2", "S3", "RDS"
    private String resourceId;
    private double cost;
    private String status; // e.g., "Pending", "Paid"
    private LocalDateTime timestamp;
}
