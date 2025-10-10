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

public class CottageAdapter extends RecyclerView.Adapter<CottageAdapter.CottageViewHolder> {

    private Context context;
    private List<Cottage> cottageList;

    public CottageAdapter(Context context, List<Cottage> cottageList) {
        this.context = context;
        this.cottageList = cottageList;
    }

    @NonNull
    @Override
    public CottageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cottage, parent, false);
        return new CottageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CottageViewHolder holder, int position) {
        Cottage cottage = cottageList.get(position);
        holder.name.setText(cottage.getCottagename());
        holder.type.setText(cottage.getCapacity()+" Pax");
        holder.price.setText("₱" + cottage.getPrice());
        holder.status.setText("Status: " + cottage.getStatus());

        String imageUrl = cottage.getImage_url();

        Glide.with(context)
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : null)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.image_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return cottageList.size();
    }

    public static class CottageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, type, price, status;

        public CottageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cottageImage);
            name = itemView.findViewById(R.id.cottageName);
            type = itemView.findViewById(R.id.cottageType);
            price = itemView.findViewById(R.id.cottagePrice);
            status = itemView.findViewById(R.id.cottageStatus);
        }
    }
}
