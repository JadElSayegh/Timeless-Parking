package com.example.timelessparking.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.timelessparking.R;
import com.example.timelessparking.model.ParkingSlot;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotAdapter extends RecyclerView.Adapter<ParkingSlotAdapter.ViewHolder> {

    private List<ParkingSlot> slotList;

    public ParkingSlotAdapter(List<ParkingSlot> slotList) {
        this.slotList = new ArrayList<>(slotList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingSlot slot = slotList.get(position);
        holder.nameTextView.setText(slot.getName());
        holder.statusTextView.setText(slot.getStatus());

        if ("Available".equals(slot.getStatus())) {
            holder.statusTextView.setTextColor(Color.GREEN);
        } else {
            holder.statusTextView.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public void updateList(List<ParkingSlot> newList) {
        this.slotList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void updateSlotStatus(String slotName, String newStatus) {
        for (int i = 0; i < slotList.size(); i++) {
            ParkingSlot slot = slotList.get(i);
            if (slot.getName().equalsIgnoreCase(slotName)) {
                slot.setStatus(newStatus);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.slotNameTextView);
            statusTextView = itemView.findViewById(R.id.slotStatusTextView);
        }
    }
}
