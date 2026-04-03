package com.minicloud.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dynamo_items")
public class DynamoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private DynamoTable table;

    @Column(name = "item_key", nullable = false)
    private String key; // PartitionKey value

    @Column(columnDefinition = "TEXT")
    private String valueBlob; // JSON content

    public DynamoItem() {}

    public DynamoItem(DynamoTable table, String key, String valueBlob) {
        this.table = table;
        this.key = key;
        this.valueBlob = valueBlob;
    }

    public Long getId() { return id; }
    public DynamoTable getTable() { return table; }
    public String getKey() { return key; }
    public String getValueBlob() { return valueBlob; }
    public void setValueBlob(String v) { this.valueBlob = v; }
}
