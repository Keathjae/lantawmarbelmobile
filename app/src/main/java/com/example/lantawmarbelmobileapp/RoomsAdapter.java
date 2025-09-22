package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> rooms;

    public RoomsAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.name.setText(room.getRoomtype() + " #" + room.getRoomnum());
        holder.desc.setText(room.getDescription());
        holder.price.setText("₱" + room.getPrice());
        holder.capacity.setText( room.getRoomcapacity()+ " Pax");

        // Glide with placeholder and error image
        Glide.with(context)
                .load(room.getImage_url())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image) // show while loading
                        .error(R.drawable.image_not_found)        // show if failed
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, desc, price,capacity;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.roomImage);
            name = itemView.findViewById(R.id.roomName);
            desc = itemView.findViewById(R.id.roomDescription);
            capacity = itemView.findViewById(R.id.roomCapacity);
            price = itemView.findViewById(R.id.roomPrice);
        }
    }
}
