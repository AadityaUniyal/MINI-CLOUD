package com.minicloud.console.dto;

public class TopicDto {
    private String name;
    private String arn;
    private int subscriptions;

    public TopicDto() {}

    public TopicDto(String name, String arn, int subscriptions) {
        this.name = name;
        this.arn = arn;
        this.subscriptions = subscriptions;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArn() { return arn; }
    public void setArn(String arn) { this.arn = arn; }
    public int getSubscriptions() { return subscriptions; }
    public void setSubscriptions(int subscriptions) { this.subscriptions = subscriptions; }
}
