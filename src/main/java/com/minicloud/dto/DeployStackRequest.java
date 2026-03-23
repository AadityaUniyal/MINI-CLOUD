package com.minicloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeployStackRequest {
    private String stackName;
    private String computeImage;
    private String dbName;
    private String dbPassword;
    private int instanceCount;
}
