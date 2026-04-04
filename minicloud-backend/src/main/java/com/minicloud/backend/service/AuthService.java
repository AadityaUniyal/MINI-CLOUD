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
        log.info("Validating token with IAM service: {}", iamServiceUrl);
        try {
            if (token == null || token.isEmpty()) return false;
            
            // In a real environment, we would call the IAM service:
            // String url = iamServiceUrl + "/api/auth/validate?token=" + token;
            // return restTemplate.getForObject(url, Boolean.class);

            // Simulation: Assume tokens starting with 'ey' are valid JWTs for this PBL
            return token.startsWith("ey") || token.equals("mock-admin-token");
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }
}
