package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {

    private BookingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        TextView totalCostText = view.findViewById(R.id.totalCostText);

        Button submitBtn = view.findViewById(R.id.submitBookingButton);

        // ✅ Observe total price from ViewModel
        viewModel.getTotalPrice().observe(getViewLifecycleOwner(), price -> {
            if (price != null) {
                totalCostText.setText("Total: ₱" + price);
            }
        });


        // ✅ Submit Booking
        submitBtn.setOnClickListener(v -> {
            // 1. Map your ViewModel → DTO
            BookingRequest bookingDto = BookingMapper.fromViewModel(viewModel);

            // 2. Make API call
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.storeBooking(bookingDto).enqueue(new Callback<BookingRequest>() {
                @Override
                public void onResponse(Call<BookingRequest> call, Response<BookingRequest> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "✅ Booking stored!", Toast.LENGTH_SHORT).show();
                        viewModel.setStatus("booked"); // update shared state
                    } else {
                        Toast.makeText(getContext(), "❌ Failed: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BookingRequest> call, Throwable t) {
                    Toast.makeText(getContext(), "🚨 Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        return view;
    }
}

