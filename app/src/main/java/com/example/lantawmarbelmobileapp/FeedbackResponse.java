// FeedbackResponse.java
package com.example.lantawmarbelmobileapp;

import java.util.List;

public class FeedbackResponse {
    private boolean success;
    private String message;
    private FeedbackItem data;
    private List<FeedbackItem> dataList;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public FeedbackItem getData() { return data; }
    public List<FeedbackItem> getDataList() { return dataList; }
}
