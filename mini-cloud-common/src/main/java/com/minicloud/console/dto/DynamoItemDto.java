package com.minicloud.console.dto;

public class DynamoItemDto {
    private String key;
    private String value;

    public DynamoItemDto() {}

    public DynamoItemDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
