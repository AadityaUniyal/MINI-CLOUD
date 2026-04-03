package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwsRegisterRequest {
    private String email;
    private String password;
    private String accountAlias;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String country;
}
