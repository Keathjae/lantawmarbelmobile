package com.example.lantawmarbelmobileapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookingDTO implements Serializable {

    // Base booking info
    public int bookingID;
    public int guestID;
    public int childGuest;
    public int adultGuest;
    public int guestAmount;
    public double totalPrice;
    public String bookingCreated;
    public String bookingStart;
    public String bookingEnd;
    public String bookingType;
    public String status;

    // Amenity info
    public Amenity amenity;

    // Collections
    public List<RoomBookingDTO> roomBookings = new ArrayList<>();
    public List<CottageBookingDTO> cottageBookings = new ArrayList<>();
    public List<MenuBookingDTO> menuBookings = new ArrayList<>();

    // Billing
    public BillingDTO billing;

    // ==================== Nested DTOs ====================
    public static class RoomBookingDTO implements Serializable {
        public int roomID;
        public Double price;
        public String bookingDate; // "YYYY-MM-DD" format
    }

    public static class CottageBookingDTO implements Serializable {
        public int cottageID;
        public Double price;
        public String bookingDate;
    }

    public static class MenuBookingDTO implements Serializable {
        public int menuID;
        public int quantity;
        public double price;
        public String bookingDate;
    }

    public static class BillingDTO implements Serializable {
        public int billingID;
        public int bookingID;
        public double totalAmount;
        public String dateBilled;
        public String status;
        public List<PaymentDTO> payments = new ArrayList<>();
    }

    public static class PaymentDTO implements Serializable {
        public int paymentID;
        public double totaltender;
        public double totalchange;
        public String datePayment;
        public String refNumber;
    }
}
