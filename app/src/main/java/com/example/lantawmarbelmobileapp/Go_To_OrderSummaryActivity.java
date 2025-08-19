package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Go_To_OrderSummaryActivity extends AppCompatActivity {

    private LinearLayout orderItemsContainer;
    private TextView totalAmountText, itemCountText;
    private Button btnPlaceOrder, btnContinueShopping;
    private List<Go_to_food.OrderItem> orderList;
    private double totalAmount;
    private DecimalFormat priceFormat = new DecimalFormat("₱#,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_go_to_order_summary);

        initializeViews();
        setupToolbar();
        loadOrderData();
        setupClickListeners();
        displayOrderItems();
    }

    private void initializeViews() {
        try {
            orderItemsContainer = findViewById(R.id.orderItemsContainer);
            totalAmountText = findViewById(R.id.totalAmountText);
            itemCountText = findViewById(R.id.itemCountText);
            btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
            btnContinueShopping = findViewById(R.id.btnContinueShopping);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Order Summary");
                }
                toolbar.setNavigationOnClickListener(v -> finish());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOrderData() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                orderList = intent.getParcelableArrayListExtra("orderList");
                totalAmount = intent.getDoubleExtra("totalAmount", 0.0);

                if (orderList == null) {
                    orderList = new ArrayList<>();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading order data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            orderList = new ArrayList<>();
        }
    }

    private void setupClickListeners() {
        try {
            if (btnPlaceOrder != null) {
                btnPlaceOrder.setOnClickListener(v -> showPlaceOrderDialog());
            }

            if (btnContinueShopping != null) {
                btnContinueShopping.setOnClickListener(v -> finish());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void displayOrderItems() {
        try {
            if (orderItemsContainer == null) return;

            orderItemsContainer.removeAllViews();

            if (orderList.isEmpty()) {
                showEmptyOrderView();
                return;
            }

            for (int i = 0; i < orderList.size(); i++) {
                Go_to_food.OrderItem item = orderList.get(i);
                View itemView = createOrderItemView(item, i);
                orderItemsContainer.addView(itemView);
            }

            updateTotalDisplay();

        } catch (Exception e) {
            Toast.makeText(this, "Error displaying order items: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private View createOrderItemView(Go_to_food.OrderItem item, int position) {
        View itemView = getLayoutInflater().inflate(R.layout.orderitemlayout, null);

        try {
            TextView itemName = itemView.findViewById(R.id.itemName);
            TextView itemPrice = itemView.findViewById(R.id.itemPrice);
            TextView itemQuantity = itemView.findViewById(R.id.itemQuantity);
            TextView itemTotal = itemView.findViewById(R.id.itemTotal);
            Button btnDecrease = itemView.findViewById(R.id.btnDecrease);
            Button btnIncrease = itemView.findViewById(R.id.btnIncrease);
            Button btnRemove = itemView.findViewById(R.id.btnRemove);

            if (itemName != null) itemName.setText(item.getName());
            if (itemPrice != null) itemPrice.setText(priceFormat.format(item.getPrice()));
            if (itemQuantity != null) itemQuantity.setText(String.valueOf(item.getQuantity()));
            if (itemTotal != null) itemTotal.setText(priceFormat.format(item.getTotalPrice()));

            // Set up quantity controls
            if (btnDecrease != null) {
                btnDecrease.setOnClickListener(v -> decreaseQuantity(position));
            }
            if (btnIncrease != null) {
                btnIncrease.setOnClickListener(v -> increaseQuantity(position));
            }
            if (btnRemove != null) {
                btnRemove.setOnClickListener(v -> removeItem(position));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemView;
    }

    private void showEmptyOrderView() {
        try {
            View emptyView = getLayoutInflater().inflate(R.layout.emptyorderlayout, null);
            orderItemsContainer.addView(emptyView);

            if (btnPlaceOrder != null) {
                btnPlaceOrder.setEnabled(false);
                btnPlaceOrder.setAlpha(0.5f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void increaseQuantity(int position) {
        try {
            if (position >= 0 && position < orderList.size()) {
                Go_to_food.OrderItem item = orderList.get(position);
                item.setQuantity(item.getQuantity() + 1);
                totalAmount += item.getPrice();
                displayOrderItems();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error increasing quantity", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void decreaseQuantity(int position) {
        try {
            if (position >= 0 && position < orderList.size()) {
                Go_to_food.OrderItem item = orderList.get(position);
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    totalAmount -= item.getPrice();
                    displayOrderItems();
                } else {
                    removeItem(position);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error decreasing quantity", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void removeItem(int position) {
        try {
            if (position >= 0 && position < orderList.size()) {
                Go_to_food.OrderItem item = orderList.get(position);
                totalAmount -= item.getTotalPrice();
                orderList.remove(position);
                displayOrderItems();
                Toast.makeText(this, item.getName() + " removed from order", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error removing item", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateTotalDisplay() {
        try {
            int totalItems = 0;
            for (Go_to_food.OrderItem item : orderList) {
                totalItems += item.getQuantity();
            }

            if (totalAmountText != null) {
                totalAmountText.setText(priceFormat.format(totalAmount));
            }
            if (itemCountText != null) {
                itemCountText.setText(totalItems + " item(s)");
            }

            // Enable/disable place order button
            if (btnPlaceOrder != null) {
                if (orderList.isEmpty()) {
                    btnPlaceOrder.setEnabled(false);
                    btnPlaceOrder.setAlpha(0.5f);
                } else {
                    btnPlaceOrder.setEnabled(true);
                    btnPlaceOrder.setAlpha(1.0f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPlaceOrderDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Order");

            StringBuilder orderSummary = new StringBuilder();
            orderSummary.append("Order Details:\n\n");

            for (Go_to_food.OrderItem item : orderList) {
                orderSummary.append("• ").append(item.getName())
                        .append(" x").append(item.getQuantity())
                        .append(" = ").append(priceFormat.format(item.getTotalPrice()))
                        .append("\n");
            }

            orderSummary.append("\nTotal: ").append(priceFormat.format(totalAmount));
            orderSummary.append("\n\nProceed with this order?");

            builder.setMessage(orderSummary.toString());

            builder.setPositiveButton("Place Order", (dialog, which) -> {
                placeOrder();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Error showing order dialog: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void placeOrder() {
        try {
            // In a real app, this would send the order to a server
            // For now, we'll just simulate order placement

            Toast.makeText(this, "Processing your order...", Toast.LENGTH_SHORT).show();

            // Simulate processing time
            new android.os.Handler().postDelayed(() -> {
                try {
                    // Return result to parent activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("orderPlaced", true);
                    resultIntent.putExtra("orderTotal", totalAmount);
                    setResult(RESULT_OK, resultIntent);

                    // Show success message and finish
                    Toast.makeText(this, "Order placed successfully! Thank you for your order.", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 2000);

        } catch (Exception e) {
            Toast.makeText(this, "Error placing order: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}