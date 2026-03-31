package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String country;
    private Double balance;
    private String status;
    private LocalDateTime createdAt;
    private String accountId;
    private String accountAlias;
    private boolean isRootUser;
    private String iamUsername;
    private String accessKey;
    private String secretKey;
    private String owner;

    public User() {}

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getPassword() { return password; }
    public void setPassword(String v) { this.password = v; }
    public String getRole() { return role; }
    public void setRole(String v) { this.role = v; }
    public String getFullName() { return fullName; }
    public void setFullName(String v) { this.fullName = v; }
    public Double getBalance() { return balance; }
    public void setBalance(Double v) { this.balance = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String v) { this.accountId = v; }
    public String getAccountAlias() { return accountAlias; }
    public void setAccountAlias(String v) { this.accountAlias = v; }
    public boolean isRootUser() { return isRootUser; }
    public void setRootUser(boolean v) { isRootUser = v; }
    public String getIamUsername() { return iamUsername; }
    public void setIamUsername(String v) { this.iamUsername = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final User u = new User();
        public Builder username(String v) { u.username = v; return this; }
        public Builder email(String v) { u.email = v; return this; }
        public Builder password(String v) { u.password = v; return this; }
        public Builder role(String v) { u.role = v; return this; }
        public Builder fullName(String v) { u.fullName = v; return this; }
        public Builder phoneNumber(String v) { u.phoneNumber = v; return this; }
        public Builder address(String v) { u.address = v; return this; }
        public Builder country(String v) { u.country = v; return this; }
        public Builder balance(Double v) { u.balance = v; return this; }
        public Builder status(String v) { u.status = v; return this; }
        public Builder accountId(String v) { u.accountId = v; return this; }
        public Builder accountAlias(String v) { u.accountAlias = v; return this; }
        public Builder isRootUser(boolean v) { u.isRootUser = v; return this; }
        public Builder iamUsername(String v) { u.iamUsername = v; return this; }
        public Builder accessKey(String v) { u.accessKey = v; return this; }
        public Builder secretKey(String v) { u.secretKey = v; return this; }
        public Builder owner(String v) { u.owner = v; return this; }
        public Builder createdAt(LocalDateTime v) { u.createdAt = v; return this; }
        public User build() { return u; }
    }
}
