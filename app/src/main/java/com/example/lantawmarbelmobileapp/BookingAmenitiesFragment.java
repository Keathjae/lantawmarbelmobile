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
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAmenitiesFragment extends Fragment {

    private BookingViewModel viewModel;
    private ArrayList<Amenity> amenityList = new ArrayList<>();
    private BookAmenetyAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_amenities, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.amenitiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: Replace this with data fetched from API

        adapter = new BookAmenetyAdapter(getContext(), amenityList, selected -> {
            if (selected.size() > 0) {
                Amenity amenity =selected.get(0) ; // only one
                viewModel.setAmenity(amenity);

                int adultCount = viewModel.getAdultGuest().getValue() != null
                        ? viewModel.getAdultGuest().getValue()
                        : 0;
                int childCount = viewModel.getChildGuest().getValue() != null
                        ? viewModel.getChildGuest().getValue()
                        : 0;

                double total = (adultCount * amenity.getAdultprice()) + (childCount * amenity.getChildprice());
                viewModel.setTotalPrice(total); // replace old total
            }
        });
        recyclerView.setAdapter(adapter);
fetchAmenities();
        return view;
    }
    private void fetchAmenities() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAmenities().enqueue(new Callback<List<Amenity>>() {
            @Override
            public void onResponse(Call<List<Amenity>> call, Response<List<Amenity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    amenityList.clear();
                    amenityList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Amenity>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load amenities", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
