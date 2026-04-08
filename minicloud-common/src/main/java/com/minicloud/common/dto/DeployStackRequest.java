package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployStackRequest {
    private String stackName;
    private String template;
    private String webImageId;
    private String dbType;
    private String dbName;
    private String dbPassword;
    private String vpcId;
    private String subnetId;
    private int instanceCount;
    private String lbAlgorithm;
}
