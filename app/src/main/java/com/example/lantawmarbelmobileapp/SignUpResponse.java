package com.example.lantawmarbelmobileapp;

public class SignUpResponse {
    public boolean success;
    public String message;
    public User user;      // Can be null if signup failed
    public Guest guest;    // Can be null if signup failed
}
