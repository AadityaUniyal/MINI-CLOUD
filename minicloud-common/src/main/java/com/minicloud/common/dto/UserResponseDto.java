package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Safe user representation for API responses.
 * Never includes the hashed password field.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String accountId;
    private String accountAlias;
    private Double balance;
    private String status;
    private boolean rootUser;
    private String iamUsername;
    private String accessKey;   // only populated on IAM user creation; null thereafter
    private Set<String> roles;
    private LocalDateTime createdAt;
}
