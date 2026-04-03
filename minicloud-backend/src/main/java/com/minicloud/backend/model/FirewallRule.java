package com.minicloud.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "firewall_rules")
public class FirewallRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String protocol;
    private String portRange;
    private String source;
    private String description;

    public FirewallRule() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }
    public String getPortRange() { return portRange; }
    public void setPortRange(String portRange) { this.portRange = portRange; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final FirewallRule f = new FirewallRule();
        public Builder type(String v) { f.type = v; return this; }
        public Builder protocol(String v) { f.protocol = v; return this; }
        public Builder portRange(String v) { f.portRange = v; return this; }
        public Builder source(String v) { f.source = v; return this; }
        public Builder description(String v) { f.description = v; return this; }
        public FirewallRule build() { return f; }
    }
}
