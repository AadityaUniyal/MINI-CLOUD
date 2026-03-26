package com.minicloud.model;

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

    private String runtime; // java, python, nodejs
    private String handler;
    private String arn;
    private String owner;
    
    @Column(columnDefinition = "TEXT")
    private String code;

    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    public LambdaFunction() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getRuntime() { return runtime; }
    public void setRuntime(String v) { this.runtime = v; }
    public String getHandler() { return handler; }
    public void setHandler(String v) { this.handler = v; }
    public String getArn() { return arn; }
    public void setArn(String v) { this.arn = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getCode() { return code; }
    public void setCode(String v) { this.code = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
