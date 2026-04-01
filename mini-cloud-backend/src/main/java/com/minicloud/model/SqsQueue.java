package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sqs_queues")
public class SqsQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String queueUrl;
    private String arn;
    private String owner;
    private LocalDateTime createdAt;
    private Integer visibilityTimeout; // seconds
    private Integer messageRetentionPeriod; // seconds

    public SqsQueue() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getQueueUrl() { return queueUrl; }
    public void setQueueUrl(String v) { this.queueUrl = v; }
    public String getArn() { return arn; }
    public void setArn(String v) { this.arn = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public Integer getVisibilityTimeout() { return visibilityTimeout; }
    public void setVisibilityTimeout(Integer v) { this.visibilityTimeout = v; }
    public Integer getMessageRetentionPeriod() { return messageRetentionPeriod; }
    public void setMessageRetentionPeriod(Integer v) { this.messageRetentionPeriod = v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final SqsQueue q = new SqsQueue();
        public Builder name(String v) { q.name = v; return this; }
        public Builder queueName(String v) { q.name = v; return this; }
        public Builder queueUrl(String v) { q.queueUrl = v; return this; }
        public Builder arn(String v) { q.arn = v; return this; }
        public Builder owner(String v) { q.owner = v; return this; }
        public Builder createdAt(LocalDateTime v) { q.createdAt = v; return this; }
        public Builder visibilityTimeout(Integer v) { q.visibilityTimeout = v; return this; }
        public Builder messageRetentionPeriod(Integer v) { q.messageRetentionPeriod = v; return this; }
        public SqsQueue build() { return q; }
    }
}
