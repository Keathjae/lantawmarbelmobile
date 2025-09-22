package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Billing implements Serializable {
    @SerializedName("billingID")
    public int billingID;

    @SerializedName("bookingID")
    public int bookingID;

    @SerializedName("totalamount")
    public String totalAmount;

    @SerializedName("datebilled")
    public String dateBilled;

    @SerializedName("status")
    public String status;

    @SerializedName("payments")
    public List<Payment> payments;
}