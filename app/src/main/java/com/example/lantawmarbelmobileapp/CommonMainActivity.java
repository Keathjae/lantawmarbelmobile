package com.example.lantawmarbelmobileapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CommonMainActivity extends AppCompatActivity {

    private ImageButton BookingButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_common_main);

        BookingButton = findViewById(R.id.reservationButton);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new TabPagerAdapter(CommonMainActivity.this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Rooms"); break;
                        case 1: tab.setText("Amenities"); break;

                        case 2: tab.setText("Cottages"); break;
                        case 3: tab.setText("Menu"); break;
                    }
                }).attach();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
BookingButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        showLoginDialog();
    }
});
    }

    private void showLoginDialog() {
            Intent intent = new Intent(CommonMainActivity.this, Go_To_Login_Signup.class);
                startActivity(intent);
    }
}