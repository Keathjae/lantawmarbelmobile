package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Go_Tap_Silog extends AppCompatActivity {

    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_tap_silog);

        orderButton = findViewById(R.id.btnOrder);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to next activity
                Intent intent = new Intent(Go_Tap_Silog.this, Go_to_food_payment.class);
                startActivity(intent);
            }
        });
    }
}
