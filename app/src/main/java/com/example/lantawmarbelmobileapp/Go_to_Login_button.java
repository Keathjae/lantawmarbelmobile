package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class Go_to_Login_button extends AppCompatActivity {

    String API_URL = "https://lantawmarbelresort.site/guest/login";

    // UI Components
    private TextInputLayout emailPhoneLayout, passwordLayout;
    private TextInputEditText emailPhoneInput, passwordInput;
    private MaterialButton loginButton;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_PASSWORD = "KEY_USER_PASSWORD";
    private static final String KEY_USER_PHONE = "KEY_USER_PHONE";
    private static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
    private static final String KEY_LAST_NAME = "KEY_LAST_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_login_button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Check if user is already logged in
        checkExistingLogin();
    }

    private void initializeViews() {
        // Initialize TextInputLayouts (assuming they exist in your layout)
        passwordLayout = findViewById(R.id.passwordLayout);

        // Initialize TextInputEditTexts
        emailPhoneInput = findViewById(R.id.emailPhoneInput);
        passwordInput = findViewById(R.id.passwordInput);

        // Initialize login button
        loginButton = findViewById(R.id.loginButton);
    }

    private void checkExistingLogin() {
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // User is already logged in, go directly to MainActivity
            String userName = sharedPreferences.getString("currentUser", "User");
            Toast.makeText(this, "Welcome back, " + userName + "!", Toast.LENGTH_SHORT).show();

            navigateToMainActivity();
        }
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin(emailPhoneInput, passwordInput);
            }
        });
    }

    private void performLogin(TextInputEditText email, TextInputEditText password) {
        // require login
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    jsonBody,
                    response -> {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(Go_to_Login_button.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                // Go to MainActivity
                                startActivity(new Intent(Go_to_Login_button.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Go_to_Login_button.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Go_to_Login_button.this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Toast.makeText(Go_to_Login_button.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            // Add request to the queue
            queue.add(request);

        } catch (JSONException e) {
            Toast.makeText(Go_to_Login_button.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String emailPhone, String password) {
        boolean isValid = true;

        // Validate Email/Phone
        if (TextUtils.isEmpty(emailPhone)) {
            emailPhoneLayout.setError("Email or phone number is required");
            isValid = false;
        } else if (emailPhone.contains("@")) {
            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches()) {
                emailPhoneLayout.setError("Please enter a valid email address");
                isValid = false;
            }
        } else {
            // Validate phone number
            if (emailPhone.length() < 10 || !emailPhone.matches("^[+]?[0-9]{10,13}$")) {
                emailPhoneLayout.setError("Please enter a valid phone number");
                isValid = false;
            }
        }

        // Validate Password
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    private void authenticateUser(String emailPhone, String password) {
        // Get saved user credentials
        String savedEmail = sharedPreferences.getString(KEY_USER_EMAIL, "");
        String savedPhone = sharedPreferences.getString(KEY_USER_PHONE, "");
        String savedPassword = sharedPreferences.getString(KEY_USER_PASSWORD, "");

        // Check for test account
        boolean isTestAccount = emailPhone.equals("test@lantawmarbel.com") && password.equals("password123");

        // Check if credentials match saved user data
        boolean isEmailMatch = emailPhone.equals(savedEmail) && password.equals(savedPassword);
        boolean isPhoneMatch = emailPhone.equals(savedPhone) && password.equals(savedPassword);

        if (isTestAccount || isEmailMatch || isPhoneMatch) {
            // Login successful
            loginSuccess(emailPhone);
        } else {
            // Login failed
            loginFailed();
        }
    }

    private void loginSuccess(String emailPhone) {
        // Get user details
        String firstName = sharedPreferences.getString(KEY_FIRST_NAME, "");
        String lastName = sharedPreferences.getString(KEY_LAST_NAME, "");
        String fullName = (firstName + " " + lastName).trim();

        // If no saved name, create from email
        if (fullName.isEmpty()) {
            if (emailPhone.contains("@")) {
                fullName = emailPhone.substring(0, emailPhone.indexOf("@"));
                fullName = fullName.substring(0, 1).toUpperCase() + fullName.substring(1);
            } else {
                fullName = "Lantaw User";
            }
        }

        // Save login session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString("currentUser", fullName);
        editor.putString("currentUserEmail", emailPhone);
        editor.putBoolean("isGuestMode", false);
        editor.putLong("loginTimestamp", System.currentTimeMillis());
        editor.apply();

        // Show success message
        Toast.makeText(this, "Welcome to Lantaw Marbel, " + fullName + "!", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "You now have access to all premium features!", Toast.LENGTH_SHORT).show();

        // Navigate to main activity with full access
        navigateToMainActivity();
    }

    private void loginFailed() {
        // Reset button state
        resetButtonState();

        // Show error message
        passwordLayout.setError("Invalid email/phone or password");
        Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show();

        // Suggest registration if no user data exists
        String savedEmail = sharedPreferences.getString(KEY_USER_EMAIL, "");
        if (savedEmail.isEmpty()) {
            Toast.makeText(this, "New to Lantaw Marbel? Please register first.", Toast.LENGTH_LONG).show();
        }
    }

    private void showLoadingState() {
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
    }

    private void resetButtonState() {
        loginButton.setEnabled(true);
        loginButton.setText("Login");
    }

    private void clearErrors() {
        emailPhoneLayout.setError(null);
        passwordLayout.setError(null);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(Go_to_Login_button.this, MainActivity.class);
        intent.putExtra("userLoggedIn", true);
        intent.putExtra("hasFullAccess", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset UI state when returning to this activity
        resetButtonState();
        clearErrors();
    }

    @Override
    public void onBackPressed() {
        // Allow user to go back to previous screen
        super.onBackPressed();
    }

    // Method to check user authentication status (can be called from other activities)
    public static boolean isUserAuthenticated(SharedPreferences prefs) {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Method to get current user name
    public static String getCurrentUserName(SharedPreferences prefs) {
        return prefs.getString("currentUser", "Guest User");
    }

    // Method to logout user
    public static void logoutUser(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.putBoolean("isGuestMode", false);
        editor.remove("currentUser");
        editor.remove("currentUserEmail");
        editor.remove("loginTimestamp");
        editor.apply();
    }
}