package com.example.lantawmarbelmobileapp;
public class Chat {
    private int chatID;
    private String chat;
    private String datesent;
    private String reply;
    private String datereplied;
    private String status;
    private int guestID;
    private Integer staffID;

    // Getters
    public int getChatID() { return chatID; }
    public String getChat() { return chat; }
    public String getDatesent() { return datesent; }
    public String getReply() { return reply; }
    public String getDatereplied() { return datereplied; }
    public String getStatus() { return status; }
    public int getGuestID() { return guestID; }
    public Integer getStaffID() { return staffID; }

    // Setters
    public void setChatID(int chatID) { this.chatID = chatID; }
    public void setChat(String chat) { this.chat = chat; }
    public void setDatesent(String datesent) { this.datesent = datesent; }
    public void setReply(String reply) { this.reply = reply; }
    public void setDatereplied(String datereplied) { this.datereplied = datereplied; }
    public void setStatus(String status) { this.status = status; }
    public void setGuestID(int guestID) { this.guestID = guestID; }
    public void setStaffID(Integer staffID) { this.staffID = staffID; }
}

