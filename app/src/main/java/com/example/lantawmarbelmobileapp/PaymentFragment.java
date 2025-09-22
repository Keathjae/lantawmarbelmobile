package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {

    private BookingViewModel viewModel;
    private PaymentAdapter paymentAdapter;

    private TextView totalCostText;
    private EditText paymentAmountInput, paymentRefInput;
    private Button addPaymentButton, submitBookingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        // ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        // UI Elements
        totalCostText = view.findViewById(R.id.totalCostText);
        paymentAmountInput = view.findViewById(R.id.paymentAmountInput);
        paymentRefInput = view.findViewById(R.id.paymentRefInput);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);
        submitBookingButton = view.findViewById(R.id.submitBookingButton);

        RecyclerView paymentRecyclerView = view.findViewById(R.id.paymentRecyclerView);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        paymentAdapter = new PaymentAdapter();
        paymentRecyclerView.setAdapter(paymentAdapter);

        // Observe Total Price
        viewModel.getTotalPrice().observe(getViewLifecycleOwner(), price -> {
            if (price != null) {
                totalCostText.setText("Total: ₱" + price);
            }
        });

        // Observe Payments
        viewModel.getBilling().observe(getViewLifecycleOwner(), billing -> {
            if (billing != null && billing.payments != null) {
                paymentAdapter.setPayments(billing.payments);
            } else {
                paymentAdapter.setPayments(null); // fall back to empty
            }
        });

        // Add Payment
        addPaymentButton.setOnClickListener(v -> addPayment());

        // Submit Booking
        submitBookingButton.setOnClickListener(v -> submitBooking());

        return view;
    }

    private void addPayment() {
        String amountStr = paymentAmountInput.getText().toString().trim();
        String refNumber = paymentRefInput.getText().toString().trim();

        if (TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(refNumber)) {
            Toast.makeText(getContext(), "Enter both amount and reference number", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // ✅ Format date to MySQL DATETIME (yyyy-MM-dd HH:mm:ss)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date());

        // ✅ use nested BookingViewModel.Payment
        Payment payment = new Payment();
        payment.paymentID = 0; // backend will assign
        payment.totalTender = amount;
        payment.totalChange = 0;
        payment.datePayment = formattedDate; // correct format
        payment.refNumber = refNumber;

        viewModel.addPayment(payment);

        // clear inputs
        paymentAmountInput.setText("");
        paymentRefInput.setText("");
    }

    private void submitBooking() {
        BookingRequest bookingDto = BookingMapper.fromViewModel(viewModel);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        if (bookingDto.bookingID > 0) {
            // ✅ Existing booking → update
            apiService.updateBooking(bookingDto.bookingID, bookingDto).enqueue(new Callback<BookingRequest>() {
                @Override
                public void onResponse(Call<BookingRequest> call, Response<BookingRequest> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "✅ Booking updated!", Toast.LENGTH_SHORT).show();
                        requireActivity().finish(); // close BookingActivity
                    }
                }
                @Override
                public void onFailure(Call<BookingRequest> call, Throwable t) {
                    Toast.makeText(getContext(), "🚨 Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // ✅ New booking → store
            apiService.storeBooking(bookingDto).enqueue(new Callback<BookingRequest>() {
                @Override
                public void onResponse(Call<BookingRequest> call, Response<BookingRequest> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "✅ Booking stored!", Toast.LENGTH_SHORT).show();
                        requireActivity().finish(); // close BookingActivity
                    }
                }
                @Override
                public void onFailure(Call<BookingRequest> call, Throwable t) {
                    Toast.makeText(getContext(), "🚨 Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
