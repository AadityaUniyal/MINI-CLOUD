package com.minicloud.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployStackResponse {
    private String stackId;
    private String status;
    private String loadBalancerUrl;
    private String message;
}
