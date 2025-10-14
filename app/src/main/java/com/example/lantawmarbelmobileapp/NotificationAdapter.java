// NotificationAdapter.java
package com.example.lantawmarbelmobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lantawmarbelmobileapp.R;
import com.example.lantawmarbelmobileapp.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notificationList;
    private OnItemClickListener listener;
    public NotificationAdapter(List<NotificationModel> notificationList, OnItemClickListener listener) {
        this.notificationList = notificationList;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(NotificationModel notification);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.body.setText(notification.getBody());
        holder.date.setText(notification.getCreatedAt());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(notification));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, body, date;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.textTitle);
            body = view.findViewById(R.id.textBody);
            date = view.findViewById(R.id.textDate);
        }
    }
}
