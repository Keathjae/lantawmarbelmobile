package com.example.lantawmarbelmobileapp;

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

import java.util.ArrayList;
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

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        totalCostText = view.findViewById(R.id.totalCostText);
        paymentAmountInput = view.findViewById(R.id.paymentAmountInput);
        paymentRefInput = view.findViewById(R.id.paymentRefInput);
        addPaymentButton = view.findViewById(R.id.addPaymentButton);
        submitBookingButton = view.findViewById(R.id.submitBookingButton);

        RecyclerView paymentRecyclerView = view.findViewById(R.id.paymentRecyclerView);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        paymentAdapter = new PaymentAdapter();
        paymentRecyclerView.setAdapter(paymentAdapter);

        // ✅ Just observe booking, ViewModel handles totalPrice
        viewModel.getBooking().observe(getViewLifecycleOwner(), booking -> {
            if (booking != null) {
                totalCostText.setText("Total: ₱" +
                        String.format(Locale.getDefault(), "%.2f", booking.totalPrice));

                if (booking.billing != null && booking.billing.payments != null) {
                    paymentAdapter.setPayments(booking.billing.payments);
                } else {
                    paymentAdapter.setPayments(new ArrayList<>());
                }
            }
        });

        addPaymentButton.setOnClickListener(v -> addPayment());
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
        String formattedDate = DateUtils.toIsoDate(new Date());

        BookingDTO.PaymentDTO payment = new BookingDTO.PaymentDTO();
        payment.paymentID = 0;
        payment.totaltender = amount;
        payment.totalchange = 0;
        payment.datePayment = formattedDate;
        payment.refNumber = refNumber;

        viewModel.addPayment(payment);

        paymentAmountInput.setText("");
        paymentRefInput.setText("");
    }

    private void submitBooking() {
        BookingDTO bookingDto = viewModel.getBooking().getValue();

        if (bookingDto == null) {
            Toast.makeText(getContext(), "Booking data is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Ensure booking meta is filled
        bookingDto.guestAmount = bookingDto.adultGuest + bookingDto.childGuest;
        bookingDto.bookingCreated = DateUtils.toIsoDateTime(new Date());
        bookingDto.bookingType = "Booking";
        bookingDto.status = "Pending";

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<BookingRequest> call;

        if (bookingDto.bookingID > 0) {
            call = apiService.updateBooking(bookingDto.bookingID, bookingDto);
        } else {
            call = apiService.storeBooking(bookingDto);
        }

        call.enqueue(new Callback<BookingRequest>() {
            @Override
            public void onResponse(Call<BookingRequest> call, Response<BookingRequest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String msg = bookingDto.bookingID > 0 ? "✅ Booking updated!" : "✅ Booking stored!";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                } else {
                    Toast.makeText(getContext(), "❌ Failed: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookingRequest> call, Throwable t) {
                Toast.makeText(getContext(), "🚨 Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
