package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView txtBookingID, txtStatus, txtBookingDates, txtGuests, txtTotalPrice;
    private TextView txtRooms, txtCottages, txtMenus, txtBilling, txtPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // Bind views
        txtBookingID = findViewById(R.id.txtBookingID);
        txtStatus = findViewById(R.id.txtStatus);
        txtBookingDates = findViewById(R.id.txtBookingDates);
        txtGuests = findViewById(R.id.txtGuests);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtRooms = findViewById(R.id.txtRooms);
        txtCottages = findViewById(R.id.txtCottages);
        txtMenus = findViewById(R.id.txtMenus);
        txtBilling = findViewById(R.id.txtBilling);
        txtPayments = findViewById(R.id.txtPayments);

// Get booking ID from intent
        int bookingId = getIntent().getIntExtra("booking_id", -1);

        if (bookingId == -1) {
            Toast.makeText(this, "No booking ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBookingDetails(bookingId);
    }
    private void loadBookingDetails(int bookingId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<BookingRequest> call = apiService.getBookingbyId(bookingId);

        call.enqueue(new Callback<BookingRequest>() {
            @Override
            public void onResponse(Call<BookingRequest> call, Response<BookingRequest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayBookingDetails(response.body());
                } else {
                    Toast.makeText(BookingDetailActivity.this, "Failed to load booking details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookingRequest> call, Throwable t) {
                Log.e("BookingDetailActivity", "Error loading booking: " + t.getMessage());
                Toast.makeText(BookingDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayBookingDetails(BookingRequest booking) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        // --- Format booking dates (just date) ---
        String formattedStart = formatDateSafe(booking.bookingStart);
        String formattedEnd = formatDateSafe(booking.bookingEnd);

        // --- Basic Info ---
        txtBookingID.setText("Booking #" + booking.bookingID);
        txtBookingDates.setText("Dates: " + formattedStart + " → " + formattedEnd);
        txtGuests.setText("Guests: " + booking.adultGuest + " Adults, " + booking.childGuest + " Children");
        txtTotalPrice.setText("Total Price: " + currencyFormat.format(booking.totalPrice));

        // --- Status Color ---
        if (booking.status != null) {
            switch (booking.status.toLowerCase()) {
                case "pending":
                    txtStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    break;
                case "confirmed":
                case "paid":
                    txtStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case "cancelled":
                    txtStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
                default:
                    txtStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
            }
            txtStatus.setText("Status: " + booking.status);
        }

        // --- Rooms ---
        if (booking.roomBookings != null && !booking.roomBookings.isEmpty()) {
            StringBuilder sbRooms = new StringBuilder("Rooms:\n");
            for (RoomBooking room : booking.roomBookings) {
                double price = 0;
                if (room.room != null && room.room.getPrice() != null) {
                    try { price = Double.parseDouble(room.room.getPrice()); }
                    catch (NumberFormatException e) { e.printStackTrace(); }
                }
                String bookingDate = DateUtils.toIsoDate(room.bookingDate);
                sbRooms.append(" - Room ID:  ").append(room.roomID).append("\n")
                        .append(" - Price: ").append(currencyFormat.format(price)).append("\n")
                        .append(" - Date: ").append(bookingDate).append("\n");
            }
            txtRooms.setText(sbRooms.toString());
        } else txtRooms.setText("Rooms: None");

        // --- Cottages ---
        if (booking.cottageBookings != null && !booking.cottageBookings.isEmpty()) {
            StringBuilder sbCottages = new StringBuilder("Cottages:\n");
            for (CottageBooking cottage : booking.cottageBookings) {
                double price = 0;
                if (cottage.cottage != null && cottage.cottage.getPrice() != null) {
                    try { price = Double.parseDouble(cottage.cottage.getPrice()); }
                    catch (NumberFormatException e) { e.printStackTrace(); }
                }
                String bookingDate = DateUtils.toIsoDate(cottage.bookingDate);
                sbCottages.append(" - Cottage ID:  ").append(cottage.cottageID).append("\n")
                        .append(" - Price: ").append(currencyFormat.format(price)).append("\n")
                        .append(" - Date: ").append(bookingDate).append("\n");
            }
            txtCottages.setText(sbCottages.toString());
        } else txtCottages.setText("Cottages: None");

        // --- Menus ---
        if (booking.menuBookings != null && !booking.menuBookings.isEmpty()) {
            StringBuilder sbMenus = new StringBuilder("Menus:\n");
            for (MenuBooking menu : booking.menuBookings) {
                double price = 0;
                if (menu.menu != null && menu.menu.getPrice() != null) {
                    try { price = Double.parseDouble(menu.menu.getPrice()); }
                    catch (NumberFormatException e) { e.printStackTrace(); }
                }
                String bookingDate = DateUtils.toIsoDate(menu.bookingDate);
                sbMenus.append(" - Menu ID:  ").append(menu.menu_id).append("\n")
                        .append(" - Quantity: ").append(menu.quantity).append("\n")
                        .append(" - Price: ").append(currencyFormat.format(price)).append("\n")
                        .append(" - Date: ").append(bookingDate).append("\n");
            }
            txtMenus.setText(sbMenus.toString());
        } else txtMenus.setText("Menus: None");

        // --- Billing ---
        if (booking.billing != null) {
            Billing billing = booking.billing;
            double totalAmount = 0;
            if (billing.totalAmount != null) {
                try { totalAmount = Double.parseDouble(billing.totalAmount); }
                catch (NumberFormatException e) { e.printStackTrace(); }
            }
            String dateBilled = formatDateSafe(billing.dateBilled);
            StringBuilder sbBilling = new StringBuilder();
            sbBilling.append("Billing:\n")
                    .append(" - Billing ID: ").append(billing.billingID).append("\n")
                    .append(" - Total Amount: ").append(currencyFormat.format(totalAmount)).append("\n")
                    .append(" - Date Billed: ").append(dateBilled).append("\n")
                    .append(" - Status: ").append(billing.status);
            txtBilling.setText(sbBilling.toString());

            // --- Payments ---
            if (billing.payments != null && !billing.payments.isEmpty()) {
                StringBuilder sbPayments = new StringBuilder("Payments:\n");
                for (Payment payment : billing.payments) {
                    double tendered = payment.totalTender;
                    double change = payment.totalChange;
                    String datePayment = formatDateSafe(payment.datePayment);
                    sbPayments.append("  - Payment ID:").append(payment.paymentID).append("\n")
                            .append(" - Tendered: ").append(currencyFormat.format(tendered)).append("\n")
                            .append(" - Change: ").append(currencyFormat.format(change)).append("\n")
                            .append(" - Date: ").append(datePayment).append("\n")
                            .append(" - Ref: ").append(payment.refNumber).append("\n");
                }
                txtPayments.setText(sbPayments.toString());
            } else txtPayments.setText("Payments: None");
        } else {
            txtBilling.setText("Billing: None");
            txtPayments.setText("Payments: None");
        }
    }

    // Helper to parse date string safely and display only date
    private String formatDateSafe(String dateStr) {
        if (dateStr == null) return "-";
        SimpleDateFormat backendFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = backendFormat.parse(dateStr);
            if (date != null) return DateUtils.toIsoDate(date); // just date
        } catch (Exception e) { e.printStackTrace(); }
        return dateStr;
    }
}
