package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stacks")
public class Stack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String stackId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String owner;

    private Long databaseId;
    private Long loadBalancerId;

    @ElementCollection
    @CollectionTable(name = "stack_instances", joinColumns = @JoinColumn(name = "stack_id"))
    @Column(name = "compute_instance_id")
    private List<Long> computeInstanceIds;

    private LocalDateTime createdAt;

    public Stack() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStackId() { return stackId; }
    public void setStackId(String stackId) { this.stackId = stackId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Long getDatabaseId() { return databaseId; }
    public void setDatabaseId(Long databaseId) { this.databaseId = databaseId; }
    public Long getLoadBalancerId() { return loadBalancerId; }
    public void setLoadBalancerId(Long loadBalancerId) { this.loadBalancerId = loadBalancerId; }
    public List<Long> getComputeInstanceIds() { return computeInstanceIds; }
    public void setComputeInstanceIds(List<Long> computeInstanceIds) { this.computeInstanceIds = computeInstanceIds; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Stack s = new Stack();
        public Builder stackId(String v) { s.stackId = v; return this; }
        public Builder name(String v) { s.name = v; return this; }
        public Builder owner(String v) { s.owner = v; return this; }
        public Builder databaseId(Long v) { s.databaseId = v; return this; }
        public Builder loadBalancerId(Long v) { s.loadBalancerId = v; return this; }
        public Builder computeInstanceIds(List<Long> v) { s.computeInstanceIds = v; return this; }
        public Builder createdAt(LocalDateTime v) { s.createdAt = v; return this; }
        public Stack build() { return s; }
    }
}
