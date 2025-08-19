package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Go_to_food_payment extends AppCompatActivity {

    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_food_payment);

        btnProceed = findViewById(R.id.btnProceed);

        btnProceed.setEnabled(true); // Optional: if you want it clickable immediately

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the next activity (replace FinalConfirmationActivity with your actual class)
                Intent intent = new Intent(Go_to_food_payment.this, Go_to_Success_message.class);
                startActivity(intent);
            }
        });
    }
}
