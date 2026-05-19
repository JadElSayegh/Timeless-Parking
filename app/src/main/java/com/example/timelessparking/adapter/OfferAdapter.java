package com.example.timelessparking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timelessparking.R;
import com.example.timelessparking.model.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    private Context context;
    private List<Offer> offerList;

    public OfferAdapter(Context context, List<Offer> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.image.setImageResource(offer.getImageResId());
        holder.title.setText(offer.getTitle());
        holder.description.setText(offer.getDescription());
        holder.date.setText(offer.getDate());
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description, date;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.IvImage);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            date = itemView.findViewById(R.id.tvDate);
        }
    }
}
