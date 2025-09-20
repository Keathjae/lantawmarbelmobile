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

public class BookingRoomFragment extends Fragment {
    private BookingRoomAdapter adapter;
    private List<Room> roomList = new ArrayList<>();
    private BookingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_room, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: fetch from API based on booking dates
        adapter = new BookingRoomAdapter(getContext(),roomList, selected -> {
            for(int i=0;i<selected.size();i++) {
                viewModel.addRoom(selected.get(i));
                viewModel.addToTotalPrice(Double.parseDouble(selected.get(i).getPrice()));
            }});
        adapter.setSelectedRooms(viewModel.getRoomBookings().getValue());
        recyclerView.setAdapter(adapter);
fetchRooms();
        return view;
    }
    private void fetchRooms() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear();
                    roomList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load rooms", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
