package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Go_To_Login_Signup extends AppCompatActivity {

    // UI Components
    private TextInputEditText emailPhoneInput;
    private TextInputEditText passwordInput;
    private TextInputLayout emailPhoneLayout;
    private TextInputLayout passwordLayout;
    private MaterialButton loginButton;
    private MaterialButton signupButton;
    private TextView guestModeText;

    // SharedPreferences
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_PASSWORD = "KEY_USER_PASSWORD";
    private static final String KEY_USER_PHONE = "KEY_USER_PHONE";
    private static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
    private static final String KEY_LAST_NAME = "KEY_LAST_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_login_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
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

    private void checkExistingLogin() {
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            // User is already logged in, go to MainActivity
            String userName = sharedPreferences.getString("currentUser", "User");
            Toast.makeText(this, "Welcome back, " + userName + "!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initializeViews() {
        // Find the TextInputLayouts - now with proper IDs
        emailPhoneLayout = findViewById(R.id.emailPhoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);

        // Find the EditTexts
        emailPhoneInput = findViewById(R.id.emailPhoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        guestModeText = findViewById(R.id.guestModeText);
    }

    private void setupClickListeners() {
        // Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Signup button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to signup/register activity
                Intent intent = new Intent(Go_To_Login_Signup.this, Go_To_SignUp_Button.class);
                startActivity(intent);
            }
        });

        // Guest mode click
        guestModeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Continue as guest
                Toast.makeText(Go_To_Login_Signup.this,
                        "Continuing as guest - Limited features available", Toast.LENGTH_SHORT).show();

                // Save guest mode status
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isGuestMode", true);
                editor.putString("currentUser", "Guest User");
                editor.apply();

                // Go to MainActivity
                Intent intent = new Intent(Go_To_Login_Signup.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void attemptLogin() {
        // Get input values
        String emailPhone = emailPhoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(emailPhone, password)) {
            return;
        }

        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Simulate login check (in real app, this would be an API call)
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkUserCredentials(emailPhone, password)) {
                    // Login successful
                    loginSuccess(emailPhone);
                } else {
                    // Login failed
                    loginFailed();
                }
            }
        }, 1500); // Simulate network delay
    }

    private boolean validateInputs(String emailPhone, String password) {
        // Clear previous errors
        clearErrors();

        boolean isValid = true;

        // Check if email/phone is empty
        if (emailPhone.isEmpty()) {
            setError(emailPhoneLayout, emailPhoneInput, "Email or phone number is required");
            emailPhoneInput.requestFocus();
            isValid = false;
        } else if (emailPhone.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches()) {
            // Check if it's email and validate format
            setError(emailPhoneLayout, emailPhoneInput, "Please enter a valid email address");
            emailPhoneInput.requestFocus();
            isValid = false;
        } else if (!emailPhone.contains("@") && emailPhone.length() < 10) {
            // Check if it's phone number and validate (simple check)
            setError(emailPhoneLayout, emailPhoneInput, "Please enter a valid phone number");
            emailPhoneInput.requestFocus();
            isValid = false;
        }

        // Check password - Match signup validation
        if (password.isEmpty()) {
            setError(passwordLayout, passwordInput, "Password is required");
            passwordInput.requestFocus();
            isValid = false;
        } else if (password.length() < 6) {
            setError(passwordLayout, passwordInput, "Password must be at least 6 characters");
            passwordInput.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void setError(TextInputLayout layout, TextInputEditText input, String errorMessage) {
        if (layout != null) {
            layout.setError(errorMessage);
        } else if (input != null) {
            input.setError(errorMessage);
        }
    }

    private void clearErrors() {
        if (emailPhoneLayout != null) {
            emailPhoneLayout.setError(null);
        } else if (emailPhoneInput != null) {
            emailPhoneInput.setError(null);
        }

        if (passwordLayout != null) {
            passwordLayout.setError(null);
        } else if (passwordInput != null) {
            passwordInput.setError(null);
        }
    }

    private boolean checkUserCredentials(String emailPhone, String password) {
        // Get saved user data
        String savedEmail = sharedPreferences.getString(KEY_USER_EMAIL, "");
        String savedPhone = sharedPreferences.getString(KEY_USER_PHONE, "");
        String savedPassword = sharedPreferences.getString(KEY_USER_PASSWORD, "");

        // Debug logging (remove in production)
        android.util.Log.d("LoginDebug", "Input: " + emailPhone + " | Saved Email: " + savedEmail + " | Saved Phone: " + savedPhone);
        android.util.Log.d("LoginDebug", "Password match: " + password.equals(savedPassword));

        // Check for demo/test account
        boolean isTestAccount = (emailPhone.equals("test@example.com") || emailPhone.equals("1234567890"))
                && password.equals("password123");

        // Check if registered user exists
        boolean isRegistered = sharedPreferences.getBoolean("isRegistered", false);

        // Check if login matches saved email or phone and password
        boolean isRegisteredUser = isRegistered &&
                (emailPhone.equals(savedEmail) || emailPhone.equals(savedPhone)) &&
                password.equals(savedPassword);

        return isTestAccount || isRegisteredUser;
    }

    private void loginSuccess(String emailPhone) {
        // Get user information
        String firstName = sharedPreferences.getString(KEY_FIRST_NAME, "");
        String lastName = sharedPreferences.getString(KEY_LAST_NAME, "");
        String fullName = (firstName + " " + lastName).trim();

        // If no name found, use default based on login method
        if (fullName.isEmpty()) {
            if (emailPhone.contains("@")) {
                fullName = emailPhone.substring(0, emailPhone.indexOf("@"));
            } else {
                fullName = "User";
            }
        }

        // Save login status
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putBoolean("isGuestMode", false); // Clear guest mode
        editor.putString("currentUser", fullName);
        editor.putString("currentUserEmail", emailPhone);
        editor.apply();

        // Show success message
        Toast.makeText(this, "Welcome back, " + fullName + "!", Toast.LENGTH_LONG).show();

        // Navigate to MainActivity
        Intent intent = new Intent(Go_To_Login_Signup.this, MainActivity.class);
        intent.putExtra("userLoggedIn", true);
        intent.putExtra("hasFullAccess", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loginFailed() {
        // Reset button state
        loginButton.setEnabled(true);
        loginButton.setText("Login");

        // Show error message
        setError(passwordLayout, passwordInput, "Invalid email/phone or password");

        Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_LONG).show();

        // Check if user might need to register
        String emailPhone = emailPhoneInput.getText().toString().trim();
        boolean isRegistered = sharedPreferences.getBoolean("isRegistered", false);

        if (!isRegistered && !emailPhone.equals("test@example.com")) {
            Toast.makeText(this, "Don't have an account? Click Sign Up to register.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear any previous input errors when returning from signup
        clearErrors();

        // Reset button state
        if (loginButton != null) {
            loginButton.setEnabled(true);
            loginButton.setText("Login");
        }
    }

    @Override
    public void onBackPressed() {
        // Return to MainActivity or close app
        super.onBackPressed();
    }
}