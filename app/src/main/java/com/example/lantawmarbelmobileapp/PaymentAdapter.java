package com.example.lantawmarbelmobileapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<BookingDTO.PaymentDTO> payments = new ArrayList<>(); // never null

    public void setPayments(List<BookingDTO.PaymentDTO> payments) {
        if (payments != null) {
            this.payments = payments;
        } else {
            this.payments = new ArrayList<>(); // fallback empty list
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        BookingDTO.PaymentDTO payment = payments.get(position);
        holder.text1.setText("₱" + payment.totaltender + " | Ref: " + payment.refNumber);
        holder.text2.setText("Date: " + payment.datePayment);
    }

    @Override
    public int getItemCount() {
        return payments.size(); // safe, payments is never null
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        PaymentViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
