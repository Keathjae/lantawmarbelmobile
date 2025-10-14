// FeedbackActivity.java
package com.example.lantawmarbelmobileapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lantawmarbelmobileapp.FeedbackResponse;
import com.example.lantawmarbelmobileapp.ApiClient;
import com.example.lantawmarbelmobileapp.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private EditText feedbackMessage;
    private RatingBar ratingBar;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        feedbackMessage = findViewById(R.id.feedbackMessage);
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> sendFeedback());
    }
    private int getguestID() {
        return sharedPreferences.getInt("guestID", -1);
    }
    private void sendFeedback() {
        String message = feedbackMessage.getText().toString().trim();
        float rating = ratingBar.getRating();
        int guestID =getguestID(); // you can get from logged-in user

        if (message.isEmpty() || rating == 0) {
            Toast.makeText(this, "Please enter a message and rating", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("rating", rating);
        body.put("guestID", guestID);

        apiService.sendFeedback(body).enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(FeedbackActivity.this, "Feedback sent successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // 👈 notify FeedbackListActivity
                    finish();
                } else {
                    Toast.makeText(FeedbackActivity.this, "Failed to send feedback", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                Toast.makeText(FeedbackActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
