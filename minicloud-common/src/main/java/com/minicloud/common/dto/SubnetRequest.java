package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubnetRequest {
    private String vpcId;
    private String cidrBlock;
    private String availabilityZone;
    private String name;
    private boolean mapPublicIpOnLaunch;
}
