package com.example.lantawmarbelmobileapp;
// BookingRequest.java
import java.util.List;

public class BookingRequest {
    public int bookingID;
    public int guestID;
    public int childGuest;
    public int adultGuest;
    public int guestamount;
    public double totalPrice;
    public String bookingCreated;
    public String bookingStart;
    public String bookingEnd;
    public String status;
    public List<MenuDTO> menuBookings;

    public GuestDTO guest;
    public AmenityDTO amenity;  // ✅ Single Amenity
    public List<RoomDTO> roomBookings;
    public List<CottageDTO> cottageBookings;
    public BillingDTO billing;
}
