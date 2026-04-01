package com.minicloud.console.dto;

public class SecurityGroupDto {
    private Long id;
    private String name;
    private String description;
    private String vpcId;

    public SecurityGroupDto() {}

    public SecurityGroupDto(Long id, String name, String description, String vpcId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.vpcId = vpcId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVpcId() { return vpcId; }
    public void setVpcId(String vpcId) { this.vpcId = vpcId; }
}
