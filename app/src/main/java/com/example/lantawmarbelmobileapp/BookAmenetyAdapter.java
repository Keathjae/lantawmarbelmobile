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
    private List<Amenity> amenities = new ArrayList<>();
    private Amenity selectedAmenity; // only one selected
    private OnAmenitySelectedListener listener;

    public interface OnAmenitySelectedListener {
        void onAmenitySelected(Amenity selected);
    }

    public BookAmenetyAdapter(Context context, OnAmenitySelectedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setSelectedAmenity(Amenity selected) {
        Amenity old = this.selectedAmenity;
        this.selectedAmenity = selected;

        if (old != null) {
            int oldIndex = findIndexById(old.getAmenityID());
            if (oldIndex != -1) notifyItemChanged(oldIndex);
        }
        if (selected != null) {
            int newIndex = findIndexById(selected.getAmenityID());
            if (newIndex != -1) notifyItemChanged(newIndex);
        }
    }

    private int findIndexById(int amenityId) {
        for (int i = 0; i < amenities.size(); i++) {
            if (amenities.get(i).getAmenityID() == amenityId) return i;
        }
        return -1;
    }

    public void updateAmenities(List<Amenity> newAmenities, Amenity preselected) {
        this.amenities.clear();
        if (newAmenities != null) this.amenities.addAll(newAmenities);
        this.selectedAmenity = preselected;
        notifyDataSetChanged();
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

        holder.name.setText(amenity.getAmenityname());
        holder.desc.setText(amenity.getDescription());
        holder.adultPrice.setText("Adult: ₱" + amenity.getAdultprice());
        holder.childPrice.setText("Child: ₱" + amenity.getChildprice());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedAmenity != null &&
                selectedAmenity.getAmenityID() == amenity.getAmenityID());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedAmenity = amenity;
                listener.onAmenitySelected(amenity);
                setSelectedAmenity(amenity);
            } else if (selectedAmenity != null &&
                    selectedAmenity.getAmenityID() == amenity.getAmenityID()) {
                selectedAmenity = null;
                listener.onAmenitySelected(null);
                setSelectedAmenity(null);
            }
        });

        Glide.with(context)
                .load(amenity.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.image_not_found)
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

