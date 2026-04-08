package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lambda_functions")
public class LambdaFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String runtime; // e.g., nodejs18.x, python3.9, java11
    private String handler;
    private String codeBlob; // Base64 or local path
    private String description;
    private String role; // IAM Role ARN
    private Integer memorySize; // MB
    private Integer timeout; // Seconds
    private String status; // Active, Pending, Failed
    private String owner;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private String arn;

    public LambdaFunction() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getRuntime() { return runtime; }
    public void setRuntime(String v) { this.runtime = v; }
    public String getHandler() { return handler; }
    public void setHandler(String v) { this.handler = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getArn() { return arn; }
    public void setArn(String v) { this.arn = v; }
    public Integer getMemorySize() { return memorySize; }
    public void setMemorySize(Integer v) { this.memorySize = v; }
    public Integer getTimeout() { return timeout; }
    public void setTimeout(Integer v) { this.timeout = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime v) { this.lastModified = v; }
    public String getCode() { return codeBlob; }
    public void setCode(String v) { this.codeBlob = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final LambdaFunction f = new LambdaFunction();
        public Builder name(String v) { f.name = v; return this; }
        public Builder runtime(String v) { f.runtime = v; return this; }
        public Builder handler(String v) { f.handler = v; return this; }
        public Builder codeBlob(String v) { f.codeBlob = v; return this; }
        public Builder description(String v) { f.description = v; return this; }
        public Builder role(String v) { f.role = v; return this; }
        public Builder memorySize(Integer v) { f.memorySize = v; return this; }
        public Builder timeout(Integer v) { f.timeout = v; return this; }
        public Builder status(String v) { f.status = v; return this; }
        public Builder owner(String v) { f.owner = v; return this; }
        public Builder createdAt(LocalDateTime v) { f.createdAt = v; return this; }
        public Builder lastModified(LocalDateTime v) { f.lastModified = v; return this; }
        public Builder arn(String v) { f.arn = v; return this; }
        public LambdaFunction build() { return f; }
    }
}
