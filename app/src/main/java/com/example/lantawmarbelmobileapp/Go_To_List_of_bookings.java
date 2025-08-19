package com.example.lantawmarbelmobileapp;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Go_To_List_of_bookings extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private CardView roomsCard, cottageCard, amenityCard, breakfastCard, dateCard, discountCard, paymentCard, summaryCard;
    private LinearLayout roomsExpandableContent, cottageExpandableContent, amenityExpandableContent,
            breakfastExpandableContent, dateExpandableContent, discountExpandableContent,
            paymentExpandableContent;
    private LinearLayout deluxeGuestLayout, standardGuestLayout, singleGuestLayout, familyGuestLayout;
    private LinearLayout discountLayout;

    private ImageView roomsDropdownIcon, cottageDropdownIcon, amenityDropdownIcon,
            breakfastDropdownIcon, dateDropdownIcon, discountDropdownIcon, paymentDropdownIcon;

    // Room checkboxes and guest inputs
    private CheckBox checkboxDeluxeRoom, checkboxStandardRoom, checkboxSuiteRoom, checkboxFamilyRoom;
    private EditText deluxeAdultsCount, deluxeKidsCount, standardAdultsCount, standardKidsCount,
            singleAdultsCount, singleKidsCount, familyAdultsCount, familyKidsCount;

    // Cottage checkboxes
    private CheckBox checkboxSmallCottage, checkboxMediumCottage, checkboxLargeCottage, checkboxPremiumCottage;

    // Amenity checkboxes
    private CheckBox checkboxPool, checkboxKaraoke, checkboxBBQ, checkboxBoat;

    // Breakfast components
    private Spinner spinnerBreakfast;
    private CheckBox checkboxExtraRice, checkboxExtraDrink, checkboxDessert;

    // Date selection
    private Button btnCheckInDate, btnCheckOutDate, btnBook;

    // Discount components
    private CheckBox checkboxSeniorDiscount, checkboxPWDDiscount;

    // Payment components
    private RadioGroup radioGroupPayment;
    private RadioButton radioCash, radioGCash;
    private EditText editTextPaymentAmount;

    // Summary components
    private TextView summaryText, totalAmount, subtotalAmount, discountAmount;

    // Room detail buttons
    private TextView deluxeRoomDetails, standardRoomDetails, singleRoomDetails, familyRoomDetails;

    // Data
    private Calendar checkInDate, checkOutDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private List<CheckBox> allCheckboxes = new ArrayList<>();

    // Pricing constants
    private static final int DELUXE_ROOM_PRICE = 2500;
    private static final int STANDARD_ROOM_PRICE = 1800;
    private static final int SINGLE_ROOM_PRICE = 3500;
    private static final int FAMILY_ROOM_PRICE = 4000;
    private static final int SMALL_COTTAGE_PRICE = 800;
    private static final int MEDIUM_COTTAGE_PRICE = 1200;
    private static final int LARGE_COTTAGE_PRICE = 1800;
    private static final int PREMIUM_COTTAGE_PRICE = 2200;
    private static final int POOL_PRICE = 200;
    private static final int KARAOKE_PRICE = 500;
    private static final int BBQ_PRICE = 300;
    private static final int BOAT_PRICE = 800;
    private static final int EXTRA_RICE_PRICE = 20;
    private static final int EXTRA_DRINK_PRICE = 50;
    private static final int DESSERT_PRICE = 80;
    private static final double DISCOUNT_RATE = 0.20; // 20% discount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_list_of_bookings);

        initializeViews();
        setupToolbar();
        setupSpinner();
        setupClickListeners();
        setupCheckboxListeners();
        setupWindowInsets();
    }

    private void initializeViews() {
        try {
            // Toolbar
            toolbar = findViewById(R.id.toolbar);

            // Cards
            roomsCard = findViewById(R.id.roomsCard);
            cottageCard = findViewById(R.id.cottageCard);
            amenityCard = findViewById(R.id.amenityCard);
            breakfastCard = findViewById(R.id.breakfastCard);
            dateCard = findViewById(R.id.dateCard);
            discountCard = findViewById(R.id.discountCard);
            paymentCard = findViewById(R.id.paymentCard);
            summaryCard = findViewById(R.id.summaryCard);

            // Expandable content
            roomsExpandableContent = findViewById(R.id.roomsExpandableContent);
            cottageExpandableContent = findViewById(R.id.cottageExpandableContent);
            amenityExpandableContent = findViewById(R.id.amenityExpandableContent);
            breakfastExpandableContent = findViewById(R.id.breakfastExpandableContent);
            dateExpandableContent = findViewById(R.id.dateExpandableContent);
            discountExpandableContent = findViewById(R.id.discountExpandableContent);
            paymentExpandableContent = findViewById(R.id.paymentExpandableContent);

            // Guest layouts
            deluxeGuestLayout = findViewById(R.id.deluxeGuestLayout);
            standardGuestLayout = findViewById(R.id.standardGuestLayout);
            singleGuestLayout = findViewById(R.id.singleGuestLayout);
            familyGuestLayout = findViewById(R.id.familyGuestLayout);

            // Dropdown icons
            roomsDropdownIcon = findViewById(R.id.roomsDropdownIcon);
            cottageDropdownIcon = findViewById(R.id.cottageDropdownIcon);
            amenityDropdownIcon = findViewById(R.id.amenityDropdownIcon);
            breakfastDropdownIcon = findViewById(R.id.breakfastDropdownIcon);
            dateDropdownIcon = findViewById(R.id.dateDropdownIcon);
            discountDropdownIcon = findViewById(R.id.discountDropdownIcon);
            paymentDropdownIcon = findViewById(R.id.paymentDropdownIcon);

            // Room checkboxes
            checkboxDeluxeRoom = findViewById(R.id.checkboxDeluxeRoom);
            checkboxStandardRoom = findViewById(R.id.checkboxStandardRoom);
            checkboxSuiteRoom = findViewById(R.id.checkboxSuiteRoom);
            checkboxFamilyRoom = findViewById(R.id.checkboxFamilyRoom);

            // Guest count inputs
            deluxeAdultsCount = findViewById(R.id.deluxeAdultsCount);
            deluxeKidsCount = findViewById(R.id.deluxeKidsCount);
            standardAdultsCount = findViewById(R.id.standardAdultsCount);
            standardKidsCount = findViewById(R.id.standardKidsCount);
            singleAdultsCount = findViewById(R.id.singleAdultsCount);
            singleKidsCount = findViewById(R.id.singleKidsCount);
            familyAdultsCount = findViewById(R.id.familyAdultsCount);
            familyKidsCount = findViewById(R.id.familyKidsCount);

            // Room detail buttons
            deluxeRoomDetails = findViewById(R.id.deluxeRoomDetails);
            standardRoomDetails = findViewById(R.id.standardRoomDetails);
            singleRoomDetails = findViewById(R.id.singleRoomDetails);
            familyRoomDetails = findViewById(R.id.familyRoomDetails);

            // Cottage checkboxes
            checkboxSmallCottage = findViewById(R.id.checkboxSmallCottage);
            checkboxMediumCottage = findViewById(R.id.checkboxMediumCottage);
            checkboxLargeCottage = findViewById(R.id.checkboxLargeCottage);
            checkboxPremiumCottage = findViewById(R.id.checkboxPremiumCottage);

            // Amenity checkboxes
            checkboxPool = findViewById(R.id.checkboxPool);
            checkboxKaraoke = findViewById(R.id.checkboxKaraoke);
            checkboxBBQ = findViewById(R.id.checkboxBBQ);
            checkboxBoat = findViewById(R.id.checkboxBoat);

            // Breakfast components
            spinnerBreakfast = findViewById(R.id.spinnerBreakfast);
            checkboxExtraRice = findViewById(R.id.checkboxExtraRice);
            checkboxExtraDrink = findViewById(R.id.checkboxExtraDrink);
            checkboxDessert = findViewById(R.id.checkboxDessert);

            // Date buttons
            btnCheckInDate = findViewById(R.id.btnCheckInDate);
            btnCheckOutDate = findViewById(R.id.btnCheckOutDate);
            btnBook = findViewById(R.id.btnBook);

            // Discount checkboxes
            checkboxSeniorDiscount = findViewById(R.id.checkboxSeniorDiscount);
            checkboxPWDDiscount = findViewById(R.id.checkboxPWDDiscount);

            // Payment components
            radioGroupPayment = findViewById(R.id.radioGroupPayment);
            radioCash = findViewById(R.id.radioCash);
            radioGCash = findViewById(R.id.radioGCash);
            editTextPaymentAmount = findViewById(R.id.editTextPaymentAmount);

            // Summary components
            summaryText = findViewById(R.id.summaryText);
            totalAmount = findViewById(R.id.totalAmount);
            subtotalAmount = findViewById(R.id.subtotalAmount);
            discountAmount = findViewById(R.id.discountAmount);
            discountLayout = findViewById(R.id.discountLayout);

            // Add all checkboxes to list for easy management
            allCheckboxes.add(checkboxDeluxeRoom);
            allCheckboxes.add(checkboxStandardRoom);
            allCheckboxes.add(checkboxSuiteRoom);
            allCheckboxes.add(checkboxFamilyRoom);
            allCheckboxes.add(checkboxSmallCottage);
            allCheckboxes.add(checkboxMediumCottage);
            allCheckboxes.add(checkboxLargeCottage);
            allCheckboxes.add(checkboxPremiumCottage);
            allCheckboxes.add(checkboxPool);
            allCheckboxes.add(checkboxKaraoke);
            allCheckboxes.add(checkboxBBQ);
            allCheckboxes.add(checkboxBoat);
            allCheckboxes.add(checkboxExtraRice);
            allCheckboxes.add(checkboxExtraDrink);
            allCheckboxes.add(checkboxDessert);
            allCheckboxes.add(checkboxSeniorDiscount);
            allCheckboxes.add(checkboxPWDDiscount);

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        try {
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }

                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSpinner() {
        try {
            if (spinnerBreakfast != null) {
                String[] breakfastOptions = {
                        "Select Breakfast Option",
                        "Filipino Breakfast - ₱150",
                        "American Breakfast - ₱200",
                        "Continental Breakfast - ₱180",
                        "Vegetarian Breakfast - ₱160"
                };

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, breakfastOptions);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBreakfast.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupWindowInsets() {
        try {
            View rootView = findViewById(android.R.id.content);
            if (rootView != null) {
                ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        try {
            // Expandable card listeners
            if (roomsCard != null) {
                roomsCard.setOnClickListener(v -> toggleExpandableContent(roomsExpandableContent, roomsDropdownIcon));
            }
            if (cottageCard != null) {
                cottageCard.setOnClickListener(v -> toggleExpandableContent(cottageExpandableContent, cottageDropdownIcon));
            }
            if (amenityCard != null) {
                amenityCard.setOnClickListener(v -> toggleExpandableContent(amenityExpandableContent, amenityDropdownIcon));
            }
            if (breakfastCard != null) {
                breakfastCard.setOnClickListener(v -> toggleExpandableContent(breakfastExpandableContent, breakfastDropdownIcon));
            }
            if (dateCard != null) {
                dateCard.setOnClickListener(v -> toggleExpandableContent(dateExpandableContent, dateDropdownIcon));
            }
            if (discountCard != null) {
                discountCard.setOnClickListener(v -> toggleExpandableContent(discountExpandableContent, discountDropdownIcon));
            }
            if (paymentCard != null) {
                paymentCard.setOnClickListener(v -> toggleExpandableContent(paymentExpandableContent, paymentDropdownIcon));
            }

            // Date picker listeners
            if (btnCheckInDate != null) {
                btnCheckInDate.setOnClickListener(v -> showDatePicker(true));
            }
            if (btnCheckOutDate != null) {
                btnCheckOutDate.setOnClickListener(v -> showDatePicker(false));
            }

            // Book button listener
            if (btnBook != null) {
                btnBook.setOnClickListener(v -> processBooking());
            }

            // Room detail listeners
            if (deluxeRoomDetails != null) {
                deluxeRoomDetails.setOnClickListener(v -> showRoomDetails("Deluxe Room",
                        "Spacious room with modern amenities, king-size bed, and city view."));
            }
            if (standardRoomDetails != null) {
                standardRoomDetails.setOnClickListener(v -> showRoomDetails("Standard Room",
                        "Comfortable room with essential amenities and queen-size bed."));
            }
            if (singleRoomDetails != null) {
                singleRoomDetails.setOnClickListener(v -> showRoomDetails("Single Room",
                        "Perfect for solo travelers with single bed and work desk."));
            }
            if (familyRoomDetails != null) {
                familyRoomDetails.setOnClickListener(v -> showRoomDetails("Family Room",
                        "Large room suitable for families with multiple beds and extra space."));
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupCheckboxListeners() {
        try {
            for (CheckBox checkbox : allCheckboxes) {
                if (checkbox != null) {
                    checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateSummary());
                }
            }

            // Room checkbox listeners for guest layout visibility
            if (checkboxDeluxeRoom != null) {
                checkboxDeluxeRoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (deluxeGuestLayout != null) {
                        deluxeGuestLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                    updateSummary();
                });
            }

            if (checkboxStandardRoom != null) {
                checkboxStandardRoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (standardGuestLayout != null) {
                        standardGuestLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                    updateSummary();
                });
            }

            if (checkboxSuiteRoom != null) {
                checkboxSuiteRoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (singleGuestLayout != null) {
                        singleGuestLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                    updateSummary();
                });
            }

            if (checkboxFamilyRoom != null) {
                checkboxFamilyRoom.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (familyGuestLayout != null) {
                        familyGuestLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    }
                    updateSummary();
                });
            }

            // Discount checkboxes - only allow one at a time
            if (checkboxSeniorDiscount != null) {
                checkboxSeniorDiscount.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked && checkboxPWDDiscount != null) {
                        checkboxPWDDiscount.setChecked(false);
                    }
                    updateSummary();
                });
            }

            if (checkboxPWDDiscount != null) {
                checkboxPWDDiscount.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked && checkboxSeniorDiscount != null) {
                        checkboxSeniorDiscount.setChecked(false);
                    }
                    updateSummary();
                });
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up checkbox listeners: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void toggleExpandableContent(LinearLayout content, ImageView icon) {
        try {
            if (content != null && icon != null) {
                if (content.getVisibility() == View.GONE) {
                    // Expand
                    content.setVisibility(View.VISIBLE);
                    rotateIcon(icon, 0f, 180f);
                } else {
                    // Collapse
                    content.setVisibility(View.GONE);
                    rotateIcon(icon, 180f, 0f);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error toggling content: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void rotateIcon(ImageView icon, float fromRotation, float toRotation) {
        try {
            if (icon != null) {
                ObjectAnimator rotate = ObjectAnimator.ofFloat(icon, "rotation", fromRotation, toRotation);
                rotate.setDuration(300);
                rotate.setInterpolator(new AccelerateDecelerateInterpolator());
                rotate.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRoomDetails(String roomType, String description) {
        try {
            String message = roomType + "\n\n" + description;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker(boolean isCheckIn) {
        try {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        try {
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(year, month, dayOfMonth);

                            if (isCheckIn) {
                                checkInDate = selectedDate;
                                if (btnCheckInDate != null) {
                                    btnCheckInDate.setText("Check-in: " + dateFormat.format(selectedDate.getTime()));
                                }
                                // Clear check-out date if it's before the new check-in date
                                if (checkOutDate != null && checkOutDate.before(checkInDate)) {
                                    checkOutDate = null;
                                    if (btnCheckOutDate != null) {
                                        btnCheckOutDate.setText("Check-out");
                                    }
                                }
                            } else {
                                if (checkInDate != null && selectedDate.before(checkInDate)) {
                                    Toast.makeText(this, "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                checkOutDate = selectedDate;
                                if (btnCheckOutDate != null) {
                                    btnCheckOutDate.setText("Check-out: " + dateFormat.format(selectedDate.getTime()));
                                }
                            }
                            updateSummary();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error setting date: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            // For check-out, set minimum date to check-in date + 1
            if (!isCheckIn && checkInDate != null) {
                Calendar minCheckOut = (Calendar) checkInDate.clone();
                minCheckOut.add(Calendar.DAY_OF_MONTH, 1);
                datePickerDialog.getDatePicker().setMinDate(minCheckOut.getTimeInMillis());
            }

            datePickerDialog.show();

        } catch (Exception e) {
            Toast.makeText(this, "Error showing date picker: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private int getIntFromEditText(EditText editText) {
        try {
            String text = editText.getText().toString().trim();
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int getBreakfastPrice() {
        try {
            if (spinnerBreakfast != null && spinnerBreakfast.getSelectedItemPosition() > 0) {
                switch (spinnerBreakfast.getSelectedItemPosition()) {
                    case 1: return 150; // Filipino Breakfast
                    case 2: return 200; // American Breakfast
                    case 3: return 180; // Continental Breakfast
                    case 4: return 160; // Vegetarian Breakfast
                    default: return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateSummary() {
        try {
            StringBuilder summary = new StringBuilder();
            double subtotal = 0.0;
            boolean hasSelections = false;

            // Calculate days between check-in and check-out
            int days = 1;
            if (checkInDate != null && checkOutDate != null) {
                long diffInMillies = Math.abs(checkOutDate.getTimeInMillis() - checkInDate.getTimeInMillis());
                days = (int) (diffInMillies / (24 * 60 * 60 * 1000));
                if (days == 0) days = 1; // Minimum 1 day
            }

            // Room selections
            if (checkboxDeluxeRoom != null && checkboxDeluxeRoom.isChecked()) {
                int adults = getIntFromEditText(deluxeAdultsCount);
                int kids = getIntFromEditText(deluxeKidsCount);
                summary.append("• Deluxe Room - ₱").append(DELUXE_ROOM_PRICE).append("/night × ").append(days).append(" days");
                if (adults > 0 || kids > 0) {
                    summary.append(" (").append(adults).append(" adults, ").append(kids).append(" kids)");
                }
                summary.append("\n");
                subtotal += DELUXE_ROOM_PRICE * days;
                hasSelections = true;
            }

            if (checkboxStandardRoom != null && checkboxStandardRoom.isChecked()) {
                int adults = getIntFromEditText(standardAdultsCount);
                int kids = getIntFromEditText(standardKidsCount);
                summary.append("• Standard Room - ₱").append(STANDARD_ROOM_PRICE).append("/night × ").append(days).append(" days");
                if (adults > 0 || kids > 0) {
                    summary.append(" (").append(adults).append(" adults, ").append(kids).append(" kids)");
                }
                summary.append("\n");
                subtotal += STANDARD_ROOM_PRICE * days;
                hasSelections = true;
            }

            if (checkboxSuiteRoom != null && checkboxSuiteRoom.isChecked()) {
                int adults = getIntFromEditText(singleAdultsCount);
                int kids = getIntFromEditText(singleKidsCount);
                summary.append("• Single Room - ₱").append(SINGLE_ROOM_PRICE).append("/night × ").append(days).append(" days");
                if (adults > 0 || kids > 0) {
                    summary.append(" (").append(adults).append(" adults, ").append(kids).append(" kids)");
                }
                summary.append("\n");
                subtotal += SINGLE_ROOM_PRICE * days;
                hasSelections = true;
            }

            if (checkboxFamilyRoom != null && checkboxFamilyRoom.isChecked()) {
                int adults = getIntFromEditText(familyAdultsCount);
                int kids = getIntFromEditText(familyKidsCount);
                summary.append("• Family Room - ₱").append(FAMILY_ROOM_PRICE).append("/night × ").append(days).append(" days");
                if (adults > 0 || kids > 0) {
                    summary.append(" (").append(adults).append(" adults, ").append(kids).append(" kids)");
                }
                summary.append("\n");
                subtotal += FAMILY_ROOM_PRICE * days;
                hasSelections = true;
            }

            // Cottage selections
            if (checkboxSmallCottage != null && checkboxSmallCottage.isChecked()) {
                summary.append("• Small Cottage - ₱").append(SMALL_COTTAGE_PRICE).append("/day × ").append(days).append(" days\n");
                subtotal += SMALL_COTTAGE_PRICE * days;
                hasSelections = true;
            }
            if (checkboxMediumCottage != null && checkboxMediumCottage.isChecked()) {
                summary.append("• Medium Cottage - ₱").append(MEDIUM_COTTAGE_PRICE).append("/day × ").append(days).append(" days\n");
                subtotal += MEDIUM_COTTAGE_PRICE * days;
                hasSelections = true;
            }
            if (checkboxLargeCottage != null && checkboxLargeCottage.isChecked()) {
                summary.append("• Large Cottage - ₱").append(LARGE_COTTAGE_PRICE).append("/day × ").append(days).append(" days\n");
                subtotal += LARGE_COTTAGE_PRICE * days;
                hasSelections = true;
            }
            if (checkboxPremiumCottage != null && checkboxPremiumCottage.isChecked()) {
                summary.append("• Premium Cottage - ₱").append(PREMIUM_COTTAGE_PRICE).append("/day × ").append(days).append(" days\n");
                subtotal += PREMIUM_COTTAGE_PRICE * days;
                hasSelections = true;
            }

            // Amenity selections
            if (checkboxPool != null && checkboxPool.isChecked()) {
                summary.append("• Swimming Pool Access - ₱").append(POOL_PRICE).append("/person\n");
                subtotal += POOL_PRICE;
                hasSelections = true;
            }
            if (checkboxKaraoke != null && checkboxKaraoke.isChecked()) {
                summary.append("• Karaoke Room - ₱").append(KARAOKE_PRICE).append("/hour\n");
                subtotal += KARAOKE_PRICE;
                hasSelections = true;
            }
            if (checkboxBBQ != null && checkboxBBQ.isChecked()) {
                summary.append("• BBQ Grill Set - ₱").append(BBQ_PRICE).append("/day × ").append(days).append(" days\n");
                subtotal += BBQ_PRICE * days;
                hasSelections = true;
            }
            if (checkboxBoat != null && checkboxBoat.isChecked()) {
                summary.append("• Boat Rental - ₱").append(BOAT_PRICE).append("/hour\n");
                subtotal += BOAT_PRICE;
                hasSelections = true;
            }

            // Breakfast selections
            int breakfastPrice = getBreakfastPrice();
            if (breakfastPrice > 0) {
                String breakfastName = spinnerBreakfast.getSelectedItem().toString();
                summary.append("• ").append(breakfastName).append("\n");
                subtotal += breakfastPrice;
                hasSelections = true;
            }

            // Breakfast add-ons
            if (checkboxExtraRice != null && checkboxExtraRice.isChecked()) {
                summary.append("• Extra Rice - ₱").append(EXTRA_RICE_PRICE).append("\n");
                subtotal += EXTRA_RICE_PRICE;
                hasSelections = true;
            }
            if (checkboxExtraDrink != null && checkboxExtraDrink.isChecked()) {
                summary.append("• Extra Drink - ₱").append(EXTRA_DRINK_PRICE).append("\n");
                subtotal += EXTRA_DRINK_PRICE;
                hasSelections = true;
            }
            if (checkboxDessert != null && checkboxDessert.isChecked()) {
                summary.append("• Dessert - ₱").append(DESSERT_PRICE).append("\n");
                subtotal += DESSERT_PRICE;
                hasSelections = true;
            }

            // Date information
            if (checkInDate != null && checkOutDate != null) {
                summary.append("\nDuration: ").append(days).append(" day(s)\n");
                summary.append("Check-in: ").append(dateFormat.format(checkInDate.getTime())).append("\n");
                summary.append("Check-out: ").append(dateFormat.format(checkOutDate.getTime()));
            }

            // Calculate discount
            double discountValue = 0.0;
            boolean hasDiscount = false;
            if ((checkboxSeniorDiscount != null && checkboxSeniorDiscount.isChecked()) ||
                    (checkboxPWDDiscount != null && checkboxPWDDiscount.isChecked())) {
                discountValue = subtotal * DISCOUNT_RATE;
                hasDiscount = true;
            }

            // Calculate total
            double total = subtotal - discountValue;

            // Show or hide summary card
            if (hasSelections && summaryCard != null && summaryText != null) {
                summaryCard.setVisibility(View.VISIBLE);
                summaryText.setText(summary.toString());

                if (subtotalAmount != null) {
                    subtotalAmount.setText(String.format("₱%.0f", subtotal));
                }

                if (hasDiscount && discountLayout != null && discountAmount != null) {
                    discountLayout.setVisibility(View.VISIBLE);
                    discountAmount.setText(String.format("-₱%.0f", discountValue));
                } else if (discountLayout != null) {
                    discountLayout.setVisibility(View.GONE);
                }

                if (totalAmount != null) {
                    totalAmount.setText(String.format("₱%.0f", total));
                }

                // Update payment amount field
                if (editTextPaymentAmount != null) {
                    editTextPaymentAmount.setText(String.format("%.0f", total));
                }

            } else if (summaryCard != null) {
                summaryCard.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error updating summary: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean validateBooking() {
        try {
            // Check if at least one room or cottage is selected
            boolean hasRoomOrCottage =
                    (checkboxDeluxeRoom != null && checkboxDeluxeRoom.isChecked()) ||
                            (checkboxStandardRoom != null && checkboxStandardRoom.isChecked()) ||
                            (checkboxSuiteRoom != null && checkboxSuiteRoom.isChecked()) ||
                            (checkboxFamilyRoom != null && checkboxFamilyRoom.isChecked()) ||
                            (checkboxSmallCottage != null && checkboxSmallCottage.isChecked()) ||
                            (checkboxMediumCottage != null && checkboxMediumCottage.isChecked()) ||
                            (checkboxLargeCottage != null && checkboxLargeCottage.isChecked()) ||
                            (checkboxPremiumCottage != null && checkboxPremiumCottage.isChecked());

            if (!hasRoomOrCottage) {
                Toast.makeText(this, "Please select at least one room or cottage", Toast.LENGTH_LONG).show();
                return false;
            }

            // Check dates
            if (checkInDate == null || checkOutDate == null) {
                Toast.makeText(this, "Please select check-in and check-out dates", Toast.LENGTH_LONG).show();
                return false;
            }

            // Validate guest counts for selected rooms
            if (checkboxDeluxeRoom != null && checkboxDeluxeRoom.isChecked()) {
                int adults = getIntFromEditText(deluxeAdultsCount);
                int kids = getIntFromEditText(deluxeKidsCount);
                if (adults == 0 && kids == 0) {
                    Toast.makeText(this, "Please specify guest count for Deluxe Room", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            if (checkboxStandardRoom != null && checkboxStandardRoom.isChecked()) {
                int adults = getIntFromEditText(standardAdultsCount);
                int kids = getIntFromEditText(standardKidsCount);
                if (adults == 0 && kids == 0) {
                    Toast.makeText(this, "Please specify guest count for Standard Room", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            if (checkboxSuiteRoom != null && checkboxSuiteRoom.isChecked()) {
                int adults = getIntFromEditText(singleAdultsCount);
                int kids = getIntFromEditText(singleKidsCount);
                if (adults == 0 && kids == 0) {
                    Toast.makeText(this, "Please specify guest count for Single Room", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            if (checkboxFamilyRoom != null && checkboxFamilyRoom.isChecked()) {
                int adults = getIntFromEditText(familyAdultsCount);
                int kids = getIntFromEditText(familyKidsCount);
                if (adults == 0 && kids == 0) {
                    Toast.makeText(this, "Please specify guest count for Family Room", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            // Validate payment amount
            if (editTextPaymentAmount != null) {
                String paymentText = editTextPaymentAmount.getText().toString().trim();
                if (paymentText.isEmpty()) {
                    Toast.makeText(this, "Please enter payment amount", Toast.LENGTH_LONG).show();
                    return false;
                }

                try {
                    double paymentAmount = Double.parseDouble(paymentText);
                    if (paymentAmount <= 0) {
                        Toast.makeText(this, "Payment amount must be greater than zero", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid payment amount", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(this, "Error validating booking: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private String getPaymentMethod() {
        try {
            if (radioGroupPayment != null) {
                int selectedId = radioGroupPayment.getCheckedRadioButtonId();
                if (selectedId == R.id.radioCash) {
                    return "Cash";
                } else if (selectedId == R.id.radioGCash) {
                    return "GCash";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Cash"; // Default
    }

    private void processBooking() {
        try {
            if (!validateBooking()) {
                return;
            }

            // Create booking summary
            StringBuilder bookingDetails = new StringBuilder();
            bookingDetails.append("BOOKING CONFIRMATION\n");
            bookingDetails.append("====================\n\n");

            if (summaryText != null) {
                bookingDetails.append(summaryText.getText());
            }

            bookingDetails.append("\n\n");

            // Payment information
            String paymentMethod = getPaymentMethod();
            String paymentAmount = editTextPaymentAmount != null ?
                    editTextPaymentAmount.getText().toString() : "0";

            bookingDetails.append("PAYMENT DETAILS:\n");
            bookingDetails.append("Payment Method: ").append(paymentMethod).append("\n");
            bookingDetails.append("Amount Paid: ₱").append(paymentAmount).append("\n");

            if (subtotalAmount != null) {
                bookingDetails.append("Subtotal: ").append(subtotalAmount.getText()).append("\n");
            }

            if (discountLayout != null && discountLayout.getVisibility() == View.VISIBLE && discountAmount != null) {
                bookingDetails.append("Discount: ").append(discountAmount.getText()).append("\n");
            }

            if (totalAmount != null) {
                bookingDetails.append("Total Amount: ").append(totalAmount.getText()).append("\n");
            }

            // Discount information
            if ((checkboxSeniorDiscount != null && checkboxSeniorDiscount.isChecked()) ||
                    (checkboxPWDDiscount != null && checkboxPWDDiscount.isChecked())) {
                String discountType = checkboxSeniorDiscount.isChecked() ? "Senior Citizen" : "PWD";
                bookingDetails.append("Discount Applied: ").append(discountType).append(" (20%)\n");
            }

            bookingDetails.append("\nBooking Status: CONFIRMED");
            bookingDetails.append("\nBooking ID: BK").append(System.currentTimeMillis());
            bookingDetails.append("\nBooking Date: ").append(dateFormat.format(Calendar.getInstance().getTime()));

            // Show confirmation dialog or toast
            Toast.makeText(this, "Booking confirmed successfully!", Toast.LENGTH_LONG).show();

            // Log booking details for debugging
            System.out.println(bookingDetails.toString());

            // Optional: Clear selections after successful booking
            // clearAllSelections();

            // In a real app, you would:
            // 1. Send data to server/database
            // 2. Generate booking confirmation
            // 3. Send confirmation email/SMS
            // 4. Navigate to confirmation screen
            // 5. Update room availability

        } catch (Exception e) {
            Toast.makeText(this, "Error processing booking: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void clearAllSelections() {
        try {
            // Clear all checkboxes
            for (CheckBox checkbox : allCheckboxes) {
                if (checkbox != null) {
                    checkbox.setChecked(false);
                }
            }

            // Clear guest counts
            if (deluxeAdultsCount != null) deluxeAdultsCount.setText("");
            if (deluxeKidsCount != null) deluxeKidsCount.setText("");
            if (standardAdultsCount != null) standardAdultsCount.setText("");
            if (standardKidsCount != null) standardKidsCount.setText("");
            if (singleAdultsCount != null) singleAdultsCount.setText("");
            if (singleKidsCount != null) singleKidsCount.setText("");
            if (familyAdultsCount != null) familyAdultsCount.setText("");
            if (familyKidsCount != null) familyKidsCount.setText("");

            // Reset dates
            checkInDate = null;
            checkOutDate = null;
            if (btnCheckInDate != null) {
                btnCheckInDate.setText("Check-in");
            }
            if (btnCheckOutDate != null) {
                btnCheckOutDate.setText("Check-out");
            }

            // Reset spinner
            if (spinnerBreakfast != null) {
                spinnerBreakfast.setSelection(0);
            }

            // Reset payment
            if (radioCash != null) {
                radioCash.setChecked(true);
            }
            if (editTextPaymentAmount != null) {
                editTextPaymentAmount.setText("");
            }

            // Hide summary card
            if (summaryCard != null) {
                summaryCard.setVisibility(View.GONE);
            }

            // Hide guest layouts
            if (deluxeGuestLayout != null) deluxeGuestLayout.setVisibility(View.GONE);
            if (standardGuestLayout != null) standardGuestLayout.setVisibility(View.GONE);
            if (singleGuestLayout != null) singleGuestLayout.setVisibility(View.GONE);
            if (familyGuestLayout != null) familyGuestLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility method to get selected items as a list (useful for further processing)
    private List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();

        try {
            if (checkboxDeluxeRoom != null && checkboxDeluxeRoom.isChecked())
                selectedItems.add("Deluxe Room");
            if (checkboxStandardRoom != null && checkboxStandardRoom.isChecked())
                selectedItems.add("Standard Room");
            if (checkboxSuiteRoom != null && checkboxSuiteRoom.isChecked())
                selectedItems.add("Single Room");
            if (checkboxFamilyRoom != null && checkboxFamilyRoom.isChecked())
                selectedItems.add("Family Room");
            if (checkboxSmallCottage != null && checkboxSmallCottage.isChecked())
                selectedItems.add("Small Cottage");
            if (checkboxMediumCottage != null && checkboxMediumCottage.isChecked())
                selectedItems.add("Medium Cottage");
            if (checkboxLargeCottage != null && checkboxLargeCottage.isChecked())
                selectedItems.add("Large Cottage");
            if (checkboxPremiumCottage != null && checkboxPremiumCottage.isChecked())
                selectedItems.add("Premium Cottage");
            if (checkboxPool != null && checkboxPool.isChecked())
                selectedItems.add("Swimming Pool Access");
            if (checkboxKaraoke != null && checkboxKaraoke.isChecked())
                selectedItems.add("Karaoke Room");
            if (checkboxBBQ != null && checkboxBBQ.isChecked())
                selectedItems.add("BBQ Grill Set");
            if (checkboxBoat != null && checkboxBoat.isChecked())
                selectedItems.add("Boat Rental");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectedItems;
    }

    // Utility method to get booking data as an object (for database storage)
    public BookingData getBookingData() {
        try {
            BookingData booking = new BookingData();

            // Room selections with guest counts
            if (checkboxDeluxeRoom != null && checkboxDeluxeRoom.isChecked()) {
                booking.addRoom("Deluxe", DELUXE_ROOM_PRICE,
                        getIntFromEditText(deluxeAdultsCount), getIntFromEditText(deluxeKidsCount));
            }
            if (checkboxStandardRoom != null && checkboxStandardRoom.isChecked()) {
                booking.addRoom("Standard", STANDARD_ROOM_PRICE,
                        getIntFromEditText(standardAdultsCount), getIntFromEditText(standardKidsCount));
            }
            if (checkboxSuiteRoom != null && checkboxSuiteRoom.isChecked()) {
                booking.addRoom("Single", SINGLE_ROOM_PRICE,
                        getIntFromEditText(singleAdultsCount), getIntFromEditText(singleKidsCount));
            }
            if (checkboxFamilyRoom != null && checkboxFamilyRoom.isChecked()) {
                booking.addRoom("Family", FAMILY_ROOM_PRICE,
                        getIntFromEditText(familyAdultsCount), getIntFromEditText(familyKidsCount));
            }

            // Cottage selections
            if (checkboxSmallCottage != null && checkboxSmallCottage.isChecked())
                booking.addCottage("Small", SMALL_COTTAGE_PRICE);
            if (checkboxMediumCottage != null && checkboxMediumCottage.isChecked())
                booking.addCottage("Medium", MEDIUM_COTTAGE_PRICE);
            if (checkboxLargeCottage != null && checkboxLargeCottage.isChecked())
                booking.addCottage("Large", LARGE_COTTAGE_PRICE);
            if (checkboxPremiumCottage != null && checkboxPremiumCottage.isChecked())
                booking.addCottage("Premium", PREMIUM_COTTAGE_PRICE);

            // Amenity selections
            if (checkboxPool != null && checkboxPool.isChecked())
                booking.addAmenity("Swimming Pool", POOL_PRICE);
            if (checkboxKaraoke != null && checkboxKaraoke.isChecked())
                booking.addAmenity("Karaoke", KARAOKE_PRICE);
            if (checkboxBBQ != null && checkboxBBQ.isChecked())
                booking.addAmenity("BBQ Grill", BBQ_PRICE);
            if (checkboxBoat != null && checkboxBoat.isChecked())
                booking.addAmenity("Boat Rental", BOAT_PRICE);

            // Dates
            booking.setCheckInDate(checkInDate);
            booking.setCheckOutDate(checkOutDate);

            // Payment
            booking.setPaymentMethod(getPaymentMethod());
            if (editTextPaymentAmount != null) {
                try {
                    booking.setPaymentAmount(Double.parseDouble(editTextPaymentAmount.getText().toString()));
                } catch (NumberFormatException e) {
                    booking.setPaymentAmount(0.0);
                }
            }

            // Discount
            if (checkboxSeniorDiscount != null && checkboxSeniorDiscount.isChecked()) {
                booking.setDiscountType("Senior Citizen");
                booking.setDiscountRate(DISCOUNT_RATE);
            } else if (checkboxPWDDiscount != null && checkboxPWDDiscount.isChecked()) {
                booking.setDiscountType("PWD");
                booking.setDiscountRate(DISCOUNT_RATE);
            }

            return booking;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

// Helper class to store booking data
class BookingData {
    private List<RoomSelection> rooms = new ArrayList<>();
    private List<CottageSelection> cottages = new ArrayList<>();
    private List<AmenitySelection> amenities = new ArrayList<>();
    private Calendar checkInDate;
    private Calendar checkOutDate;
    private String paymentMethod;
    private double paymentAmount;
    private String discountType;
    private double discountRate;

    public void addRoom(String type, int price, int adults, int kids) {
        rooms.add(new RoomSelection(type, price, adults, kids));
    }

    public void addCottage(String type, int price) {
        cottages.add(new CottageSelection(type, price));
    }

    public void addAmenity(String type, int price) {
        amenities.add(new AmenitySelection(type, price));
    }

    // Getters and setters
    public List<RoomSelection> getRooms() { return rooms; }
    public List<CottageSelection> getCottages() { return cottages; }
    public List<AmenitySelection> getAmenities() { return amenities; }
    public Calendar getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Calendar checkInDate) { this.checkInDate = checkInDate; }
    public Calendar getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Calendar checkOutDate) { this.checkOutDate = checkOutDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public double getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(double paymentAmount) { this.paymentAmount = paymentAmount; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
}

class RoomSelection {
    private String type;
    private int price;
    private int adults;
    private int kids;

    public RoomSelection(String type, int price, int adults, int kids) {
        this.type = type;
        this.price = price;
        this.adults = adults;
        this.kids = kids;
    }

    // Getters
    public String getType() { return type; }
    public int getPrice() { return price; }
    public int getAdults() { return adults; }
    public int getKids() { return kids; }
}

class CottageSelection {
    private String type;
    private int price;

    public CottageSelection(String type, int price) {
        this.type = type;
        this.price = price;
    }

    // Getters
    public String getType() { return type; }
    public int getPrice() { return price; }
}

class AmenitySelection {
    private String type;
    private int price;

    public AmenitySelection(String type, int price) {
        this.type = type;
        this.price = price;
    }

    // Getters
    public String getType() { return type; }
    public int getPrice() { return price; }
}