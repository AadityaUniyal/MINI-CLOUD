package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "cf_stacks")
public class CfStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String stackName;

    @Column(columnDefinition = "TEXT")
    private String templateJson;

    private String status; // CREATE_IN_PROGRESS, CREATE_COMPLETE, DELETE_IN_PROGRESS
    private String owner;
    private LocalDateTime createdAt;
    
    @ElementCollection
    @CollectionTable(name = "cf_resource_mapping", joinColumns = @JoinColumn(name = "stack_id"))
    @MapKeyColumn(name = "logical_id")
    @Column(name = "physical_id")
    private Map<String, String> resourceMapping = new HashMap<>();

    public CfStack() {}

    public Long getId() { return id; }
    public String getStackName() { return stackName; }
    public void setStackName(String v) { this.stackName = v; }
    public String getTemplateJson() { return templateJson; }
    public void setTemplateJson(String v) { this.templateJson = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public Map<String, String> getResourceMapping() { return resourceMapping; }
    public void setResourceMapping(Map<String, String> v) { this.resourceMapping = v; }
}
