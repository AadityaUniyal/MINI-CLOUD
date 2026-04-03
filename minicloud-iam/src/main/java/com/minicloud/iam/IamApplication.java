package com.minicloud.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * MiniCloud Identity & Access Management (IAM) Service.
 * This service handles user authentication, RBAC, and multi-tenancy.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IamApplication {
    public static void main(String[] args) {
        SpringApplication.run(IamApplication.class, args);
    }
}
