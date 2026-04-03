package com.minicloud.common.dto;

public class IamRoleDto {
    private String name;
    private String arn;
    private String creationDate;

    public IamRoleDto() {}

    public IamRoleDto(String name, String arn, String creationDate) {
        this.name = name;
        this.arn = arn;
        this.creationDate = creationDate;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArn() { return arn; }
    public void setArn(String arn) { this.arn = arn; }
    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }
}
