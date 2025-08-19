package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationManager {

    private static final String PREFS_NAME = "BookingNotifications";
    private Context context;
    private SharedPreferences prefs;

    public NotificationManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Notification data class
    public static class Notification {
        public String id;
        public String message;
        public String date;
        public String bookingId;
        public String type;
        public boolean isRead;

        public Notification(String id, String message, String date, String bookingId, String type, boolean isRead) {
            this.id = id;
            this.message = message;
            this.date = date;
            this.bookingId = bookingId;
            this.type = type;
            this.isRead = isRead;
        }
    }

    // Add a new notification
    public void addNotification(String message, String bookingId, String type) {
        int notificationCount = prefs.getInt("notification_count", 0);
        notificationCount++;

        String notificationKey = "notification_" + notificationCount;
        String currentDate = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(new Date());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(notificationKey + "_message", message);
        editor.putString(notificationKey + "_date", currentDate);
        editor.putString(notificationKey + "_booking_id", bookingId);
        editor.putString(notificationKey + "_type", type);
        editor.putBoolean(notificationKey + "_read", false);
        editor.putInt("notification_count", notificationCount);
        editor.apply();
    }

    // Get all notifications
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        int notificationCount = prefs.getInt("notification_count", 0);

        for (int i = notificationCount; i >= 1; i--) { // Show newest first
            String notificationKey = "notification_" + i;
            String message = prefs.getString(notificationKey + "_message", "");
            String date = prefs.getString(notificationKey + "_date", "");
            String bookingId = prefs.getString(notificationKey + "_booking_id", "");
            String type = prefs.getString(notificationKey + "_type", "");
            boolean isRead = prefs.getBoolean(notificationKey + "_read", false);

            if (!message.isEmpty()) {
                notifications.add(new Notification(notificationKey, message, date, bookingId, type, isRead));
            }
        }

        return notifications;
    }

    // Get unread notification count
    public int getUnreadNotificationCount() {
        int unreadCount = 0;
        int notificationCount = prefs.getInt("notification_count", 0);

        for (int i = 1; i <= notificationCount; i++) {
            String notificationKey = "notification_" + i;
            boolean isRead = prefs.getBoolean(notificationKey + "_read", false);
            if (!isRead) {
                unreadCount++;
            }
        }

        return unreadCount;
    }

    // Mark notification as read
    public void markAsRead(String notificationId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(notificationId + "_read", true);
        editor.apply();
    }

    // Mark all notifications as read
    public void markAllAsRead() {
        int notificationCount = prefs.getInt("notification_count", 0);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 1; i <= notificationCount; i++) {
            String notificationKey = "notification_" + i;
            editor.putBoolean(notificationKey + "_read", true);
        }

        editor.apply();
    }

    // Clear all notifications
    public void clearAllNotifications() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    // Delete specific notification
    public void deleteNotification(String notificationId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(notificationId + "_message");
        editor.remove(notificationId + "_date");
        editor.remove(notificationId + "_booking_id");
        editor.remove(notificationId + "_type");
        editor.remove(notificationId + "_read");
        editor.apply();
    }
}

