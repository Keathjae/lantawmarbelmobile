package com.example.lantawmarbelmobileapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRListActivity extends AppCompatActivity {
    private static final int ADD_FEEDBACK_REQUEST = 1001;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private RecyclerView recyclerView;
    private QRCodeAdapter adapter;
    private int getguestID() {
        return sharedPreferences.getInt("guestID", -1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlist);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerViewQR);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int guestID =getguestID(); // sample, replace with logged-in guest id

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getQRCodesByGuest(guestID).enqueue(new Callback<ApiResponse<List<QRCode>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<QRCode>>> call, Response<ApiResponse<List<QRCode>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    adapter = new QRCodeAdapter(QRListActivity.this, response.body().data);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(QRListActivity.this, "No records found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<QRCode>>> call, Throwable t) {
                Toast.makeText(QRListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
