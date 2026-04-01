package com.minicloud.console.dto;

public class QueueDto {
    private String name;
    private String url;
    private int messagesAvailable;

    public QueueDto() {}

    public QueueDto(String name, String url, int messagesAvailable) {
        this.name = name;
        this.url = url;
        this.messagesAvailable = messagesAvailable;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getMessagesAvailable() { return messagesAvailable; }
    public void setMessagesAvailable(int messagesAvailable) { this.messagesAvailable = messagesAvailable; }
}
