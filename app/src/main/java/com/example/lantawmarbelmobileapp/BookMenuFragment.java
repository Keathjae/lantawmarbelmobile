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

public class BookMenuFragment extends Fragment {

    private BookingViewModel viewModel;
    private List<Menu> menuList = new ArrayList<>();
    private BookMenuAdapter adapter;
    private static final String ARG_DATE = "arg_date";
    private String date;

    public static BookMenuFragment newInstance(String date) {
        BookMenuFragment f = new BookMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_menu, container, false);

        if (getArguments() != null) {
            date = getArguments().getString(ARG_DATE);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookMenuAdapter(getContext(), menuList, selected -> {
            // reset and add selected menus with proper date
            viewModel.resetMenuBookings();
            for (Menu m : selected) {
                BookingDTO.MenuBookingDTO booking = new BookingDTO.MenuBookingDTO();
                booking.menuID = m.getMenuID();
                booking.quantity = m.getqty();
                booking.price=Double.parseDouble(m.getPrice());
                booking.bookingDate = date; // use ARG_DATE
                viewModel.addMenuBooking(booking);
            }
        });

        recyclerView.setAdapter(adapter);

        // observe ViewModel changes and re-apply to adapter
        viewModel.getMenuBookings().observe(getViewLifecycleOwner(), menuBookings -> {
            if (menuBookings != null) {
                List<Menu> selectedMenus = new ArrayList<>();
                for (BookingDTO.MenuBookingDTO mb : menuBookings) {
                    for (Menu m : menuList) {
                        if (m.getMenuID() == mb.menuID) {
                            m.setqty(mb.quantity);
                            m.setSelected(true);
                            selectedMenus.add(m);
                        }
                    }
                }
                adapter.setSelectedMenu(selectedMenus);
            }
        });

        fetchMenus();
        return view;
    }

    private void fetchMenus() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMenus().enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    menuList.clear();
                    menuList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No menu found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load menu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
