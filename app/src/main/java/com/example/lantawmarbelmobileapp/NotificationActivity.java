package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lantawmarbelmobileapp.NotificationAdapter;
import com.example.lantawmarbelmobileapp.NotificationModel;
import com.example.lantawmarbelmobileapp.ApiClient;
import com.example.lantawmarbelmobileapp.ApiService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";



    private String getGuestFullName() {
        return sharedPreferences.getString(KEY_USER_NAME, "Guest");
    }

    private int getUserID() {
        return sharedPreferences.getInt("userID", -1);
    }

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerNotifications);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getUserID(); // Replace with dynamic user ID from your login/session
        fetchNotifications(userId);
    }
    private int extractBookingId(String title) {
        if (title == null) return -1;

        Pattern pattern = Pattern.compile("#(\\d+)");
        Matcher matcher = pattern.matcher(title);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    private void fetchNotifications(int userId) {
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getNotifications(userId).enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new NotificationAdapter(response.body(), notification -> {
                        int bookingId = extractBookingId(notification.getTitle());
                        if (bookingId != -1) {
                            Intent intent = new Intent(NotificationActivity.this, BookingDetailActivity.class);
                            intent.putExtra("booking_id", bookingId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(NotificationActivity.this, "No booking ID found", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(NotificationActivity.this, "No notifications found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Notifications", "Error: " + t.getMessage());
                Toast.makeText(NotificationActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
