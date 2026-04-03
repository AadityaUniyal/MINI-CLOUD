package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwsLoginRequest {
    private String accountId; // Can be ID or Alias
    private String email;      // For root
    private String iamUsername; // For IAM user
    private String password;
    private boolean rootUser;
}
