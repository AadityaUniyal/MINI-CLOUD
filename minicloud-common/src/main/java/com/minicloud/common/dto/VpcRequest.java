package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VpcRequest {
    private String cidrBlock;
    private String name;
    private boolean enableDnsSupport;
    private boolean enableDnsHostnames;
}
