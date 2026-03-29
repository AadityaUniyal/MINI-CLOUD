package com.minicloud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Internal unique username for Spring Security

    private String email; // Required for Root user
    private String accountId; // 12-digit unique ID
    private String accountAlias;
    private boolean isRootUser;
    private String iamUsername; // Username within the account
    private String owner; // Username of the Root user for IAM accounts
    
    // Profile information
    private String fullName;
    private String phoneNumber;
    private String address;
    private String country;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // e.g., ROLE_USER, ROLE_ADMIN

    @Column(nullable = false)
    @Builder.Default
    private Double balance = 100.0; // Starting credit

    private String accessKey;
    private String secretKey;
    private String status; // ACTIVE, INACTIVE, SUSPENDED
}
