package com.example.lantawmarbelmobileapp;

// BillingDTO.java
import java.util.List;

public class BillingDTO {
    public int billingID;
    public int bookingID;
    public String totalamount;
    public String datebilled;
    public String status;
    public List<PaymentDTO> payments;
}
