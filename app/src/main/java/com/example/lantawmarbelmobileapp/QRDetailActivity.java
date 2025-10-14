package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class QRDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdetail);

        ImageView imgQR = findViewById(R.id.imgQR);
        TextView txtAmenity = findViewById(R.id.txtAmenity);
        TextView txtAccessDate = findViewById(R.id.txtAccessDate);

        String qrUrl = getIntent().getStringExtra("qr_url");
        String accessDate = getIntent().getStringExtra("accessdate");
        String amenity = getIntent().getStringExtra("amenity");

        Glide.with(this).load(qrUrl).into(imgQR);
        txtAmenity.setText("Amenity: " + amenity);
        txtAccessDate.setText("Access Date: " + accessDate);
    }
}
