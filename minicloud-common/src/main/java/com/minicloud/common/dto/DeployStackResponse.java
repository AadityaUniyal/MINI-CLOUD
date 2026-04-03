package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployStackResponse {
    private String stackId;
    private String status;
    private List<String> instanceIds;
    private String loadBalancerEndpoint;
}
