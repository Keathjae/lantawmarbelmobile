package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Guest implements Serializable {
    @SerializedName("guestID")
    public int guestID;

    @SerializedName("firstname")
    public String firstName;

    @SerializedName("lastname")
    public String lastName;

    @SerializedName("email")
    public String email;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}