package com.example.lantawmarbelmobileapp;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("bookingID")
    public int bookingID;

    @SerializedName("guestID")
    public int guestID;

    @SerializedName("childguest")
    public int childGuest;

    @SerializedName("adultguest")
    public int adultGuest;

    @SerializedName("guestamount")
    public int guestamount;

    @SerializedName("totalprice")
    public double totalPrice;

    @SerializedName("bookingcreated")
    public String bookingCreated;

    @SerializedName("bookingstart")
    public String bookingStart;

    @SerializedName("bookingend")
    public String bookingEnd;
    @SerializedName("booking_type")
    public String booking_type;

    @SerializedName("status")
    public String status;

    @SerializedName("guest")
    public Guest guest;

    @SerializedName("amenity")
    public Amenity amenity;

    @SerializedName("room_bookings")
    public List<RoomBooking> roomBookings;

    @SerializedName("cottage_bookings")
    public List<CottageBooking> cottageBookings;

    @SerializedName("menu_bookings")
    public List<MenuBooking> menuBookings;

    @SerializedName("billing")
    public Billing billing;
}
