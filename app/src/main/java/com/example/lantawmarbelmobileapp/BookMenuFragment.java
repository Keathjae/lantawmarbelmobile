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

public class BookMenuFragment extends Fragment {

    private BookingViewModel viewModel;
    private List<Menu> menuList = new ArrayList<>();
    BookMenuAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_menu, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: Replace with menu items from API

      adapter = new BookMenuAdapter(getContext(),menuList, selected -> {
            for(int i=0;i<selected.size();i++){
                viewModel.addMenu(selected.get(i));
                viewModel.addToTotalPrice(Double.parseDouble(selected.get(i).getPrice()));
            }
        });

        recyclerView.setAdapter(adapter);
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
