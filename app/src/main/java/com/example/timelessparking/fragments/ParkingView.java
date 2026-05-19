package com.example.timelessparking.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timelessparking.R;
import com.example.timelessparking.adapter.ParkingSlotAdapter;
import com.example.timelessparking.model.ParkingSlot;
import com.example.timelessparking.utils.SlotStatusManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ParkingView extends Fragment {

    private RecyclerView recyclerView;
    private ParkingSlotAdapter adapter;
    private List<ParkingSlot> slotList = new ArrayList<>();

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSlots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ParkingSlotAdapter(slotList);
        recyclerView.setAdapter(adapter);
        SlotStatusManager.registerAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchSlotData();

        return view;
    }

    private void fetchSlotData() {
        String adminUserId = "Gi3xgfOfYRW2BN9BBv0qG3pH1c03"; // Replace with actual user UID

        db.collection("Users")
                .document(adminUserId)
                .collection("parkingSlots")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("ParkingView", "Firestore error: ", e);
                        return;
                    }

                    if (snapshots != null) {
                        List<ParkingSlot> updatedList = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            String name = doc.getString("name");
                            Boolean available = doc.getBoolean("available");

                            if (name == null || available == null) continue;

                            String status = available ? "Available" : "Occupied";
                            updatedList.add(new ParkingSlot(name, status));
                        }

                        adapter.updateList(updatedList);
                    }
                });
    }
}
