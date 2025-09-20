package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Booking implements Serializable {

    @SerializedName("bookingID")
    public int bookingID;

    @SerializedName("guestID")
    public int guestID;

    @SerializedName("guestamount")
    public int guestAmount;

    @SerializedName("childguest")
    public int childGuest;

    @SerializedName("adultguest")
    public int adultGuest;

    @SerializedName("totalprice")
    public String totalPrice;

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

    @SerializedName("room_bookings")
    public List<RoomBooking> roomBookings;

    @SerializedName("cottage_bookings")
    public List<CottageBooking> cottageBookings;

    @SerializedName("amenity_book")
    public List<AmenityBook> amenityBook;

    @SerializedName("billing")
    public List<Billing> billing;

    // Nested Serializable classes
    public static class Guest implements Serializable {
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

    public static class RoomBooking implements Serializable {
        @SerializedName("roombookID")
        public int roombookID;

        @SerializedName("bookingID")
        public int bookingID;

        @SerializedName("roomID")
        public int roomID; // sometimes returned directly in API

        @SerializedName("room")
        public Room room;
    }

    public static class Room implements Serializable {
        @SerializedName("roomID")
        public int roomID;

        @SerializedName("roomnum")
        public int roomNum;
    }

    public static class CottageBooking implements Serializable {
        // Add fields if your API returns them
    }

    public static class AmenityBook implements Serializable {
        // Add fields if your API returns them
    }

    public static class Billing implements Serializable {
        @SerializedName("bookingID")
        public int bookingID;

        @SerializedName("totalamount")
        public String totalAmount;

        @SerializedName("status")
        public String status;
    }
}
