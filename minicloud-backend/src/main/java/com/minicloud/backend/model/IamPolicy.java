package com.minicloud.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "iam_policies")
public class IamPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String policyDocument; // JSON: Action, Effect, Resource

    private String owner;

    public IamPolicy() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getPolicyDocument() { return policyDocument; }
    public void setPolicyDocument(String v) { this.policyDocument = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final IamPolicy p = new IamPolicy();
        public Builder name(String v) { p.setName(v); return this; }
        public Builder policyDocument(String v) { p.setPolicyDocument(v); return this; }
        public Builder owner(String v) { p.setOwner(v); return this; }
        public IamPolicy build() { return p; }
    }
}
