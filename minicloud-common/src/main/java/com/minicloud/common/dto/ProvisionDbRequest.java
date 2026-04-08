package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionDbRequest {
    private String name;
    private String engine;
    private String engineVersion;
    private String dbInstanceClass;
    private String dbName;
    private String dbType;
    private String username;
    private String password;
    private String rootPassword;
    private int port;
    private String vpcId;
    private String subnetId;
}
