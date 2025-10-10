package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmenityFragment extends Fragment {

    private RecyclerView recyclerView;
    private AmenityAdapter adapter;
    private List<Amenity> amenityList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amenity, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAmenities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AmenityAdapter(getContext(), amenityList);
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
                Toast.makeText(requireContext(), "Failed to load amenities", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
