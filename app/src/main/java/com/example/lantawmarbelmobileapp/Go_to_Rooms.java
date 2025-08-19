package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Go_to_Rooms extends AppCompatActivity {

    private LinearLayout standardRoomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_rooms);

        // Reference the StandardRoom layout by ID
        standardRoomLayout = findViewById(R.id.StandardRoom);

        // Set click listener
        standardRoomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Standard Room Details Activity
                Intent intent = new Intent(Go_to_Rooms.this, Go_to_Standard_room_details.class);
                startActivity(intent);
            }
        });
    }
}
