package com.minicloud.iam.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accessKey;

    @Column(nullable = false)
    private String secretKeyHash;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String status;

    public ApiKey() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getSecretKeyHash() { return secretKeyHash; }
    public void setSecretKeyHash(String secretKeyHash) { this.secretKeyHash = secretKeyHash; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static ApiKeyBuilder builder() {
        return new ApiKeyBuilder();
    }

    public static class ApiKeyBuilder {
        private final ApiKey ak = new ApiKey();
        public ApiKeyBuilder accessKey(String accessKey) { ak.accessKey = accessKey; return this; }
        public ApiKeyBuilder secretKeyHash(String secretKeyHash) { ak.secretKeyHash = secretKeyHash; return this; }
        public ApiKeyBuilder user(User user) { ak.user = user; return this; }
        public ApiKeyBuilder status(String status) { ak.status = status; return this; }
        public ApiKey build() {
            if (ak.createdAt == null) ak.createdAt = LocalDateTime.now();
            return ak;
        }
    }
}
