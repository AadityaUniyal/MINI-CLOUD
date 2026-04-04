package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaunchRequest {
    private String instanceType;
    private String imageId;
    private String keyName;
    private int count;
    private String instanceName;
    private String vpcId;
    private String subnetId;
    private String amiId;
}
