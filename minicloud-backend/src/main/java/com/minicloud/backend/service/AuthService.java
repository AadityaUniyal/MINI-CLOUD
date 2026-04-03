package com.minicloud.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
public class AuthService {

    private final RestTemplate restTemplate;

    @Value("${minicloud.iam.url:http://iam-service:8081}")
    private String iamServiceUrl;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateToken(String token) {
        log.info("Validating token with IAM service");
        try {
            // Simulation of token validation call to IAM service
            // String url = iamServiceUrl + "/api/auth/validate?token=" + token;
            // Boolean isValid = restTemplate.getForObject(url, Boolean.class);
            // return isValid != null && isValid;
            
            // For now, return true if token is present (placeholder)
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }
}
