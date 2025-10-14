package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import com.google.firebase.messaging.FirebaseMessaging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Go_To_Login_Signup extends AppCompatActivity {

    private TextInputEditText emailPhoneInput, passwordInput;
    private TextInputLayout emailPhoneLayout, passwordLayout;
    private MaterialButton loginButton, signupButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_login_signup);

        emailPhoneLayout = findViewById(R.id.emailPhoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailPhoneInput = findViewById(R.id.emailPhoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        apiService = ApiClient.getClient().create(ApiService.class);

        loginButton.setOnClickListener(v -> attemptLogin());
        signupButton.setOnClickListener(v -> startActivity(new Intent(this, Go_To_SignUp_Button.class)));

        // Auto-login if token exists
        if (sharedPreferences.contains(KEY_TOKEN)) {
            navigateToMain();
        }
    }

    private void attemptLogin() {
        clearErrors();
        String emailPhone = emailPhoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (!validateInputs(emailPhone, password)) return;

        // ✅ Reset SharedPreferences before new login
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        Map<String, String> body = new HashMap<>();
        body.put("username", emailPhone);
        body.put("password", password);

        apiService.login(body).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginButton.setEnabled(true);
                loginButton.setText("Login");

                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    LoginResponse loginResponse = response.body();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_TOKEN, loginResponse.token);
                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.putString("role", loginResponse.user.role);

                    String welcomeName = loginResponse.user.username; // fallback

                    if ("guest".equalsIgnoreCase(loginResponse.user.role) && loginResponse.profile != null) {
                        editor.putInt("guestID", loginResponse.profile.guestID);
                        editor.putString("guestEmail", loginResponse.profile.email);
                        String guestFullName = loginResponse.profile.firstname + " " + loginResponse.profile.lastname;
                        editor.putString(KEY_USER_NAME, guestFullName);
                        welcomeName = loginResponse.profile.firstname;

                    } else if ("staff".equalsIgnoreCase(loginResponse.user.role) && loginResponse.profile != null) {
                        editor.putInt("staffID", loginResponse.profile.staffID);
                        editor.putString("staffEmail", loginResponse.profile.email);
                        editor.putString("staffLevel", loginResponse.profile.role);
                        String staffFullName = loginResponse.profile.firstname + " " + loginResponse.profile.lastname;
                        editor.putString(KEY_USER_NAME, staffFullName);
                        welcomeName = loginResponse.profile.firstname;
                    }
                    editor.putInt("userID", loginResponse.user.id);
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String fcmToken = task.getResult();
                                    Log.d("FCM", "Got FCM token after login: " + fcmToken);

                                    int userId = (loginResponse.user.id);

                                    String role = loginResponse.user.role;
                                    String authToken = loginResponse.token;

                                    MyFirebaseMessagingService.sendTokenToServer(fcmToken, userId, role, authToken);
                                } else {
                                    Log.w("FCM", "Fetching FCM token failed", task.getException());
                                }
                            });
                    editor.apply();

                    Toast.makeText(Go_To_Login_Signup.this, "Welcome, " + welcomeName + "!", Toast.LENGTH_LONG).show();

                    if ("guest".equalsIgnoreCase(loginResponse.user.role)) {
                        startActivity(new Intent(Go_To_Login_Signup.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(Go_To_Login_Signup.this, QrScanActivity.class));
                    }
                    finish();


                } else {
                    Toast.makeText(Go_To_Login_Signup.this, "Login failed. Check your credentials.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginButton.setEnabled(true);
                loginButton.setText("Login");
                Toast.makeText(Go_To_Login_Signup.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean validateInputs(String emailPhone, String password) {
        boolean isValid = true;
        if (emailPhone.isEmpty()) {
            emailPhoneLayout.setError("Email or phone is required");
            isValid = false;
        } else if (emailPhone.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(emailPhone).matches()) {
            emailPhoneLayout.setError("Invalid email format");
            isValid = false;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            isValid = false;
        }
        return isValid;
    }

    private void clearErrors() {
        emailPhoneLayout.setError(null);
        passwordLayout.setError(null);
    }

    private void navigateToMain() {
        Intent intent = new Intent(Go_To_Login_Signup.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
