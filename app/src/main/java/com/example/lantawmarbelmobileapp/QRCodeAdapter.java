package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QRCodeAdapter extends RecyclerView.Adapter<QRCodeAdapter.ViewHolder> {
    private List<QRCode> qrList;
    private Context context;

    public QRCodeAdapter(Context context, List<QRCode> qrList) {
        this.context = context;
        this.qrList = qrList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QRCode qr = qrList.get(position);
        holder.title.setText("QR ID: " + qr.qrID);
        holder.subtitle.setText("Accessed: " + qr.accessdate);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, QRDetailActivity.class);
            intent.putExtra("qr_url", qr.qr_url);
            intent.putExtra("accessdate", qr.accessdate);
            intent.putExtra("amenity", qr.amenity != null ? qr.amenity.getAmenityname() : "");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return qrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            subtitle = itemView.findViewById(android.R.id.text2);
        }
    }
}
