package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hosted_zones")
public class HostedZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g. example.com

    private String owner;
    private String comment;
    private LocalDateTime createdAt;
    private Integer resourceRecordCount;

    public HostedZone() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getComment() { return comment; }
    public void setComment(String v) { this.comment = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public Integer getResourceRecordCount() { return resourceRecordCount; }
    public void setResourceRecordCount(Integer v) { this.resourceRecordCount = v; }
}
