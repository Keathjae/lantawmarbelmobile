package com.example.lantawmarbelmobileapp;

import java.io.Serializable;
import java.util.Date;

// ==================== RoomBooking ====================
public class RoomBooking implements Serializable {
    private static final long serialVersionUID = 1L;

    public int roombookID;
    public int bookingID;
    public int roomID; // matches JSON
    public Room room;  // optional, may be null
    public Date bookingDate;
}
