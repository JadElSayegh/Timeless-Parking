package com.example.timelessparking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timelessparking.R;
import com.example.timelessparking.ReplacerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Fragment {

    private TextView tvProfileName, tvProfileEmail;
    private TextView paymentSettingsButton, helpButton, vehicleSettingsButton, logoutButton;
    private ImageView editProfileButton, ivProfileImage;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        editProfileButton = view.findViewById(R.id.ivEditProfile);
        paymentSettingsButton = view.findViewById(R.id.layoutPaymentSettings);
        helpButton = view.findViewById(R.id.layoutHelp);
        vehicleSettingsButton = view.findViewById(R.id.layoutVehicleSettings);
        logoutButton = view.findViewById(R.id.layoutLogout);

        mAuth = FirebaseAuth.getInstance();

        editProfileButton.setOnClickListener(v -> navigateToEditProfile());
        paymentSettingsButton.setOnClickListener(v -> navigateToPaymentSettings());
        helpButton.setOnClickListener(v -> navigateToHelp());
        vehicleSettingsButton.setOnClickListener(v -> navigateToVehicleSettings());
        logoutButton.setOnClickListener(v -> logout());

        loadUserData();

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("Users").document(uid);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");

                tvProfileName.setText((name != null && !name.isEmpty()) ? name : "No name set");
                tvProfileEmail.setText((email != null && !email.isEmpty()) ? email : "No email set");

            } else {
                Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(getActivity(), ReplacerActivity.class);
        intent.putExtra("fragmentToLoad", "editProfile");
        startActivity(intent);
    }

    private void navigateToPaymentSettings() {
        Intent intent = new Intent(getActivity(), ReplacerActivity.class);
        intent.putExtra("fragmentToLoad", "paymentSettings");
        startActivity(intent);
    }

    private void navigateToHelp() {
        Intent intent = new Intent(getContext(), ReplacerActivity.class);
        intent.putExtra("fragmentToLoad", "help");
        startActivity(intent);
    }

    private void navigateToVehicleSettings() {
        Intent intent = new Intent(getContext(), ReplacerActivity.class);
        intent.putExtra("fragmentToLoad", "vehicleSettings");
        startActivity(intent);
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), ReplacerActivity.class);
        intent.putExtra("fragmentToLoad", "login");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {
             if (requestCode == 1) {
                String updatedUsername = data.getStringExtra("updatedUsername");
                if (updatedUsername != null) {
                    tvProfileName.setText(updatedUsername);
                }
                loadUserData();
            }
        }
    }
}
