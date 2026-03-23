package com.minicloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeployStackResponse {
    private String stackId;
    private String status;
    private String loadBalancerUrl;
    private String message;
}
