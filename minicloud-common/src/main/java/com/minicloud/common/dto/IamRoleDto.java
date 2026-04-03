package com.minicloud.common.dto;

public class IamRoleDto {
    private String name;
    private String arn;
    private String createDate;

    public IamRoleDto() {}

    public IamRoleDto(String name, String arn, String createDate) {
        this.name = name;
        this.arn = arn;
        this.createDate = createDate;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArn() { return arn; }
    public void setArn(String arn) { this.arn = arn; }
    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }
}
