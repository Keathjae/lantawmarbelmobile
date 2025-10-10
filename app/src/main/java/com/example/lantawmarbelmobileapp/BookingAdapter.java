package com.example.lantawmarbelmobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<BookingRequest> bookings;
    private final BookingClickListener listener;

    public interface BookingClickListener {
        void onBookingClick(BookingRequest booking);
    }

    public BookingAdapter(List<BookingRequest> bookings, BookingClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingRequest booking = bookings.get(position); // ✅ now BookingDTO

        // Header
        holder.txtBookingID.setText("Booking #" + booking.bookingID);
        holder.txtStatus.setText(booking.status);

//        // Guest
//        if (booking.guest != null) {
//            holder.txtGuestName.setText("Guest: " + booking.guest.firstName + " " + booking.guest.lastName);
//            holder.txtGuestEmail.setText(booking.guest.email);
//        }

        // Dates
        holder.txtDates.setText("📅 " + booking.bookingStart + " → " + booking.bookingEnd);

        // Guests
        holder.txtGuests.setText("👨 " + booking.adultGuest + " Adults, 👦 " + booking.childGuest + " Children");

        // Rooms (take first room if exists)
//        if (booking.roomBookings != null && !booking.roomBookings.isEmpty()) {
//            int roomNum = booking.roomBookings.get(0).room.getRoomnum();
//            holder.txtRooms.setText("🏨 Room: #" + roomNum);
//        } else {
//            holder.txtRooms.setText("🏨 No room assigned");
//        }

        // Total
        holder.txtTotal.setText("💰 Total: ₱" + booking.totalPrice);

        // Status color
        switch (booking.status.toLowerCase()) {
            case "pending":
                holder.txtStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_dark));
                break;
            case "confirmed":
            case "paid":
                holder.txtStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "cancelled":
                holder.txtStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
                break;
            default:
                holder.txtStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
                break;
        }

        // Click
        holder.itemView.setOnClickListener(v -> listener.onBookingClick(booking));

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookingID, txtStatus, txtGuestName, txtGuestEmail, txtDates, txtGuests, txtRooms, txtTotal;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookingID = itemView.findViewById(R.id.txtBookingID);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtGuestName = itemView.findViewById(R.id.txtGuestName);
            txtGuestEmail = itemView.findViewById(R.id.txtGuestEmail);
            txtDates = itemView.findViewById(R.id.txtDates);
            txtGuests = itemView.findViewById(R.id.txtGuests);
            txtRooms = itemView.findViewById(R.id.txtRooms);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }
    }
}
