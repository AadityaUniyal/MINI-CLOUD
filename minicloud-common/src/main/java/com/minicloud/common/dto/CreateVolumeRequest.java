package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVolumeRequest {
    private String name;
    private int sizeGb;
    private String instanceId;
    private String availabilityZone;
    private String volumeType;
}
