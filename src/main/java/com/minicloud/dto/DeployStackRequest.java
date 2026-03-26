package com.minicloud.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployStackRequest {
    private String stackName;
    private int instanceCount;
    private String dbName;
    private String dbPassword;
    private String template; // e.g., JAVA_WEB_APP, HA_WEB_CLUSTER
}
