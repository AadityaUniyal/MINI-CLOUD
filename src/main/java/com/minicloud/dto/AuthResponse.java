package com.minicloud.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String message;
    private Double balance;
    private String accountId;
    private String username;
}
