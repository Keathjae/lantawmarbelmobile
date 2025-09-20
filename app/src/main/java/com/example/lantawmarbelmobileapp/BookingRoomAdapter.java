package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class BookingRoomAdapter extends RecyclerView.Adapter<BookingRoomAdapter.RoomViewHolder> {
    private final Context context;
    private final List<Room> rooms;
    private final List<Room> selectedRooms = new ArrayList<>();
    private final OnRoomsSelectedListener listener;

    public interface OnRoomsSelectedListener {
        void onRoomsSelected(List<Room> selected);
    }

    public BookingRoomAdapter(Context context, List<Room> rooms, OnRoomsSelectedListener listener) {
        this.rooms = rooms;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.name.setText(room.getRoomtype() + " #" + room.getRoomnum());
        holder.desc.setText(room.getDescription());
        holder.price.setText("₱" + room.getPrice());

        // Load image with Glide
        Glide.with(context)
                .load(room.getImage_url())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.image_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.image);

        // ---- Handle Checkbox State ----
        // Remove previous listener to avoid duplicate triggers
        holder.checkBox.setOnCheckedChangeListener(null);

        // Set initial state based on selection
        holder.checkBox.setChecked(selectedRooms.contains(room));

        holder.checkBox.setText("Avail");
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedRooms.contains(room)) {
                    selectedRooms.add(room);
                }
            } else {
                selectedRooms.remove(room);
            }
            listener.onRoomsSelected(new ArrayList<>(selectedRooms));
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setSelectedRooms(List<Room> alreadySelected) {
        selectedRooms.clear();
        if (alreadySelected != null) {
            selectedRooms.addAll(alreadySelected);
        }
        notifyDataSetChanged();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView image;
        TextView name, desc, price;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.roomCheckBox);
            image = itemView.findViewById(R.id.roomImage);
            name = itemView.findViewById(R.id.roomName);
            desc = itemView.findViewById(R.id.roomDescription);
            price = itemView.findViewById(R.id.roomPrice);
        }
    }
}
