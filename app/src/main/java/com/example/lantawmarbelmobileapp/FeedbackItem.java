// FeedbackItem.java
package com.example.lantawmarbelmobileapp;

public class FeedbackItem {
    private int feedbackID;
    private String message;
    private String date;
    private int rating;
    private String status;
    private int guestID;

    public int getFeedbackID() { return feedbackID; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public int getRating() { return rating; }
    public String getStatus() { return status; }
    public int getGuestID() { return guestID; }
}
