package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Go_to_booking_confirmation extends AppCompatActivity {

    private TextView bookingId, bookingDate, guestName, guestContact, guestEmail;
    private TextView checkInDate, checkOutDate, duration, bookingDetails;
    private TextView subtotal, discount, totalAmount, paymentMethod, paymentStatus;
    private TextView bookingStatus;
    private Button btnShareReceipt, btnDownloadReceipt, btnBookAnother;
    private CardView receiptCard;

    // Booking data variables
    private String currentBookingId;
    private String currentGuestName;
    private String currentGuestContact;
    private String currentGuestEmail;
    private String currentCheckIn;
    private String currentCheckOut;
    private String currentDuration;
    private String currentBookingDetails;
    private String currentSubtotal;
    private String currentDiscount;
    private String currentTotalAmount;
    private String currentPaymentMethod;
    private boolean hasDiscount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_booking_confirmation);

        initializeViews();
        setupToolbar();
        loadBookingData();
        populateBookingDetails();
        setupButtonListeners();

        // Save booking to notifications
        saveBookingNotification();

        // Show success message
        showBookingSuccessMessage();
    }

    private void initializeViews() {
        // Initialize all TextViews
        bookingId = findViewById(R.id.bookingId);
        bookingDate = findViewById(R.id.bookingDate);
        guestName = findViewById(R.id.guestName);
        guestContact = findViewById(R.id.guestContact);
        guestEmail = findViewById(R.id.guestEmail);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        duration = findViewById(R.id.duration);
        bookingDetails = findViewById(R.id.bookingDetails);
        subtotal = findViewById(R.id.subtotal);
        discount = findViewById(R.id.discount);
        totalAmount = findViewById(R.id.totalAmount);
        paymentMethod = findViewById(R.id.paymentMethod);
        paymentStatus = findViewById(R.id.paymentStatus);
        bookingStatus = findViewById(R.id.bookingStatus);

        // Initialize buttons and card
        btnShareReceipt = findViewById(R.id.btnShareReceipt);
        btnDownloadReceipt = findViewById(R.id.btnDownloadReceipt);
        btnBookAnother = findViewById(R.id.btnBookAnother);
        receiptCard = findViewById(R.id.receiptCard); // You'll need to add this ID to your CardView
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to main activity with booking confirmed flag
                Intent intent = new Intent(Go_to_booking_confirmation.this, MainActivity.class);
                intent.putExtra("booking_confirmed", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadBookingData() {
        // Get booking data from Intent or SharedPreferences
        Intent intent = getIntent();

        // Generate booking ID with timestamp
        currentBookingId = "BK-" + System.currentTimeMillis();

        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Load data from Intent extras or use default values
        currentGuestName = intent.getStringExtra("guest_name") != null ?
                intent.getStringExtra("guest_name") : "Juan Dela Cruz";
        currentGuestContact = intent.getStringExtra("guest_contact") != null ?
                intent.getStringExtra("guest_contact") : "+63 912 345 6789";
        currentGuestEmail = intent.getStringExtra("guest_email") != null ?
                intent.getStringExtra("guest_email") : "juan@email.com";
        currentCheckIn = intent.getStringExtra("check_in") != null ?
                intent.getStringExtra("check_in") : "Dec 25, 2024";
        currentCheckOut = intent.getStringExtra("check_out") != null ?
                intent.getStringExtra("check_out") : "Dec 27, 2024";
        currentDuration = intent.getStringExtra("duration") != null ?
                intent.getStringExtra("duration") : "2 nights, 3 days";
        currentBookingDetails = intent.getStringExtra("booking_details") != null ?
                intent.getStringExtra("booking_details") :
                "• Deluxe Room - ₱2,500/night × 2 days (2 adults, 1 kids)\n" +
                        "• Small Cottage - ₱800/day × 2 days\n" +
                        "• Swimming Pool Access - ₱200/person\n" +
                        "• BBQ Grill Set - ₱300/day × 2 days";
        currentSubtotal = intent.getStringExtra("subtotal") != null ?
                intent.getStringExtra("subtotal") : "₱8,400";
        currentDiscount = intent.getStringExtra("discount") != null ?
                intent.getStringExtra("discount") : "₱1,680";
        currentTotalAmount = intent.getStringExtra("total_amount") != null ?
                intent.getStringExtra("total_amount") : "₱6,720";
        currentPaymentMethod = intent.getStringExtra("payment_method") != null ?
                intent.getStringExtra("payment_method") : "GCash";

        hasDiscount = intent.getBooleanExtra("has_discount", true);
    }

    private void populateBookingDetails() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Populate all fields
        bookingId.setText(currentBookingId);
        bookingDate.setText(currentDate);
        guestName.setText(currentGuestName);
        guestContact.setText(currentGuestContact);
        guestEmail.setText(currentGuestEmail);
        checkInDate.setText(currentCheckIn);
        checkOutDate.setText(currentCheckOut);
        duration.setText(currentDuration);
        bookingDetails.setText(currentBookingDetails);
        subtotal.setText(currentSubtotal);
        totalAmount.setText(currentTotalAmount);
        paymentMethod.setText(currentPaymentMethod);
        paymentStatus.setText("CONFIRMED");
        bookingStatus.setText("CONFIRMED - Your booking is confirmed and ready!");

        // Handle discount visibility
        View discountLayout = findViewById(R.id.discountLayout);
        if (hasDiscount && discountLayout != null) {
            discountLayout.setVisibility(View.VISIBLE);
            discount.setText("-" + currentDiscount);
        } else if (discountLayout != null) {
            discountLayout.setVisibility(View.GONE);
        }
    }

    private void setupButtonListeners() {
        btnShareReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReceipt();
            }
        });

        btnDownloadReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadReceipt();
            }
        });

        btnBookAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to booking activity or main activity
                Intent intent = new Intent(Go_to_booking_confirmation.this, MainActivity.class);
                intent.putExtra("navigate_to_booking", true);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveBookingNotification() {
        SharedPreferences prefs = getSharedPreferences("BookingNotifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get current notification count
        int notificationCount = prefs.getInt("notification_count", 0);
        notificationCount++;

        // Save booking notification
        String notificationKey = "notification_" + notificationCount;
        String notificationMessage = "Booking " + currentBookingId + " confirmed for " + currentGuestName;
        String notificationDate = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(new Date());

        editor.putString(notificationKey + "_message", notificationMessage);
        editor.putString(notificationKey + "_date", notificationDate);
        editor.putString(notificationKey + "_booking_id", currentBookingId);
        editor.putString(notificationKey + "_type", "booking_confirmation");
        editor.putBoolean(notificationKey + "_read", false);

        editor.putInt("notification_count", notificationCount);
        editor.apply();

        // Update main activity notification badge
        updateMainActivityNotification();
    }

    private void updateMainActivityNotification() {
        // Send broadcast to main activity to update notification badge
        Intent broadcastIntent = new Intent("com.example.lantawmarbelmobileapp.NOTIFICATION_UPDATE");
        broadcastIntent.putExtra("new_notification", true);
        sendBroadcast(broadcastIntent);
    }

    private void shareReceipt() {
        try {
            // Create a bitmap of the receipt
            Bitmap receiptBitmap = createReceiptBitmap();

            // Save bitmap to cache directory
            File cachePath = new File(getCacheDir(), "receipts");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, "receipt_" + currentBookingId + ".png");

            FileOutputStream stream = new FileOutputStream(imageFile);
            receiptBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            // Create sharing intent
            Uri contentUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "My booking confirmation for Lantaw Marbel Resort\nBooking ID: " + currentBookingId);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share Receipt"));

        } catch (Exception e) {
            Toast.makeText(this, "Error sharing receipt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadReceipt() {
        try {
            // Create PDF document
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Draw receipt content to PDF
            drawReceiptToPDF(canvas, paint);

            pdfDocument.finishPage(page);

            // Save PDF to downloads
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File pdfFile = new File(downloadsDir, "Receipt_" + currentBookingId + ".pdf");

            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();

            Toast.makeText(this, "Receipt downloaded to Downloads folder", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error downloading receipt: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap createReceiptBitmap() {
        // Create bitmap from receipt card view
        if (receiptCard != null) {
            Bitmap bitmap = Bitmap.createBitmap(receiptCard.getWidth(),
                    receiptCard.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            receiptCard.draw(canvas);
            return bitmap;
        }

        // Fallback: create simple text bitmap
        Bitmap bitmap = Bitmap.createBitmap(400, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(16);

        canvas.drawColor(Color.WHITE);
        canvas.drawText("LANTAW MARBEL RESORT", 20, 50, paint);
        canvas.drawText("Booking ID: " + currentBookingId, 20, 80, paint);
        canvas.drawText("Guest: " + currentGuestName, 20, 110, paint);
        canvas.drawText("Check-in: " + currentCheckIn, 20, 140, paint);
        canvas.drawText("Check-out: " + currentCheckOut, 20, 170, paint);
        canvas.drawText("Total: " + currentTotalAmount, 20, 200, paint);
        canvas.drawText("Status: CONFIRMED", 20, 230, paint);

        return bitmap;
    }

    private void drawReceiptToPDF(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        paint.setFakeBoldText(true);

        int y = 50;
        canvas.drawText("LANTAW MARBEL RESORT", 50, y, paint);

        paint.setFakeBoldText(false);
        paint.setTextSize(14);
        y += 30;
        canvas.drawText("Booking Receipt", 50, y, paint);

        y += 40;
        canvas.drawText("Booking ID: " + currentBookingId, 50, y, paint);
        y += 25;
        canvas.drawText("Guest Name: " + currentGuestName, 50, y, paint);
        y += 25;
        canvas.drawText("Contact: " + currentGuestContact, 50, y, paint);
        y += 25;
        canvas.drawText("Email: " + currentGuestEmail, 50, y, paint);
        y += 25;
        canvas.drawText("Check-in: " + currentCheckIn, 50, y, paint);
        y += 25;
        canvas.drawText("Check-out: " + currentCheckOut, 50, y, paint);
        y += 25;
        canvas.drawText("Duration: " + currentDuration, 50, y, paint);

        y += 40;
        paint.setFakeBoldText(true);
        canvas.drawText("Booking Details:", 50, y, paint);
        paint.setFakeBoldText(false);

        // Split booking details by lines
        String[] details = currentBookingDetails.split("\n");
        for (String detail : details) {
            y += 20;
            canvas.drawText(detail, 50, y, paint);
        }

        y += 40;
        canvas.drawText("Subtotal: " + currentSubtotal, 50, y, paint);
        if (hasDiscount) {
            y += 25;
            canvas.drawText("Discount: -" + currentDiscount, 50, y, paint);
        }
        y += 25;
        paint.setFakeBoldText(true);
        canvas.drawText("Total Amount: " + currentTotalAmount, 50, y, paint);
        paint.setFakeBoldText(false);
        y += 25;
        canvas.drawText("Payment Method: " + currentPaymentMethod, 50, y, paint);
        y += 25;
        canvas.drawText("Status: CONFIRMED", 50, y, paint);
    }

    private void showBookingSuccessMessage() {
        Toast.makeText(this, "Booking confirmed successfully! Booking ID: " + currentBookingId,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // Override back button to go to main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("booking_confirmed", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}