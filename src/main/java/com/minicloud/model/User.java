package com.minicloud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;
    private String accountId;
    private String accountAlias;
    private boolean isRootUser;
    private String iamUsername;
    private String owner;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String country;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Double balance = 100.0;

    private String accessKey;
    private String secretKey;
    private String status;

    public User() {}

    public User(Long id, String username, String email, String accountId, String accountAlias,
                boolean isRootUser, String iamUsername, String owner, String fullName,
                String phoneNumber, String address, String country, String password,
                String role, Double balance, String accessKey, String secretKey, String status) {
        this.id = id; this.username = username; this.email = email; this.accountId = accountId;
        this.accountAlias = accountAlias; this.isRootUser = isRootUser; this.iamUsername = iamUsername;
        this.owner = owner; this.fullName = fullName; this.phoneNumber = phoneNumber;
        this.address = address; this.country = country; this.password = password;
        this.role = role; this.balance = balance; this.accessKey = accessKey;
        this.secretKey = secretKey; this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountAlias() { return accountAlias; }
    public void setAccountAlias(String accountAlias) { this.accountAlias = accountAlias; }
    public boolean isRootUser() { return isRootUser; }
    public void setRootUser(boolean rootUser) { isRootUser = rootUser; }
    public String getIamUsername() { return iamUsername; }
    public void setIamUsername(String iamUsername) { this.iamUsername = iamUsername; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final User u = new User();
        public Builder username(String v) { u.username = v; return this; }
        public Builder email(String v) { u.email = v; return this; }
        public Builder accountId(String v) { u.accountId = v; return this; }
        public Builder accountAlias(String v) { u.accountAlias = v; return this; }
        public Builder isRootUser(boolean v) { u.isRootUser = v; return this; }
        public Builder iamUsername(String v) { u.iamUsername = v; return this; }
        public Builder owner(String v) { u.owner = v; return this; }
        public Builder fullName(String v) { u.fullName = v; return this; }
        public Builder phoneNumber(String v) { u.phoneNumber = v; return this; }
        public Builder address(String v) { u.address = v; return this; }
        public Builder country(String v) { u.country = v; return this; }
        public Builder password(String v) { u.password = v; return this; }
        public Builder role(String v) { u.role = v; return this; }
        public Builder balance(Double v) { u.balance = v; return this; }
        public Builder accessKey(String v) { u.accessKey = v; return this; }
        public Builder secretKey(String v) { u.secretKey = v; return this; }
        public Builder status(String v) { u.status = v; return this; }
        public User build() { return u; }
    }
}
