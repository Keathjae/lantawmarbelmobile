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

public class CottageFragment extends Fragment {

    private RecyclerView recyclerView;
    private CottageAdapter adapter;
    private ArrayList<Cottage> cottageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cottage, container, false);

        recyclerView = view.findViewById(R.id.cottageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CottageAdapter(getContext(), cottageList);
        recyclerView.setAdapter(adapter);

        fetchcottages();

        return view;
    }

    private void fetchcottages() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCottages().enqueue(new Callback<List<Cottage>>() {
            @Override
            public void onResponse(Call<List<Cottage>> call, Response<List<Cottage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cottageList.clear();
                    cottageList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No cottage found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cottage>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load cottage", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
