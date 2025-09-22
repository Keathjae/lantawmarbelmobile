package com.example.lantawmarbelmobileapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookingActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_GUEST_ID = "guestID";
    private static final String KEY_GUEST_EMAIL = "guestEmail";
    private int getGuestID() {
        return sharedPreferences.getInt(KEY_GUEST_ID, -1);
    }

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private BookingPagerAdapter adapter;
    BookingViewModel bookingViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        adapter = new BookingPagerAdapter(this);
        viewPager.setAdapter(adapter);
bookingViewModel.setGuestID(getGuestID());
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Dates"); break;
                        case 1: tab.setText("Rooms"); break;
                        case 2: tab.setText("Amenities"); break;
                        case 3: tab.setText("Cottages"); break;
                        case 4: tab.setText("Menu"); break;
                        case 5: tab.setText("Payment"); break;
                    }
                }).attach();
    }
}
