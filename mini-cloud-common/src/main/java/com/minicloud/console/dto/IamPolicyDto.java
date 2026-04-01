package com.minicloud.console.dto;

public class IamPolicyDto {
    private String name;
    private String arn;
    private String description;

    public IamPolicyDto() {}

    public IamPolicyDto(String name, String arn, String description) {
        this.name = name;
        this.arn = arn;
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArn() { return arn; }
    public void setArn(String arn) { this.arn = arn; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
