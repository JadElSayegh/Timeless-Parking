package com.example.timelessparking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.timelessparking.R;
import com.example.timelessparking.model.Receipt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private List<Receipt> receiptList;

    public ReceiptAdapter(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipt, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        Receipt receipt = receiptList.get(position);
        holder.name.setText(receipt.getName());
        holder.plate.setText(receipt.getLicensePlate());
        holder.total.setText("Total: $" + receipt.getTotal());
        holder.duration.setText("Duration: " + receipt.getDuration());

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(receipt.getTimestamp()));
        holder.timestamp.setText(date);
    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    public static class ReceiptViewHolder extends RecyclerView.ViewHolder {
        TextView name, plate, total, timestamp, duration;

        public ReceiptViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.receiptName);
            plate = itemView.findViewById(R.id.receiptPlate);
            total = itemView.findViewById(R.id.receiptTotal);
            timestamp = itemView.findViewById(R.id.receiptTimestamp);
            duration = itemView.findViewById(R.id.receiptDuration);
        }
    }
}
