package com.minicloud.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "iam_roles")
public class IamRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    private String owner;

    @ManyToMany
    @JoinTable(
        name = "role_policies",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private List<IamPolicy> attachedPolicies;

    public IamRole() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public List<IamPolicy> getAttachedPolicies() { return attachedPolicies; }
    public void setAttachedPolicies(List<IamPolicy> v) { this.attachedPolicies = v; }
}
