package com.example.lantawmarbelmobileapp;
public class NotificationModel {
    private String id;
    private String title;
    private String body;
    private String created_at;
    private boolean read;

    public NotificationModel(String id, String title, String body, String created_at, boolean read) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.created_at = created_at;
        this.read = read;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getCreatedAt() { return created_at; }
    public boolean isRead() { return read; }
}
