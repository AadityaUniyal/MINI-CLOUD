package com.minicloud.common.dto;

public class IamGroupDto {
    private String name;
    private int userCount;

    public IamGroupDto() {}

    public IamGroupDto(String name, int userCount) {
        this.name = name;
        this.userCount = userCount;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getUserCount() { return userCount; }
    public void setUserCount(int userCount) { this.userCount = userCount; }
}
