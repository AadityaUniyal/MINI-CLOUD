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
    private String dbName;
    private String engine;
    private String rootPassword;
    private String engineVersion;
    private String dbInstanceClass;
    private String vpcId;
    private String subnetId;
    private String dbType;   // Keep for backward compatibility if needed
    private String username;
    private String password;
    private int port;
}
