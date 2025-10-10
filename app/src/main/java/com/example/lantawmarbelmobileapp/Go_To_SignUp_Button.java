package com.example.lantawmarbelmobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Go_To_SignUp_Button extends AppCompatActivity {

    private static final int REQUEST_AVATAR_CAPTURE = 100;
    private static final int REQUEST_ID_CAPTURE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private TextInputEditText usernameInput, firstNameInput, lastNameInput, emailInput, phoneInput, passwordInput, confirmPasswordInput, birthdayInput;
    private TextInputLayout usernameLayout, firstNameLayout, lastNameLayout, emailLayout, phoneLayout, passwordLayout, confirmPasswordLayout, birthdayLayout;
    private Spinner genderSpinner;
    private MaterialButton selectAvatarButton, selectIdButton, signUpButton;

    private Bitmap avatarBitmap, idBitmap;
    private String idOcrText;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_signup_button);

        // Initialize layouts
        usernameLayout = findViewById(R.id.usernameLayout);
        firstNameLayout = findViewById(R.id.firstNameLayout);
        lastNameLayout = findViewById(R.id.lastNameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        birthdayLayout = findViewById(R.id.birthdayLayout);

        // Inputs
        usernameInput = findViewById(R.id.usernameInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        birthdayInput = findViewById(R.id.birthdayInput);

        // Gender spinner
        genderSpinner = findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Buttons
        selectAvatarButton = findViewById(R.id.selectAvatarButton);
        selectIdButton = findViewById(R.id.selectIdButton);
        signUpButton = findViewById(R.id.signUpButton);

        apiService = ApiClient.getClient().create(ApiService.class);

        selectAvatarButton.setOnClickListener(v -> checkCameraPermission(REQUEST_AVATAR_CAPTURE));
        selectIdButton.setOnClickListener(v -> checkCameraPermission(REQUEST_ID_CAPTURE));
        signUpButton.setOnClickListener(v -> attemptSignup());

        findViewById(R.id.loginLink).setOnClickListener(v -> navigateToLogin());
    }

    private void checkCameraPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION + requestCode); // offset requestCode
        } else {
            captureImage(requestCode);
        }
    }

    private void captureImage(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CAMERA_PERMISSION + REQUEST_AVATAR_CAPTURE) {
                captureImage(REQUEST_AVATAR_CAPTURE);
            } else if (requestCode == REQUEST_CAMERA_PERMISSION + REQUEST_ID_CAPTURE) {
                captureImage(REQUEST_ID_CAPTURE);
            }
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if (requestCode == REQUEST_AVATAR_CAPTURE) {
                avatarBitmap = bitmap;
                Toast.makeText(this, "Avatar captured", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_ID_CAPTURE) {
                idBitmap = bitmap;
                Toast.makeText(this, "ID captured", Toast.LENGTH_SHORT).show();

                // Run OCR
                InputImage image = InputImage.fromBitmap(idBitmap, 0);
                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

                recognizer.process(image)
                        .addOnSuccessListener(visionText -> {
                            idOcrText = visionText.getText();
                            Toast.makeText(this, "ID OCR: " + idOcrText, Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "OCR failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void attemptSignup() {
        clearErrors();

        String username = usernameInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        String birthday = birthdayInput.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();

        if (!validateInputs(username, firstName, lastName, email, phone, password, confirmPassword, gender, birthday))
            return;

        signUpButton.setEnabled(false);
        signUpButton.setText("Signing up...");

        // Convert Bitmaps to Multipart
        MultipartBody.Part avatarPart = bitmapToPart(avatarBitmap, "avatar", "avatar.jpg");
        MultipartBody.Part validIDPart = bitmapToPart(idBitmap, "validID", "id.jpg");

        apiService.signupMultipart(
                RequestBody.create(MediaType.parse("text/plain"), username),
                RequestBody.create(MediaType.parse("text/plain"), password),
                RequestBody.create(MediaType.parse("text/plain"), confirmPassword),
                RequestBody.create(MediaType.parse("text/plain"), firstName),
                RequestBody.create(MediaType.parse("text/plain"), lastName),
                RequestBody.create(MediaType.parse("text/plain"), email),
                RequestBody.create(MediaType.parse("text/plain"), phone),
                RequestBody.create(MediaType.parse("text/plain"), gender),
                RequestBody.create(MediaType.parse("text/plain"), birthday),
                avatarPart,
                validIDPart
        ).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                signUpButton.setEnabled(true);
                signUpButton.setText("Sign Up");
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    Toast.makeText(Go_To_SignUp_Button.this, "Account created! Please login.", Toast.LENGTH_LONG).show();
                    navigateToLogin();
                } else {
                    // Handle malformed JSON or unexpected response
                    try {
                        String rawResponse = response.errorBody() != null ? response.errorBody().string() : "empty";
                        Toast.makeText(Go_To_SignUp_Button.this, "Malformed JSON / Error: " + rawResponse, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(Go_To_SignUp_Button.this, "Unknown error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                signUpButton.setEnabled(true);
                signUpButton.setText("Sign Up");

                // Log full error including raw response if possible
                Toast.makeText(Go_To_SignUp_Button.this, "Request failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private MultipartBody.Part bitmapToPart(Bitmap bitmap, String fieldName, String filename) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), baos.toByteArray());
        return MultipartBody.Part.createFormData(fieldName, filename, body);
    }

    private boolean validateInputs(String username, String firstName, String lastName, String email,
                                   String phone, String password, String confirmPassword,
                                   String gender, String birthday) {
        boolean isValid = true;

        if (TextUtils.isEmpty(username)) { usernameLayout.setError("Username required"); isValid = false; }
        if (TextUtils.isEmpty(firstName)) { firstNameLayout.setError("First name required"); isValid = false; }
        if (TextUtils.isEmpty(lastName)) { lastNameLayout.setError("Last name required"); isValid = false; }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailLayout.setError("Valid email required"); isValid = false; }
        if (TextUtils.isEmpty(phone)) { phoneLayout.setError("Phone required"); isValid = false; }
        if (TextUtils.isEmpty(password) || password.length() < 8) { passwordLayout.setError("Password min 8 chars"); isValid = false; }
        if (!password.equals(confirmPassword)) { confirmPasswordLayout.setError("Passwords do not match"); isValid = false; }

        return isValid;
    }

    private void clearErrors() {
        usernameLayout.setError(null);
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
        phoneLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
        birthdayLayout.setError(null);
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, Go_To_Login_Signup.class));
        finish();
    }
}
