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
    private List<Cottage> cottages = new ArrayList<>();
    private List<Integer> selectedCottageIds = new ArrayList<>();
    private OnCottagesSelectedListener listener;

    public interface OnCottagesSelectedListener {
        void onCottagesSelected(List<Integer> selectedIds);
    }

    public BookCottageAdapter(Context context, OnCottagesSelectedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateCottages(List<Cottage> newCottages, List<Integer> preselectedIds) {
        this.cottages.clear();
        if (newCottages != null) this.cottages.addAll(newCottages);

        this.selectedCottageIds.clear();
        if (preselectedIds != null) this.selectedCottageIds.addAll(preselectedIds);

        notifyDataSetChanged();
    }

    public void setSelectedCottages(List<Integer> selectedIds) {
        this.selectedCottageIds.clear();
        if (selectedIds != null) this.selectedCottageIds.addAll(selectedIds);
        notifyDataSetChanged();
    }

    // ✅ getter for cottages so Fragment can access them
    public List<Cottage> getCottages() {
        return cottages;
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

        holder.name.setText(cottage.getCottagename());
        holder.type.setText(cottage.getCapacity() + " Pax");
        holder.price.setText("₱" + cottage.getPrice());
        holder.status.setText("Status: " + cottage.getStatus());

        // ✅ tie checkbox to selected list
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedCottageIds.contains(cottage.getCottageID()));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedCottageIds.contains(cottage.getCottageID())) {
                    selectedCottageIds.add(cottage.getCottageID());
                }
            } else {
                selectedCottageIds.remove((Integer) cottage.getCottageID());
            }
            listener.onCottagesSelected(new ArrayList<>(selectedCottageIds));
        });

        Glide.with(context)
                .load(cottage.getImage_url())
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
