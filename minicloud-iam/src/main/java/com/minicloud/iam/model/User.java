package com.minicloud.iam.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

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
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountAlias() { return accountAlias; }
    public void setAccountAlias(String accountAlias) { this.accountAlias = accountAlias; }
    public boolean isRootUser() { return isRootUser; }
    public void setIsRootUser(boolean isRootUser) { this.isRootUser = isRootUser; }
    public String getIamUsername() { return iamUsername; }
    public void setIamUsername(String iamUsername) { this.iamUsername = iamUsername; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private final User user = new User();

        public UserBuilder username(String username) { user.username = username; return this; }
        public UserBuilder email(String email) { user.email = email; return this; }
        public UserBuilder password(String password) { user.password = password; return this; }
        public UserBuilder fullName(String fullName) { user.fullName = fullName; return this; }
        public UserBuilder tenant(Tenant tenant) { user.tenant = tenant; return this; }
        public UserBuilder accountId(String accountId) { user.accountId = accountId; return this; }
        public UserBuilder roles(Set<Role> roles) { user.roles = roles; return this; }
        public UserBuilder iamUsername(String iamUsername) { user.iamUsername = iamUsername; return this; }
        public UserBuilder isRootUser(boolean isRootUser) { user.isRootUser = isRootUser; return this; }
        public UserBuilder accountAlias(String accountAlias) { user.accountAlias = accountAlias; return this; }
        public UserBuilder owner(String owner) { user.owner = owner; return this; }
        public UserBuilder phoneNumber(String phoneNumber) { user.phoneNumber = phoneNumber; return this; }
        public UserBuilder balance(Double balance) { user.balance = balance; return this; }
        public UserBuilder status(String status) { user.status = status; return this; }
        public UserBuilder address(String address) { user.address = address; return this; }
        public UserBuilder country(String country) { user.country = country; return this; }

        public User build() {
            if (user.createdAt == null) user.createdAt = LocalDateTime.now();
            return user;
        }
    }
}
