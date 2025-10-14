package com.example.lantawmarbelmobileapp;

import java.util.List;

public class FeedbackListResponse {
    private boolean success;
    private List<FeedbackItem> data;

    public boolean isSuccess() { return success; }
    public List<FeedbackItem> getData() { return data; }
}
