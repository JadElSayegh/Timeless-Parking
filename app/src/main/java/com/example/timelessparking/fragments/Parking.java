package com.example.timelessparking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.timelessparking.R;
import com.example.timelessparking.ReplacerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Parking extends Fragment {

    private TextView nameTextView, phoneTextView;
    private final String adminUid = "Gi3xgfOfYRW2BN9BBv0qG3pH1c03";
    private View adminInfoLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        adminInfoLayout = view.findViewById(R.id.adminInfoLayout);

        loadAdminInfo();

        adminInfoLayout.setOnClickListener(v -> navigateToParkingView());

        return view;
    }

    private void navigateToParkingView() {
        Intent intent = new Intent(getActivity(), ReplacerActivity.class);
        intent.putExtra("fragmentToLoad", "parkingview");
        startActivity(intent);
    }

    private void loadAdminInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference adminRef = db.collection("Users").document(adminUid);

        adminRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String phone = documentSnapshot.getString("phone");  // changed here

                nameTextView.setText(name);
                phoneTextView.setText(phone != null ? phone : "No phone provided");

            } else {
                Toast.makeText(getContext(), "Admin user not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Failed to load admin data", Toast.LENGTH_SHORT).show()
        );
    }
}
