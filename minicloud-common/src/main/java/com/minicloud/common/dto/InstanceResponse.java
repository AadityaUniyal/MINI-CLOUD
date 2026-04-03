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
public class InstanceResponse {
    private String instanceId;
    private String tenantId;
    private String name;
    private String type;
    private String state;
    private String ipAddress;
    private String imageId;
    private LocalDateTime createdAt;
}
