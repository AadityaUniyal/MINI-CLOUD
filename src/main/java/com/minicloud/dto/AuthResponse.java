package com.minicloud.dto;

public class AuthResponse {
    private String token;
    private String message;
    private Double balance;
    private String accountId;
    private String username;

    public AuthResponse() {}
    public AuthResponse(String token, String message, Double balance, String accountId, String username) {
        this.token = token; this.message = message; this.balance = balance;
        this.accountId = accountId; this.username = username;
    }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final AuthResponse r = new AuthResponse();
        public Builder token(String v) { r.token = v; return this; }
        public Builder message(String v) { r.message = v; return this; }
        public Builder balance(Double v) { r.balance = v; return this; }
        public Builder accountId(String v) { r.accountId = v; return this; }
        public Builder username(String v) { r.username = v; return this; }
        public AuthResponse build() { return r; }
    }
}
