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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Go_To_SignUp_Button extends AppCompatActivity {

    // UI Components
    private TextInputLayout firstNameLayout, lastNameLayout, addressLayout,
            emailLayout, phoneLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText firstNameInput, lastNameInput, addressInput,
            emailInput, phoneInput, passwordInput, confirmPasswordInput;
    private MaterialButton signUpButton;

    // SharedPreferences - Same constants as login activity
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_PASSWORD = "KEY_USER_PASSWORD";
    private static final String KEY_USER_PHONE = "KEY_USER_PHONE";
    private static final String KEY_FIRST_NAME = "KEY_FIRST_NAME";
    private static final String KEY_LAST_NAME = "KEY_LAST_NAME";
    private static final String KEY_USER_ADDRESS = "KEY_USER_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_signup_button);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Initialize views
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    private void initializeViews() {
        // Initialize TextInputLayouts
        firstNameLayout = findViewById(R.id.firstNameLayout);
        lastNameLayout = findViewById(R.id.lastNameLayout);
        addressLayout = findViewById(R.id.addressLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        // Initialize TextInputEditTexts
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        addressInput = findViewById(R.id.addressInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        // Initialize Button
        signUpButton = findViewById(R.id.signUpButton);
    }

    private void setClickListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });

        // Handle login link click (if you have it in your layout)
        findViewById(R.id.loginLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void performSignUp() {
        // Clear previous errors
        clearErrors();

        // Get input values
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        // Validate inputs
        if (validateInputs(firstName, lastName, address, email, phone, password, confirmPassword)) {
            // Check if user already exists
            if (checkUserExists(email, phone)) {
                Toast.makeText(this, "User with this email or phone already exists!", Toast.LENGTH_LONG).show();
                return;
            }

            // If validation passes, create account
            createAccount(firstName, lastName, address, email, phone, password);
        }
    }

    private boolean checkUserExists(String email, String phone) {
        String savedEmail = sharedPreferences.getString(KEY_USER_EMAIL, "");
        String savedPhone = sharedPreferences.getString(KEY_USER_PHONE, "");

        return email.equals(savedEmail) || phone.equals(savedPhone);
    }

    private boolean validateInputs(String firstName, String lastName, String address,
                                   String email, String phone, String password, String confirmPassword) {
        boolean isValid = true;

        // Validate First Name
        if (TextUtils.isEmpty(firstName)) {
            firstNameLayout.setError("First name is required");
            isValid = false;
        } else if (firstName.length() < 2) {
            firstNameLayout.setError("First name must be at least 2 characters");
            isValid = false;
        }

        // Validate Last Name
        if (TextUtils.isEmpty(lastName)) {
            lastNameLayout.setError("Last name is required");
            isValid = false;
        } else if (lastName.length() < 2) {
            lastNameLayout.setError("Last name must be at least 2 characters");
            isValid = false;
        }

        // Validate Address
        if (TextUtils.isEmpty(address)) {
            addressLayout.setError("Address is required");
            isValid = false;
        } else if (address.length() < 10) {
            addressLayout.setError("Please enter a complete address");
            isValid = false;
        }

        // Validate Email
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email address");
            isValid = false;
        }

        // Validate Phone Number
        if (TextUtils.isEmpty(phone)) {
            phoneLayout.setError("Phone number is required");
            isValid = false;
        } else if (phone.length() < 10) {
            phoneLayout.setError("Please enter a valid phone number");
            isValid = false;
        } else if (!phone.matches("^[+]?[0-9]{10,13}$")) {
            phoneLayout.setError("Phone number can only contain numbers and optional + prefix");
            isValid = false;
        }

        // Validate Password
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 8) {
            passwordLayout.setError("Password must be at least 8 characters");
            isValid = false;
        } else if (!isPasswordStrong(password)) {
            passwordLayout.setError("Password must contain at least one uppercase, lowercase, number, and special character");
            isValid = false;
        }

        // Validate Confirm Password
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordLayout.setError("Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    private boolean isPasswordStrong(String password) {
        // Check for at least one uppercase letter
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        // Check for at least one lowercase letter
        boolean hasLowerCase = password.matches(".*[a-z].*");
        // Check for at least one digit
        boolean hasDigit = password.matches(".*\\d.*");
        // Check for at least one special character
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }

    private void clearErrors() {
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        addressLayout.setError(null);
        emailLayout.setError(null);
        phoneLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
    }

    private void createAccount(String firstName, String lastName, String address,
                               String email, String phone, String password) {
        // Disable button to prevent multiple clicks
        signUpButton.setEnabled(false);
        signUpButton.setText("Creating Account...");

        // Simulate network delay and then save user data
        signUpButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Save user data to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Save all user information
                editor.putString(KEY_FIRST_NAME, firstName);
                editor.putString(KEY_LAST_NAME, lastName);
                editor.putString(KEY_USER_ADDRESS, address);
                editor.putString(KEY_USER_EMAIL, email);
                editor.putString(KEY_USER_PHONE, phone);
                editor.putString(KEY_USER_PASSWORD, password);

                // Set registration status
                editor.putBoolean("isRegistered", true);

                // Apply changes
                editor.apply();

                // Reset button state
                signUpButton.setEnabled(true);
                signUpButton.setText("Sign Up");

                // Show success message
                Toast.makeText(Go_To_SignUp_Button.this,
                        "Account created successfully! You can now login with your email/phone and password.",
                        Toast.LENGTH_LONG).show();

                // Navigate back to login activity
                navigateToLogin();
            }
        }, 2000); // 2 second delay to simulate network request
    }

    private void navigateToLogin() {
        Intent intent = new Intent(Go_To_SignUp_Button.this, Go_To_Login_Signup.class);
        // Add flags to clear the activity stack and start fresh
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Handle back button press
    @Override
    public void onBackPressed() {
        navigateToLogin();
        super.onBackPressed();
    }
}