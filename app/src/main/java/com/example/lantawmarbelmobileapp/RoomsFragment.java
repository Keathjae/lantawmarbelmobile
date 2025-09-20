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

public class RoomsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoomsAdapter adapter;
    private List<Room> roomList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RoomsAdapter(getContext(), roomList);
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
