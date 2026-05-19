package com.example.timelessparking.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.timelessparking.R;
import com.example.timelessparking.ReplacerActivity;
import com.example.timelessparking.adapter.OfferAdapter;
import com.example.timelessparking.model.Offer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Home extends Fragment {

    private static final String ADMIN_UID = "Gi3xgfOfYRW2BN9BBv0qG3pH1c03";
    private ArrayList<Offer> offerList;
    private OfferAdapter adapter;
    private FirebaseFirestore db;

    public Home() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        offerList = new ArrayList<>();

        // Static offers
        offerList.add(new Offer(R.drawable.ic_supersale, "Seasonal Discounts", "Limited-time offers during holidays or off-peak times.", "01.12.24"));
        offerList.add(new Offer(R.drawable.ic_shoice, "Loyalty Rewards", "Reduced rates for frequent users.", "01.12.24"));
        offerList.add(new Offer(R.drawable.ic_specialsale, "Parking at the airport", "Are you going on vacation? Don't worry about traffic and park your car here.", "28.05.24"));
        offerList.add(new Offer(R.drawable.ic_hotdeal, "Valet Parking", "Offers discounted valet parking services for app users.", "01.01.24"));
        offerList.add(new Offer(R.drawable.ic_50sale, "First-Time User Discounts", "Special offers for users making their payments through the app.", "01.01.24"));
        offerList.add(new Offer(R.drawable.ic_shoice, "New parking in city center!", "Do you need to park your car safely?", "01.01.24"));

        adapter = new OfferAdapter(getContext(), offerList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAdminOffers();

        Button btnHistory = view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReplacerActivity.class);
            intent.putExtra("fragmentToLoad", "history");
            startActivity(intent);
        });

        return view;
    }

    private void loadAdminOffers() {
        db.collection("Users").document(ADMIN_UID).collection("Offers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null && !snapshot.isEmpty()) {
                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                String title = doc.getString("title");
                                String description = doc.getString("description");
                                String expiryDate = doc.getString("date");

                                // Default image if not provided
                                int imageRes = R.drawable.ic_hotdeal;

                                Offer offer = new Offer(imageRes, title, description, expiryDate);
                                offerList.add(offer);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to load admin offers", Toast.LENGTH_SHORT).show();
                        Log.e("HomeFragment", "Error getting documents: ", task.getException());
                    }
                });
    }
}
