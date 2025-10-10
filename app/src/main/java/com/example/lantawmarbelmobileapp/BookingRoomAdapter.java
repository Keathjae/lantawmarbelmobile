package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingRoomAdapter extends RecyclerView.Adapter<BookingRoomAdapter.RoomViewHolder> {

    private final Context context;
    private final OnRoomClickListener listener;

    private List<Room> rooms = new ArrayList<>();           // ✅ adapter owns its copy
    private final Set<Integer> selectedRoomIds = new HashSet<>();

    public interface OnRoomClickListener {
        void onRoomClicked(Room room, boolean isSelected);
    }

    public BookingRoomAdapter(Context context, OnRoomClickListener listener) {
        this.context = context;
        this.listener = listener;
        setHasStableIds(true); // ✅ preserve row states
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.name.setText(room.getRoomtype() + " #" + room.getRoomnum());
        holder.desc.setText(room.getDescription());
        holder.price.setText("₱" + room.getPrice());

        Glide.with(context)
                .load(room.getImage_url())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.image_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.image);

        // reset listener first
        holder.checkBox.setOnCheckedChangeListener(null);

        // restore selection
        holder.checkBox.setChecked(selectedRoomIds.contains(room.getRoomID()));
        holder.checkBox.setText("Avail");

        // set new listener
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedRoomIds.add(room.getRoomID());
            } else {
                selectedRoomIds.remove(room.getRoomID());
            }
            listener.onRoomClicked(room, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    @Override
    public long getItemId(int position) {
        return rooms.get(position).getRoomID();
    }

    // ✅ DiffUtil update method
    public void updateRooms(List<Room> newRooms) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override public int getOldListSize() { return rooms.size(); }
            @Override public int getNewListSize() { return newRooms.size(); }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return rooms.get(oldItemPosition).getRoomID() ==
                        newRooms.get(newItemPosition).getRoomID();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return rooms.get(oldItemPosition).equals(newRooms.get(newItemPosition));
            }
        });

        rooms = new ArrayList<>(newRooms); // ✅ replace list
        diffResult.dispatchUpdatesTo(this);
    }

    // ✅ update only changed rows when selection changes
    public void setSelectedRoomIds(Set<Integer> ids) {
        if (ids == null) ids = new HashSet<>();

        // Find what changed
        Set<Integer> old = new HashSet<>(selectedRoomIds);
        if (old.equals(ids)) {
            // ✅ No changes -> do nothing (prevents flicker)
            return;
        }

        // Compute added + removed items
        Set<Integer> added = new HashSet<>(ids);
        added.removeAll(old);

        Set<Integer> removed = new HashSet<>(old);
        removed.removeAll(ids);

        selectedRoomIds.clear();
        selectedRoomIds.addAll(ids);

        // 🔄 Only update affected items
        for (int i = 0; i < rooms.size(); i++) {
            int roomId = rooms.get(i).getRoomID();
            if (added.contains(roomId) || removed.contains(roomId)) {
                notifyItemChanged(i);
            }
        }
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
