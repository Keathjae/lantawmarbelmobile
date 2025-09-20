package com.example.lantawmarbelmobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_GUEST = 1;
    private static final int VIEW_TYPE_STAFF = 2;

    private List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = chatList.get(position);
        if ("guest".equals(chat.getReply())) {
            return VIEW_TYPE_GUEST;
        } else {
            return VIEW_TYPE_STAFF;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_GUEST) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_guest_message, parent, false);
            return new GuestViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_staff_message, parent, false);
            return new StaffViewHolder(view);
        }
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        String formattedDate = formatDate(chat.getDatesent());
        String status = chat.getStatus(); // e.g. "sent", "delivered", "read"

        if (holder instanceof GuestViewHolder) {
            ((GuestViewHolder) holder).textMessage.setText(chat.getChat());
            ((GuestViewHolder) holder).textDate.setText(formattedDate);
            setStatusIcon(((GuestViewHolder) holder).imgStatus, status);
        } else if (holder instanceof StaffViewHolder) {
            ((StaffViewHolder) holder).textMessage.setText(chat.getChat());
            ((StaffViewHolder) holder).textDate.setText(formattedDate);
            setStatusIcon(((StaffViewHolder) holder).imgStatus, status);
        }
    }

    private String formatDate(String inputDate) {
        if (inputDate == null) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return inputDate;
        }
    }

    static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textDate;
        ImageView imgStatus;
        GuestViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textDate = itemView.findViewById(R.id.textDate);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textDate;
        ImageView imgStatus;
        StaffViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textDate = itemView.findViewById(R.id.textDate);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }
    }
    private void setStatusIcon(ImageView imageView, String status) {
        if ("sent".equalsIgnoreCase(status)) {
            // single check (gray)
            imageView.setImageResource(R.drawable.ic_done);
            imageView.setColorFilter(
                    imageView.getContext().getResources().getColor(android.R.color.darker_gray)
            );
        } else if ("delivered".equalsIgnoreCase(status)) {
            // double checks (gray)
            imageView.setImageResource(R.drawable.ic_done_all);
            imageView.setColorFilter(
                    imageView.getContext().getResources().getColor(android.R.color.darker_gray)
            );
        } else if ("read".equalsIgnoreCase(status)) {
            // double checks (blue)
            imageView.setImageResource(R.drawable.ic_done_all);
            imageView.setColorFilter(
                    imageView.getContext().getResources().getColor(android.R.color.holo_blue_light)
            );
        } else {
            imageView.setImageDrawable(null); // hide if no status
        }
    }


    public void updateList(List<Chat> newList) {
        chatList = newList;
        notifyDataSetChanged();
    }
}


