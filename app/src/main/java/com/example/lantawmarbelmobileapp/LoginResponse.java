package com.example.lantawmarbelmobileapp;

public class LoginResponse {
    public boolean success;
    public String message;
    public String token_type;
    public User user;
    public Profile profile; // <-- use profile instead of guest/staff
    public String token; // optional if backend adds it later

    // Represents the user object
    public static class User {
        public int id;
        public String username;
        public String role; // "guest" or "staff"
    }

    // Profile can be either Guest or Staff depending on role
    public static class Profile {
        public Integer guestID;   // will exist if role = guest
        public Integer staffID;   // will exist if role = staff
        public String firstname;
        public String lastname;
        public String email;
        public String role; // only for staff level, e.g. "manager"
    }
}
