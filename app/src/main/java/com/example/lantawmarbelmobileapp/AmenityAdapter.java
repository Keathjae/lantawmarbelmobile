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

public class AmenityAdapter extends RecyclerView.Adapter<AmenityAdapter.AmenityViewHolder> {

    private Context context;
    private List<Amenity> amenities;

    public AmenityAdapter(Context context, List<Amenity> amenities) {
        this.context = context;
        this.amenities = amenities;
    }

    @NonNull
    @Override
    public AmenityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_amenity, parent, false);
        return new AmenityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenityViewHolder holder, int position) {
        Amenity amenity = amenities.get(position);

        holder.name.setText(amenity.getAmenityname());
        holder.desc.setText(amenity.getDescription());
        holder.adultPrice.setText("Adult: ₱" + amenity.getAdultprice());
        holder.childPrice.setText("Child: ₱" + amenity.getChildprice());

        String imageUrl = amenity.getImage();

        // Load image safely with placeholder and error image
        Glide.with(context)
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : null) // avoid null
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)  // temporary while loading
                        .error(R.drawable.image_not_found)         // default if fails
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return amenities.size();
    }

    public static class AmenityViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, desc, adultPrice, childPrice;

        public AmenityViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.amenityImage);
            name = itemView.findViewById(R.id.amenityName);
            desc = itemView.findViewById(R.id.amenityDescription);
            adultPrice = itemView.findViewById(R.id.amenityAdultPrice);
            childPrice = itemView.findViewById(R.id.amenityChildPrice);
        }
    }
}
