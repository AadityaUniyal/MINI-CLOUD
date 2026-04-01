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
    private String name;

    private String partitionKey;
    private String sortKey;
    private String status; // ACTIVE, CREATING
    private String owner;
    private long itemCount;
    private long sizeBytes;
    private LocalDateTime createdAt;

    public DynamoTable() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { this.name = v; }
    public String getPartitionKey() { return partitionKey; }
    public void setPartitionKey(String v) { this.partitionKey = v; }
    public String getSortKey() { return sortKey; }
    public void setSortKey(String v) { this.sortKey = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getOwner() { return owner; }
    public void setOwner(String v) { this.owner = v; }
    public long getItemCount() { return itemCount; }
    public void setItemCount(long v) { this.itemCount = v; }
    public long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(long v) { this.sizeBytes = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public static DynamoTableBuilder builder() { return new DynamoTableBuilder(); }

    public static class DynamoTableBuilder {
        private final DynamoTable t = new DynamoTable();
        public DynamoTableBuilder name(String val) { t.name = val; return this; }
        public DynamoTableBuilder tableName(String val) { t.name = val; return this; }
        public DynamoTableBuilder partitionKey(String val) { t.partitionKey = val; return this; }
        public DynamoTableBuilder sortKey(String val) { t.sortKey = val; return this; }
        public DynamoTableBuilder status(String val) { t.status = val; return this; }
        public DynamoTableBuilder owner(String val) { t.owner = val; return this; }
        public DynamoTableBuilder itemCount(long val) { t.itemCount = val; return this; }
        public DynamoTableBuilder sizeBytes(long val) { t.sizeBytes = val; return this; }
        public DynamoTableBuilder createdAt(LocalDateTime val) { t.createdAt = val; return this; }
        public DynamoTable build() { return t; }
    }
}
