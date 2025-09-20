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

public class BookCottageAdapter extends RecyclerView.Adapter<BookCottageAdapter.CottageViewHolder> {

    private Context context;
    private List<Cottage> cottages;
    private List<Cottage> selectedCottages = new ArrayList<Cottage>();
    private OnCottagesSelectedListener listener;

    public interface OnCottagesSelectedListener {
        void onCottagesSelected(List<Cottage> selected);
    }

    public BookCottageAdapter(Context context,List<Cottage> cottages, OnCottagesSelectedListener listener) {
        this.context=context;
        this.cottages = cottages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CottageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_cottage, parent, false);
        return new CottageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CottageViewHolder holder, int position) {
        Cottage cottage = cottages.get(position);
        holder.checkBox.setText("Avail");

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedCottages.contains(cottage)) selectedCottages.add(cottage);
            } else {
                selectedCottages.remove(cottage);
            }
            listener.onCottagesSelected(selectedCottages);
        });
        holder.name.setText(cottage.getCottageName());
        holder.type.setText(cottage.getCapacity()+" Pax");
        holder.price.setText("₱" + cottage.getPrice());
        holder.status.setText("Status: " + cottage.getStatus());

        String imageUrl = cottage.getImage();

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
        return cottages.size();
    }

    static class CottageViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView image;
        TextView name, type, price, status;

        public CottageViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cottageCheckBox);
            image = itemView.findViewById(R.id.cottageImage);
            name = itemView.findViewById(R.id.cottageName);
            type = itemView.findViewById(R.id.cottageType);
            price = itemView.findViewById(R.id.cottagePrice);
            status = itemView.findViewById(R.id.cottageStatus);
        }
    }
}

