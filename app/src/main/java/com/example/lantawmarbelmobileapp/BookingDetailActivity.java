package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView txtBookingID, txtGuestName, txtGuestEmail, txtDates, txtGuests, txtRooms, txtTotal, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        txtBookingID = findViewById(R.id.txtBookingID);
        txtGuestName = findViewById(R.id.txtGuestName);
        txtGuestEmail = findViewById(R.id.txtGuestEmail);
        txtDates = findViewById(R.id.txtDates);
        txtGuests = findViewById(R.id.txtGuests);
        txtRooms = findViewById(R.id.txtRooms);
        txtTotal = findViewById(R.id.txtTotal);
        txtStatus = findViewById(R.id.txtStatus);

        Booking booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking != null) populateDetails(booking);
    }

    private void populateDetails(Booking booking) {
        txtBookingID.setText("Booking ID: " + booking.bookingID);
        txtGuestName.setText(booking.guest.firstName + " " + booking.guest.lastName);
        txtGuestEmail.setText(booking.guest.email);
        txtDates.setText("From: " + booking.bookingStart + " To: " + booking.bookingEnd);
        txtGuests.setText("Adults: " + booking.adultGuest + " | Children: " + booking.childGuest);

        StringBuilder rooms = new StringBuilder();
        for (Room rb : booking.roomBookings) {
            rooms.append("Room ").append(rb.getRoomnum()).append("\n");
        }
        txtRooms.setText(rooms.toString());

        txtTotal.setText("Total: ₱" + booking.totalPrice);
        txtStatus.setText("Status: " + booking.status);
    }
}
