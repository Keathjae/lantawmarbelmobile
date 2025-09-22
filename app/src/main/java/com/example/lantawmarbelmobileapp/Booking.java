package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {

    @SerializedName("bookingID")
    public int bookingID;

    @SerializedName("guestID")
    public int guestID;

    @SerializedName("childguest")
    public int childGuest;

    @SerializedName("adultguest")
    public int adultGuest;

    @SerializedName("totalprice")
    public double totalPrice;

    @SerializedName("bookingcreated")
    public String bookingCreated;

    @SerializedName("bookingstart")
    public String bookingStart;

    @SerializedName("bookingend")
    public String bookingEnd;

    @SerializedName("status")
    public String status;

    @SerializedName("guest")
    public Guest guest;

    @SerializedName("amenity")
    public Amenity amenity;

    @SerializedName("room_bookings")
    public List<Room> roomBookings;

    @SerializedName("cottage_bookings")
    public List<Cottage> cottageBookings;

    @SerializedName("menu_bookings")
    public List<Menu> menuBookings;

    @SerializedName("billing")   // ✅ added
    public Billing billing;

    // ================= Billing ================= //


    // ================= Payment ================= //

}
