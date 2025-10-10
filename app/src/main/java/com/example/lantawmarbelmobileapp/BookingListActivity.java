package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_GUEST_ID = "guestID";
    private static final String KEY_GUEST_EMAIL = "guestEmail";

    private TextView emptyText;

    private String getGuestFullName() {
        return sharedPreferences.getString(KEY_USER_NAME, "Guest");
    }

    private int getGuestID() {
        return sharedPreferences.getInt(KEY_GUEST_ID, -1);
    }

    private String getGuestEmail() {
        return sharedPreferences.getString(KEY_GUEST_EMAIL, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        recyclerView = findViewById(R.id.recyclerBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyText = findViewById(R.id.emptyText);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Button btnNewBooking = findViewById(R.id.btnNewBooking);
        btnNewBooking.setOnClickListener(v -> {
            Intent intent = new Intent(BookingListActivity.this, BookingActivity.class);
            startActivity(intent);
        });

        fetchBookings();
    }
    private void fetchBookings() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<BookingRequest>> call = apiService.getBookingsForGuest(getGuestID());

        call.enqueue(new Callback<List<BookingRequest>>() {
            @Override
            public void onResponse(Call<List<BookingRequest>> call, Response<List<BookingRequest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookingRequest> bookings = response.body();

                    if (bookings.isEmpty()) {
                        emptyText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter = new BookingAdapter(bookings, booking -> {
                            Intent intent = new Intent(BookingListActivity.this, BookingDetailActivity.class);
                            intent.putExtra("booking_id", booking.bookingID); // pass entire object
                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    emptyText.setText("No bookings found.");
                    emptyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<BookingRequest>> call, Throwable t) {
                emptyText.setText("Unable to load bookings.");
                emptyText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

//    private void openBookingDetail(BookingRequest booking) {
//        Intent intent = new Intent(this, BookingActivity.class);
//        intent.putExtra("booking", booking);
//        startActivity(intent);
//    }
}
