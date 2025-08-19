package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<com.example.lantawmarbelmobileapp.NotificationManager.Notification> notifications;
    private com.example.lantawmarbelmobileapp.NotificationManager notificationManager;


    public NotificationAdapter(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;
        this.notifications = new ArrayList<>();
    }

    public void updateNotifications(List<NotificationManager.Notification> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationManager.Notification notification = notifications.get(position);

        holder.messageText.setText(notification.message);
        holder.dateText.setText(notification.date);
        holder.bookingIdText.setText("Booking ID: " + notification.bookingId);

        // Set notification icon based on type
        // 
        switch (notification.type) {
            case "booking_confirmation":
                holder.iconImage.setImageResource(R.drawable.check_circle_ic);
                break;
            case "booking_reminder":
                // Use ic_notification if available, otherwise use a fallback
                try {
                    holder.iconImage.setImageResource(R.drawable.ic_notification);
                } catch (Exception e) {
                    holder.iconImage.setImageResource(android.R.drawable.ic_dialog_info);
                }
                break;
            case "booking_cancellation":
                // Use ic_cancel if available, otherwise use a fallback
                try {
                    holder.iconImage.setImageResource(R.drawable.ic_cancel);
                } catch (Exception e) {
                    holder.iconImage.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                }
                break;
            case "welcome":
                try {
                    holder.iconImage.setImageResource(R.drawable.ic_notification);
                } catch (Exception e) {
                    holder.iconImage.setImageResource(android.R.drawable.ic_dialog_info);
                }
                break;
            default:
                holder.iconImage.setImageResource(android.R.drawable.ic_dialog_info);
                break;
        }

        // Style for unread notifications
        if (!notification.isRead) {
            holder.messageText.setTypeface(holder.messageText.getTypeface(), Typeface.BOLD);
            try {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.unread_notification_bg));
            } catch (Exception e) {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            }
            holder.unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.messageText.setTypeface(Typeface.DEFAULT);
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            holder.unreadIndicator.setVisibility(View.GONE);
        }

        // Handle click events
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mark as read if not already read
                if (!notification.isRead) {
                    notificationManager.markAsRead(notification.id);
                    notification.isRead = true;
                    notifyItemChanged(position);
                }

                // Handle notification click based on type
                handleNotificationClick(notification);
            }
        });
    }

    private void handleNotificationClick(NotificationManager.Notification notification) {
        switch (notification.type) {
            case "booking_confirmation":
                // Show confirmation details or navigate to booking details
                Toast.makeText(context, "Booking " + notification.bookingId + " confirmed!",
                        Toast.LENGTH_SHORT).show();
                break;
            case "booking_reminder":
                // Show reminder details
                Toast.makeText(context, "Reminder for booking " + notification.bookingId,
                        Toast.LENGTH_SHORT).show();
                break;
            case "booking_cancellation":
                // Show cancellation details
                Toast.makeText(context, "Booking " + notification.bookingId + " cancelled",
                        Toast.LENGTH_SHORT).show();
                break;
            case "welcome":
                // Welcome message
                Toast.makeText(context, "Welcome to Lantaw Marbel Resort!",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Notification: " + notification.message,
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView iconImage;
        TextView messageText;
        TextView dateText;
        TextView bookingIdText;
        View unreadIndicator;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.notificationCard);
            iconImage = itemView.findViewById(R.id.notificationIcon);
            messageText = itemView.findViewById(R.id.notificationMessage);
            dateText = itemView.findViewById(R.id.notificationDate);
            bookingIdText = itemView.findViewById(R.id.notificationBookingId);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
        }
    }
}