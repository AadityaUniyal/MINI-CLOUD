package com.minicloud.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "security_groups")
public class SecurityGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private String vpcId;
    private String owner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "security_group_id")
    private List<FirewallRule> rules;

    public SecurityGroup() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public List<FirewallRule> getRules() { return rules; }
    public void setRules(List<FirewallRule> rules) { this.rules = rules; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final SecurityGroup s = new SecurityGroup();
        public Builder name(String v) { s.name = v; return this; }
        public Builder description(String v) { s.description = v; return this; }
        public Builder vpcId(String v) { s.vpcId = v; return this; }
        public Builder owner(String v) { s.owner = v; return this; }
        public Builder rules(List<FirewallRule> v) { s.rules = v; return this; }
        public SecurityGroup build() { return s; }
    }
}
