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

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<Menu> menuList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MenuAdapter(getContext(), menuList);
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
