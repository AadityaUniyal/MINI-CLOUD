package com.minicloud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "waf_rules")
public class WafRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ruleName;

    private String action; // ALLOW, BLOCK
    private String scope; // IP, USER_AGENT
    private String pattern; // e.g. 192.168.1.1 or Mozilla/5.0
    private String owner;

    public WafRule() {}

    public Long getId() { return id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String v) { this.ruleName = v; }
    public String getAction() { return action; }
    public void setAction(String v) { this.action = v; }
    public String getScope() { return scope; }
    public void setScope(String v) { this.scope = v; }
    public String getPattern() { return pattern; }
    public void setPattern(String v) { this.pattern = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
}
