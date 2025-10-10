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
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class BookCottageFragment extends Fragment {

    private BookingViewModel viewModel;
    private BookCottageAdapter adapter;
    private static final String ARG_DATE = "arg_date";
    private String date;

    public static BookCottageFragment newInstance(String date) {
        BookCottageFragment f = new BookCottageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_cottage, container, false);

        if (getArguments() != null) {
            date = getArguments().getString(ARG_DATE);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.cottagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookCottageAdapter(getContext(), selectedIds -> {
            BookingDTO dto = viewModel.getBooking().getValue();
            if (dto == null) return;
            if (dto.cottageBookings == null) dto.cottageBookings = new ArrayList<>();

            // remove existing entries for this date
            dto.cottageBookings.removeIf(cb ->
                    date != null && date.equals(cb.bookingDate) && selectedIds.contains(cb.cottageID) == false);

            for (int id : selectedIds) {
                boolean exists = false;
                for (BookingDTO.CottageBookingDTO cb : dto.cottageBookings) {
                    if (cb.cottageID == id && date.equals(cb.bookingDate)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    BookingDTO.CottageBookingDTO cb = new BookingDTO.CottageBookingDTO();
                    cb.cottageID = id;
                    cb.bookingDate = date;

// assign price from the Cottage object
                    for (Cottage cottage : adapter.getCottages()) {
                        if (cottage.getCottageID() == id) {
                            cb.price =Double.parseDouble(cottage.getPrice());
                            break;
                        }
                    }

                    dto.cottageBookings.add(cb);

                }
            }
            viewModel.setBooking(dto);
        });

        recyclerView.setAdapter(adapter);

        // observe ViewModel
        viewModel.getBooking().observe(getViewLifecycleOwner(), bookingDTO -> {
            if (bookingDTO == null || bookingDTO.cottageBookings == null) return;

            List<Integer> selectedIds = new ArrayList<>();
            for (BookingDTO.CottageBookingDTO cb : bookingDTO.cottageBookings) {
                if (date != null && date.equals(cb.bookingDate)) {
                    selectedIds.add(cb.cottageID);
                }
            }
            adapter.setSelectedCottages(selectedIds);
        });

        fetchCottages();
        return view;
    }

    private void fetchCottages() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCottages().enqueue(new Callback<List<Cottage>>() {
            @Override
            public void onResponse(Call<List<Cottage>> call, Response<List<Cottage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // reapply selection after loading
                    BookingDTO dto = viewModel.getBooking().getValue();
                    List<Integer> selectedIds = new ArrayList<>();
                    if (dto != null && dto.cottageBookings != null) {
                        for (BookingDTO.CottageBookingDTO cb : dto.cottageBookings) {
                            if (date != null && date.equals(cb.bookingDate)) {
                                selectedIds.add(cb.cottageID);
                            }
                        }
                    }

                    adapter.updateCottages(response.body(), selectedIds);
                } else {
                    Toast.makeText(getContext(), "No cottage found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cottage>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load cottages", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
