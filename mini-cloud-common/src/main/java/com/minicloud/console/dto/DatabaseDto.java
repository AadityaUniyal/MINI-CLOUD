package com.minicloud.console.dto;

public class DatabaseDto {
    private Long id;
    private String name;
    private String engine;
    private String status;

    public DatabaseDto() {}

    public DatabaseDto(Long id, String name, String engine, String status) {
        this.id = id;
        this.name = name;
        this.engine = engine;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEngine() { return engine; }
    public void setEngine(String engine) { this.engine = engine; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
