package com.example.lantawmarbelmobileapp;

import java.util.List;

// =================== Booking DTO =================== //
public class BookingRequest {
    public int bookingID;
    public int guestID;
    public int guestamount;
    public int childGuest;
    public int adultGuest;
    public double totalPrice;
    public String bookingCreated;
    public String bookingStart;
    public String bookingEnd;
    public String status;

    public GuestDTO guest;

    public List<AmenityDTO> amenityBook;
    public List<RoomDTO> roomBookings;
    public List<CottageDTO> cottageBookings;
    public List<BillingDTO> billing;
    public List<MenuDTO> menuBookings;
}

