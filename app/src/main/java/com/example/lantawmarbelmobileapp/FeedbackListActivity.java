package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackListActivity extends AppCompatActivity {
    private static final int ADD_FEEDBACK_REQUEST = 1001;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";

    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private Button btnAddFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerViewFeedback);
        btnAddFeedback = findViewById(R.id.btnAddFeedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int guestId = getguestID();
        fetchFeedbackList(guestId);

        btnAddFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(FeedbackListActivity.this, FeedbackActivity.class);
            startActivityForResult(intent, ADD_FEEDBACK_REQUEST);
        });
    }

    private int getguestID() {
        return sharedPreferences.getInt("guestID", -1);
    }

    private void fetchFeedbackList(int guestid) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getFeedbackList(guestid).enqueue(new Callback<FeedbackListResponse>() {
            @Override
            public void onResponse(Call<FeedbackListResponse> call, Response<FeedbackListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    adapter = new FeedbackAdapter(response.body().getData());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(FeedbackListActivity.this, "No feedback found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedbackListResponse> call, Throwable t) {
                Toast.makeText(FeedbackListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FeedbackList", "Error fetching feedback", t);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FEEDBACK_REQUEST && resultCode == RESULT_OK) {
            fetchFeedbackList(getguestID()); // Refresh list after adding feedback
        }
    }
}
