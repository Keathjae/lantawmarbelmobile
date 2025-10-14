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

public class MainActivity extends AppCompatActivity {

    // Declare UI components
    private CardView discountCard;
    private CardView foodCard;
    private CardView bookCard;
    private CardView dayTourCard;
    private CardView viewQRCard;

    private TextView userProfile;
    private ImageView expandIcon;
    private ImageView profileIcon;
    private ImageView notificationBell;
    private TextView notificationBadge;

    private ImageButton homeButton;
    private ImageButton inquiryButton;
    private ImageButton BookingButton;

    // Notification system components
//    private NotificationManager notificationManager;
    private BroadcastReceiver notificationReceiver;

    // For checking login status
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_ID = "userID";




    private String getGuestFullName() {
        return sharedPreferences.getString(KEY_USER_NAME, "Guest");
    }

    private int getUserID() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
int uid=getUserID();
        // Show logged-in user's name
        if (userProfile != null && isUserLoggedIn()) {
            userProfile.setText(getGuestFullName());
        }
        notificationBell=findViewById(R.id.notificationBell);
        notificationBell.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
        expandIcon=findViewById(R.id.expandIcon);
       expandIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FeedbackListActivity.class);
            startActivity(intent);
        });

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new TabPagerAdapter(MainActivity.this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Welcome"); break;
                        case 1: tab.setText("Rooms"); break;
                        case 2: tab.setText("Amenities"); break;
                        case 3: tab.setText("Cottages"); break;
                        case 4: tab.setText("Menu"); break;
                    }
                }).attach();

        handleIntentExtras();
        BookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, BookingListActivity.class);
                startActivity(intent);
            }
        });
inquiryButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,ChatActivity.class);
        startActivity(intent);
    }
});
        addWelcomeNotificationIfNeeded();
    }


    private void initializeViews() {
        // Cards
        discountCard = findViewById(R.id.discountCard);
//        bookCard = findViewById(R.id.BookCard);
//        foodCard = findViewById(R.id.foodCard);
//        dayTourCard = findViewById(R.id.dayTourCard);
//        viewQRCard = findViewById(R.id.ViewQRCard);

        // Text Views and Images
        userProfile = findViewById(R.id.userProfile);
        expandIcon = findViewById(R.id.expandIcon);
        profileIcon = findViewById(R.id.profileIcon);

        // Notification components
        notificationBell = findViewById(R.id.notificationBell);
//        notificationBadge = findViewById(R.id.notificationBadge);

        // Bottom navigation buttons
        homeButton = findViewById(R.id.homeButton);
        BookingButton = findViewById(R.id.reservationButton);
        inquiryButton = findViewById(R.id.inquiryButton);
    }

//    private void initializeNotificationSystem() {
//        notificationManager = new NotificationManager(this);
//        setupNotificationReceiver();
//        setupNotificationBellListener();
//    }

    private void setupNotificationReceiver() {
        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.example.lantawmarbelmobileapp.NOTIFICATION_UPDATE".equals(intent.getAction())) {
//                    updateNotificationBadge();

                    // Show a toast if there's a new notification
                    if (intent.getBooleanExtra("new_notification", false)) {
                        Toast.makeText(MainActivity.this, "New notification received!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter("com.example.lantawmarbelmobileapp.NOTIFICATION_UPDATE");
//        registerReceiver(notificationReceiver, filter);
    }

//    private void setupNotificationBellListener() {
//        if (notificationBell != null) {
//            notificationBell.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Open notifications activity
//                    Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }
//    }

    private void handleIntentExtras() {
        Intent intent = getIntent();

        // Check if coming from booking confirmation
        if (intent.getBooleanExtra("booking_confirmed", false)) {
            Toast.makeText(this, "Welcome back! Your booking has been confirmed.",
                    Toast.LENGTH_LONG).show();
//            updateNotificationBadge();
        }

        // Check if need to navigate to booking section
        if (intent.getBooleanExtra("navigate_to_booking", false)) {
            navigateToBookingSection();
        }
    }

    private void navigateToBookingSection() {
        // Navigate to booking list or show booking options
        try {
            Intent intent = new Intent(MainActivity.this, Go_To_List_of_bookings.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Booking section not available", Toast.LENGTH_SHORT).show();
        }
    }

//    private void updateNotificationBadge() {
//        if (notificationManager != null && notificationBadge != null) {
//            int unreadCount = notificationManager.getUnreadNotificationCount();
//
//            if (unreadCount > 0) {
//                notificationBadge.setVisibility(View.VISIBLE);
//                notificationBadge.setText(String.valueOf(Math.min(unreadCount, 99))); // Show max 99
//            } else {
//                notificationBadge.setVisibility(View.GONE);
//            }
//        }
//    }

    private void addWelcomeNotificationIfNeeded() {
        boolean showWelcome = sharedPreferences.getBoolean("show_welcome_notification", false);

        if (showWelcome && isUserLoggedIn()) {
            String username = getGuestFullName();
            String welcomeMessage = "Welcome to Lantaw Marbel Resort, " + username + "! " +
                    "Explore our rooms, food, and day tour packages.";

            // notificationManager.addNotification(welcomeMessage, "WELCOME-001", "welcome");
            // updateNotificationBadge();

            // Clear the flag so we don't show it again
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("show_welcome_notification", false);
            editor.apply();
        }
    }


    private void setupClickListeners() {
        // Discount Card
//        discountCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkLoginAndProceed(() -> {
//                    Toast.makeText(MainActivity.this, "Discount Special clicked", Toast.LENGTH_SHORT).show();
//                    // Add your navigation here
//                });
//            }
//        });

        // Book Card
//        bookCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(MainActivity.this, Go_To_List_of_bookings.class);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Rooms activity not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Food Card
//        foodCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(MainActivity.this, Go_to_food.class);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Food activity not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Day Tour Card
//        dayTourCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(MainActivity.this, Go_to_daytour.class);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Day Tour clicked", Toast.LENGTH_SHORT).show();
//                    // Add your navigation here
//                }
//            }
//        });
//
//        // View QR Card
//        viewQRCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkLoginAndProceed(() -> {
//                    Toast.makeText(MainActivity.this, "View QR clicked", Toast.LENGTH_SHORT).show();
//                    // Add your navigation here
//                });
//            }
//        });

        // Profile-related clicks
//        expandIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to profile
//                try {
//                    Intent intent = new Intent(MainActivity.this, Go_to_expand_icon.class);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Profile activity not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Bottom Navigation
//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Already on Home", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        inquiryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(MainActivity.this, Go_To_Inquiry.class);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    // Inquiry doesn't require login
//                    Toast.makeText(MainActivity.this, "Inquiry clicked", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void checkLoginAndProceed(Runnable action) {
        if (isUserLoggedIn()) {
            action.run();
        } else {
            showLoginDialog();
        }
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    private void showLoginDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_go_to_login_signup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Initialize dialog views
        MaterialButton loginButton = dialog.findViewById(R.id.loginButton);
        MaterialButton signupButton = dialog.findViewById(R.id.signupButton);
        TextView guestModeText = dialog.findViewById(R.id.guestModeText);

        // Login button click - you can add the login logic here

        // SignUp click
        signupButton.setOnClickListener(v -> {
            dialog.dismiss();
            try {
                Intent intent = new Intent(MainActivity.this, Go_To_SignUp_Button.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Signup screen not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Guest mode click
        guestModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Continuing as guest - Limited features available", Toast.LENGTH_SHORT).show();
                // You can set a flag for guest mode if needed
            }
        });

        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Update UI when returning to this activity (in case user logged in)

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notificationReceiver != null) {
            unregisterReceiver(notificationReceiver);
        }
    }

    // Method to simulate login (call this from LoginActivity)
    public static void setUserLoggedIn(SharedPreferences prefs, boolean isLoggedIn, String username) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        if (isLoggedIn && username != null) {
            editor.putString(KEY_USER_NAME, username);
            // Set flag to show welcome notification
            editor.putBoolean("show_welcome_notification", true);
        }
        editor.apply();
    }

    // Public methods for adding notifications (can be called from other activities)
    public void addBookingConfirmation(String bookingId, String guestName) {
        String message = "Booking " + bookingId + " confirmed successfully for " + guestName;
//        notificationManager.addNotification(message, bookingId, "booking_confirmation");
//        updateNotificationBadge();

        // Send broadcast to update other activities if needed
        Intent broadcastIntent = new Intent("com.example.lantawmarbelmobileapp.NOTIFICATION_UPDATE");
        broadcastIntent.putExtra("new_notification", true);
        sendBroadcast(broadcastIntent);
    }

    public void addBookingReminder(String bookingId, String message) {
//        notificationManager.addNotification(message, bookingId, "booking_reminder");
//        updateNotificationBadge();

        Intent broadcastIntent = new Intent("com.example.lantawmarbelmobileapp.NOTIFICATION_UPDATE");
        broadcastIntent.putExtra("new_notification", true);
        sendBroadcast(broadcastIntent);
    }

    public void addBookingCancellation(String bookingId, String guestName) {
        String message = "Booking " + bookingId + " has been cancelled for " + guestName;

        Intent broadcastIntent = new Intent("com.example.lantawmarbelmobileapp.NOTIFICATION_UPDATE");
        broadcastIntent.putExtra("new_notification", true);
        sendBroadcast(broadcastIntent);
    }

}