package com.minicloud.console.dto;

public class DynamoTableDto {
    private String tableName;
    private String partitionKey;
    private long itemCount;
    private String status;

    public DynamoTableDto() {}

    public DynamoTableDto(String tableName, String partitionKey, long itemCount, String status) {
        this.tableName = tableName;
        this.partitionKey = partitionKey;
        this.itemCount = itemCount;
        this.status = status;
    }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public String getPartitionKey() { return partitionKey; }
    public void setPartitionKey(String partitionKey) { this.partitionKey = partitionKey; }
    public long getItemCount() { return itemCount; }
    public void setItemCount(long itemCount) { this.itemCount = itemCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
