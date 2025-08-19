package com.example.lantawmarbelmobileapp;

import java.util.List;

public class Message {
    private String content;
    private boolean isUser;
    private long timestamp;
    private List<String> quickReplies;
    private boolean isTyping;

    public Message(String content, boolean isUser, long timestamp, List<String> quickReplies) {
        this.content = content;
        this.isUser = isUser;
        this.timestamp = timestamp;
        this.quickReplies = quickReplies;
        this.isTyping = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(List<String> quickReplies) {
        this.quickReplies = quickReplies;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
