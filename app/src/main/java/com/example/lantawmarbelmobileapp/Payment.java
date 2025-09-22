package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payment implements Serializable {
    @SerializedName("paymentID")
    public int paymentID;

    @SerializedName("totaltender")
    public double totalTender;

    @SerializedName("totalchange")
    public double totalChange;

    @SerializedName("datepayment")
    public String datePayment;

    @SerializedName("refNumber")
    public String refNumber;
    public Payment() {}

    public Payment(double totaltender, double totalchange, String datepayment, String refNumber) {
        this.totalTender = totaltender;
        this.totalChange = totalchange;
        this.datePayment = datepayment;
        this.refNumber = refNumber;
    }
}