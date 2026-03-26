package com.minicloud.dto;

public class AwsLoginRequest {
    private boolean rootUser;
    private String email;
    private String accountId;
    private String iamUsername;
    private String password;

    public AwsLoginRequest() {}
    public boolean isRootUser() { return rootUser; }
    public void setRootUser(boolean rootUser) { this.rootUser = rootUser; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getIamUsername() { return iamUsername; }
    public void setIamUsername(String iamUsername) { this.iamUsername = iamUsername; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
