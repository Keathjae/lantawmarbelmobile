package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Go_to_food extends AppCompatActivity {

    // UI Components
    private EditText searchBar;
    private LinearLayout mainDishesSection, drinksSection;
    private ExtendedFloatingActionButton fabOrderSummary;

    // Category buttons
    private CardView categoryAll, categoryMainDish, categoryDrinks, categoryDessert;
    private TextView categoryAllText, categoryMainDishText, categoryDrinksText, categoryDessertText;

    // Food item buttons
    private Button btnAddTapsilog, btnAddSinigang, btnAddFriedRice, btnAddCoke, btnAddIcedTea;

    // Order management
    private List<OrderItem> orderList = new ArrayList<>();
    private Map<String, Integer> orderQuantities = new HashMap<>();
    private double totalAmount = 0.0;

    // Current selected category
    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_food);

        initializeViews();
        setupClickListeners();
        setupCategoryFilters();
        setupSearchFunctionality();
        updateOrderSummaryVisibility();
    }

    private void initializeViews() {
        try {
            // Search bar
            searchBar = findViewById(R.id.searchBar);

            // Sections
            mainDishesSection = findViewById(R.id.mainDishesSection);
            drinksSection = findViewById(R.id.drinksSection);

            // FAB
            fabOrderSummary = findViewById(R.id.fabOrderSummary);

            // Category cards
            categoryAll = findViewById(R.id.categoryAll);
            categoryMainDish = findViewById(R.id.categoryMainDish);
            categoryDrinks = findViewById(R.id.categoryDrinks);
            categoryDessert = findViewById(R.id.categoryDessert);

            // Add buttons
            btnAddTapsilog = findViewById(R.id.btnAddTapsilog);
            btnAddSinigang = findViewById(R.id.btnAddSinigang);
            btnAddFriedRice = findViewById(R.id.btnAddFriedRice);
            btnAddCoke = findViewById(R.id.btnAddCoke);
            btnAddIcedTea = findViewById(R.id.btnAddIcedTea);

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        try {
            // Add to order buttons
            if (btnAddTapsilog != null) {
                btnAddTapsilog.setOnClickListener(v -> addToOrder("Tapsilog", 100.0));
            }
            if (btnAddSinigang != null) {
                btnAddSinigang.setOnClickListener(v -> addToOrder("Sinigang", 200.0));
            }
            if (btnAddFriedRice != null) {
                btnAddFriedRice.setOnClickListener(v -> addToOrder("Fried Rice With Egg", 69.0));
            }
            if (btnAddCoke != null) {
                btnAddCoke.setOnClickListener(v -> addToOrder("Coke", 30.0));
            }
            if (btnAddIcedTea != null) {
                btnAddIcedTea.setOnClickListener(v -> addToOrder("Iced Tea", 25.0));
            }

            // Order summary FAB
            if (fabOrderSummary != null) {
                fabOrderSummary.setOnClickListener(v -> openOrderSummary());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupCategoryFilters() {
        try {
            if (categoryAll != null) {
                categoryAll.setOnClickListener(v -> filterByCategory("All"));
            }
            if (categoryMainDish != null) {
                categoryMainDish.setOnClickListener(v -> filterByCategory("MainDish"));
            }
            if (categoryDrinks != null) {
                categoryDrinks.setOnClickListener(v -> filterByCategory("Drinks"));
            }
            if (categoryDessert != null) {
                categoryDessert.setOnClickListener(v -> filterByCategory("Dessert"));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up category filters: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setupSearchFunctionality() {
        try {
            if (searchBar != null) {
                searchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filterFoodItems(s.toString().toLowerCase());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up search: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void filterByCategory(String category) {
        try {
            currentCategory = category;
            updateCategorySelection(category);

            if (mainDishesSection != null && drinksSection != null) {
                switch (category) {
                    case "All":
                        mainDishesSection.setVisibility(View.VISIBLE);
                        drinksSection.setVisibility(View.VISIBLE);
                        break;
                    case "MainDish":
                        mainDishesSection.setVisibility(View.VISIBLE);
                        drinksSection.setVisibility(View.GONE);
                        break;
                    case "Drinks":
                        mainDishesSection.setVisibility(View.GONE);
                        drinksSection.setVisibility(View.VISIBLE);
                        break;
                    case "Dessert":
                        // Hide all sections since no desserts are available yet
                        mainDishesSection.setVisibility(View.GONE);
                        drinksSection.setVisibility(View.GONE);
                        Toast.makeText(this, "Desserts coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error filtering by category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateCategorySelection(String selectedCategory) {
        try {
            // Reset all category styles
            resetCategoryStyle(categoryAll);
            resetCategoryStyle(categoryMainDish);
            resetCategoryStyle(categoryDrinks);
            resetCategoryStyle(categoryDessert);

            // Highlight selected category
            CardView selectedCard = null;
            switch (selectedCategory) {
                case "All":
                    selectedCard = categoryAll;
                    break;
                case "MainDish":
                    selectedCard = categoryMainDish;
                    break;
                case "Drinks":
                    selectedCard = categoryDrinks;
                    break;
                case "Dessert":
                    selectedCard = categoryDessert;
                    break;
            }

            if (selectedCard != null) {
                highlightCategoryStyle(selectedCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetCategoryStyle(CardView cardView) {
        if (cardView != null) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            TextView textView = (TextView) cardView.getChildAt(0);
            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            }
        }
    }

    private void highlightCategoryStyle(CardView cardView) {
        if (cardView != null) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.teal_700));
            TextView textView = (TextView) cardView.getChildAt(0);
            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700));
            }
        }
    }

    private void filterFoodItems(String searchText) {
        try {
            if (searchText.isEmpty()) {
                // Show all items based on current category
                filterByCategory(currentCategory);
                return;
            }

            // Hide all sections first
            if (mainDishesSection != null) {
                mainDishesSection.setVisibility(View.GONE);
            }
            if (drinksSection != null) {
                drinksSection.setVisibility(View.GONE);
            }

            // Show sections that contain matching items
            boolean hasMainDishMatch = searchText.contains("tapsilog") || searchText.contains("sinigang") ||
                    searchText.contains("fried rice") || searchText.contains("main");
            boolean hasDrinksMatch = searchText.contains("coke") || searchText.contains("iced tea") ||
                    searchText.contains("drink");

            if (hasMainDishMatch && mainDishesSection != null) {
                mainDishesSection.setVisibility(View.VISIBLE);
            }
            if (hasDrinksMatch && drinksSection != null) {
                drinksSection.setVisibility(View.VISIBLE);
            }

            if (!hasMainDishMatch && !hasDrinksMatch) {
                Toast.makeText(this, "No items found for \"" + searchText + "\"", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error filtering items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void addToOrder(String itemName, double price) {
        try {
            // Update quantity map
            int currentQuantity = orderQuantities.getOrDefault(itemName, 0);
            orderQuantities.put(itemName, currentQuantity + 1);

            // Update or add to order list
            boolean itemExists = false;
            for (OrderItem item : orderList) {
                if (item.getName().equals(itemName)) {
                    item.setQuantity(item.getQuantity() + 1);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                orderList.add(new OrderItem(itemName, price, 1));
            }

            // Update total
            totalAmount += price;

            // Update UI
            updateOrderSummaryVisibility();
            Toast.makeText(this, itemName + " added to order!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error adding to order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateOrderSummaryVisibility() {
        try {
            if (fabOrderSummary != null) {
                if (orderList.isEmpty()) {
                    fabOrderSummary.setVisibility(View.GONE);
                } else {
                    fabOrderSummary.setVisibility(View.VISIBLE);
                    fabOrderSummary.setText("View Order (" + getTotalItemCount() + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getTotalItemCount() {
        int total = 0;
        for (OrderItem item : orderList) {
            total += item.getQuantity();
        }
        return total;
    }

    private void openOrderSummary() {
        try {
            Intent intent = new Intent(this, Go_To_OrderSummaryActivity.class);
            // Pass order data to the summary activity
            intent.putParcelableArrayListExtra("orderList", (ArrayList<OrderItem>) orderList);
            intent.putExtra("totalAmount", totalAmount);
            startActivityForResult(intent, 1001);
        } catch (Exception e) {
            Toast.makeText(this, "Error opening order summary: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Handle result from OrderSummaryActivity
            if (data != null) {
                boolean orderPlaced = data.getBooleanExtra("orderPlaced", false);
                if (orderPlaced) {
                    // Clear the current order
                    clearOrder();
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void clearOrder() {
        try {
            orderList.clear();
            orderQuantities.clear();
            totalAmount = 0.0;
            updateOrderSummaryVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Inner class for order items
    public static class OrderItem implements android.os.Parcelable {
        private String name;
        private double price;
        private int quantity;

        public OrderItem(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        protected OrderItem(android.os.Parcel in) {
            name = in.readString();
            price = in.readDouble();
            quantity = in.readInt();
        }

        public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
            @Override
            public OrderItem createFromParcel(android.os.Parcel in) {
                return new OrderItem(in);
            }

            @Override
            public OrderItem[] newArray(int size) {
                return new OrderItem[size];
            }
        };

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getTotalPrice() { return price * quantity; }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeDouble(price);
            dest.writeInt(quantity);
        }
    }
}