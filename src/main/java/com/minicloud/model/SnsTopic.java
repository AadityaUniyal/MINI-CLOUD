package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sns_topics")
public class SnsTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String arn;
    private String owner;
    private String displayName;
    private LocalDateTime createdAt;

    public SnsTopic() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getArn() { return arn; }
    public void setArn(String v) { this.arn = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String v) { this.displayName = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public static SnsBuilder builder() { return new SnsBuilder(); }

    public static class SnsBuilder {
        private final SnsTopic t = new SnsTopic();
        public SnsBuilder name(String v) { t.name = v; return this; }
        public SnsBuilder arn(String v) { t.arn = v; return this; }
        public SnsBuilder owner(String v) { t.owner = v; return this; }
        public SnsBuilder displayName(String v) { t.displayName = v; return this; }
        public SnsBuilder createdAt(LocalDateTime v) { t.createdAt = v; return this; }
        public SnsTopic build() { return t; }
    }
}
