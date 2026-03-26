package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sqs_messages")
public class SqsMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    private SqsQueue queue;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String status; // PENDING, PROCESSING, COMPLETED
    private LocalDateTime createdAt;

    public SqsMessage() {}

    public Long getId() { return id; }
    public SqsQueue getQueue() { return queue; }
    public void setQueue(SqsQueue v) { this.queue = v; }
    public String getBody() { return body; }
    public void setBody(String v) { this.body = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
