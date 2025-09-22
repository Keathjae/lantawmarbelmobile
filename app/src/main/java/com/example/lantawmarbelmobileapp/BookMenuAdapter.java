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

public class BookMenuAdapter extends RecyclerView.Adapter<BookMenuAdapter.MenuViewHolder> {
    private Context context;
    private List<Menu> menuList;

       private List<Menu> selectedMenu = new ArrayList<>();
    private OnMenuSelectedListener listener;

    public interface OnMenuSelectedListener {
        void onMenuSelected(List<Menu> selected);
    }

    public BookMenuAdapter(Context context,List<Menu> menuItems, OnMenuSelectedListener listener) {
        this.menuList = menuItems;
        this.listener = listener;
        this.context=context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menuList.get(position);
        holder.checkBox.setText("Avail");

        holder.name.setText(menu.getMenuname());
        holder.type.setText("Type: " + menu.getItemtype());
        holder.price.setText("₱" + menu.getPrice());
        holder.status.setText("Status: " + menu.getStatus());

        String imageUrl = menu.getImage();

        Glide.with(context)
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : null)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.image_not_found)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop())
                .into(holder.image);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedMenu.contains(menu)) selectedMenu.add(menu);
            } else {
                selectedMenu.remove(menu);
            }
            listener.onMenuSelected(selectedMenu);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView image;
        TextView name, type, price, status;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.menuCheckBox);
            image = itemView.findViewById(R.id.menuImage);
            name = itemView.findViewById(R.id.menuName);
            type = itemView.findViewById(R.id.menuType);
            price = itemView.findViewById(R.id.menuPrice);
            status = itemView.findViewById(R.id.menuStatus);
        }
    }
}
