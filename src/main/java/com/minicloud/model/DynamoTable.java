package com.minicloud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dynamo_tables")
public class DynamoTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tableName;

    private String partitionKey;
    private String sortKey;
    private String status; // CREATING, ACTIVE, DELETING
    private String owner;
    private LocalDateTime createdAt;
    private Long itemCount;
    private Long sizeBytes;

    public DynamoTable() {}

    public static Builder builder() { return new Builder(); }

    public Long getId() { return id; }
    public String getTableName() { return tableName; }
    public String getPartitionKey() { return partitionKey; }
    public String getSortKey() { return sortKey; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class Builder {
        private final DynamoTable t = new DynamoTable();
        public Builder tableName(String v) { t.tableName = v; return this; }
        public Builder partitionKey(String v) { t.partitionKey = v; return this; }
        public Builder sortKey(String v) { t.sortKey = v; return this; }
        public Builder status(String v) { t.status = v; return this; }
        public Builder owner(String v) { t.owner = v; return this; }
        public Builder createdAt(LocalDateTime v) { t.createdAt = v; return this; }
        public DynamoTable build() { 
            t.itemCount = 0L;
            t.sizeBytes = 0L;
            return t; 
        }
    }
}
