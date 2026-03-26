package com.minicloud.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "iam_groups")
public class IamGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String groupName;

    private String owner;

    @ManyToMany
    @JoinTable(
        name = "group_policies",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private List<IamPolicy> attachedPolicies = new ArrayList<>();

    public IamGroup() {}

    public Long getId() { return id; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String v) { this.groupName = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public List<IamPolicy> getAttachedPolicies() { return attachedPolicies; }
    public void setAttachedPolicies(List<IamPolicy> v) { this.attachedPolicies = v; }
}
