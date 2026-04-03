package com.minicloud.common.dto;

public class FileDto {
    private String name;
    private String size;
    private String lastModified;

    public FileDto() {}

    public FileDto(String name, String size, String lastModified) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getLastModified() { return lastModified; }
    public void setLastModified(String lastModified) { this.lastModified = lastModified; }
}
