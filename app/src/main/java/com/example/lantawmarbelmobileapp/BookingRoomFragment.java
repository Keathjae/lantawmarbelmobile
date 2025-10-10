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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingRoomFragment extends Fragment {

    private BookingRoomAdapter adapter;
    private BookingViewModel viewModel;
    private static final String ARG_DATE = "arg_date";
    private String date;

    public static BookingRoomFragment newInstance(String date) {
        BookingRoomFragment f = new BookingRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_room, container, false);
        if (getArguments() != null) {
            date = getArguments().getString(ARG_DATE);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.roomsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ new adapter signature: no need to pass `roomList`
        adapter = new BookingRoomAdapter(getContext(), (room, isSelected) -> {
            BookingDTO dto = viewModel.getBooking().getValue();
            if (dto == null) return;
            if (dto.roomBookings == null) dto.roomBookings = new ArrayList<>();

            // remove existing entry for this room/date
            dto.roomBookings.removeIf(rb ->
                    date != null && date.equals(rb.bookingDate) && rb.roomID == room.getRoomID());

            if (isSelected) {
                BookingDTO.RoomBookingDTO rb = new BookingDTO.RoomBookingDTO();
                rb.roomID = room.getRoomID();
                rb.bookingDate = date;
                rb.price = Double.parseDouble(room.getPrice());
                dto.roomBookings.add(rb);
            }

            viewModel.setBooking(dto);
        });

        recyclerView.setAdapter(adapter);

        // Observe changes from ViewModel (re-applies selection)
        viewModel.getBooking().observe(getViewLifecycleOwner(), bookingDTO -> {
            if (bookingDTO == null || bookingDTO.roomBookings == null) return;
            Set<Integer> selectedIds = getSelectedRoomIdsForDate(bookingDTO.roomBookings, date);
            adapter.setSelectedRoomIds(selectedIds);
        });

        fetchRooms();
        return view;
    }

    private Set<Integer> getSelectedRoomIdsForDate(List<BookingDTO.RoomBookingDTO> bookings, String date) {
        Set<Integer> result = new HashSet<>();
        for (BookingDTO.RoomBookingDTO rb : bookings) {
            if (date != null && date.equals(rb.bookingDate)) {
                result.add(rb.roomID);
            }
        }
        return result;
    }

    private void fetchRooms() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Room> newRooms = response.body();

                    // re-apply selection after loading
                    BookingDTO dto = viewModel.getBooking().getValue();
                    if (dto != null && dto.roomBookings != null) {
                        Set<Integer> selectedIds = getSelectedRoomIdsForDate(dto.roomBookings, date);
                        adapter.setSelectedRoomIds(selectedIds);
                    }

                    // ✅ Use DiffUtil for smooth updates
                    adapter.updateRooms(newRooms);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load rooms", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
