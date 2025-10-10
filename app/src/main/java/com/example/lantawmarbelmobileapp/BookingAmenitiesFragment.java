package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class BookingAmenitiesFragment extends Fragment {

    private BookingViewModel viewModel;
    private BookAmenetyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_amenities, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.amenitiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookAmenetyAdapter(getContext(), selectedAmenity -> {
            // ✅ This updates BookingDTO and triggers price recalculation
            viewModel.setAmenity(selectedAmenity);
        });
        recyclerView.setAdapter(adapter);

        // Observe ViewModel and update selection
        viewModel.getAmenity().observe(getViewLifecycleOwner(), amenity -> {
            adapter.setSelectedAmenity(amenity);
        });

        fetchAmenities();
        return view;
    }

    private void fetchAmenities() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAmenities().enqueue(new Callback<List<Amenity>>() {
            @Override
            public void onResponse(Call<List<Amenity>> call, Response<List<Amenity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Amenity preselected = viewModel.getAmenity().getValue();
                    adapter.updateAmenities(response.body(), preselected);
                }
            }

            @Override
            public void onFailure(Call<List<Amenity>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load amenities", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
