package com.minicloud.console.dto;

public class LambdaDto {
    private Long id;
    private String name;
    private String runtime;
    private String status;

    public LambdaDto() {}

    public LambdaDto(Long id, String name, String runtime, String status) {
        this.id = id;
        this.name = name;
        this.runtime = runtime;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRuntime() { return runtime; }
    public void setRuntime(String runtime) { this.runtime = runtime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
