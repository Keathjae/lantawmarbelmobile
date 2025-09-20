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

public class BookAmenetyAdapter extends RecyclerView.Adapter<BookAmenetyAdapter.AmenityViewHolder> {
    private Context context;

    private List<Amenity> amenities;
    private List<Amenity> selectedAmenities = new ArrayList<>();
    private OnAmenitiesSelectedListener listener;

    public interface OnAmenitiesSelectedListener {
        void onAmenitiesSelected(List<Amenity> selected);
    }

    public BookAmenetyAdapter(Context context,ArrayList<Amenity> amenities, OnAmenitiesSelectedListener listener) {
        this.amenities = amenities;
        this.context=context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AmenityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_amenities, parent, false);
        return new AmenityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenityViewHolder holder, int position) {
        Amenity amenity = amenities.get(position);
        holder.checkBox.setText("Avail");

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedAmenities.contains(amenity)) selectedAmenities.add(amenity);
            } else {
                selectedAmenities.remove(amenity);
            }
            listener.onAmenitiesSelected(selectedAmenities);
        });

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

    static class AmenityViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView image;
        TextView name, desc, adultPrice, childPrice;

        public AmenityViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.amenityCheckBox);
            image = itemView.findViewById(R.id.amenityImage);
            name = itemView.findViewById(R.id.amenityName);
            desc = itemView.findViewById(R.id.amenityDescription);
            adultPrice = itemView.findViewById(R.id.amenityAdultPrice);
            childPrice = itemView.findViewById(R.id.amenityChildPrice);
        }
    }
}
