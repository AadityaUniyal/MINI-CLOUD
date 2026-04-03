package com.minicloud.compute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstanceRequest {
    private String name;
    private String type;
    private String imageId;
    private String tenantId;
}
