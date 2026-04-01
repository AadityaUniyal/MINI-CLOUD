package com.minicloud.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sns_subscriptions")
public class SnsSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private SnsTopic topic;

    private String protocol; // sqs, lambda, email (mock)
    private String endpoint; // ARN or email address

    public SnsSubscription() {}

    public Long getId() { return id; }
    public SnsTopic getTopic() { return topic; }
    public void setTopic(SnsTopic v) { this.topic = v; }
    public String getProtocol() { return protocol; }
    public void setProtocol(String v) { this.protocol = v; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String v) { this.endpoint = v; }
}
