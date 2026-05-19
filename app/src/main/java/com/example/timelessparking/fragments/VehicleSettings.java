package com.example.timelessparking.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.timelessparking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class VehicleSettings extends Fragment {

    private EditText licensePlate, codeRFID;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_settings, container, false);

        licensePlate = view.findViewById(R.id.licensePlate);
        codeRFID = view.findViewById(R.id.codeRFID);
        saveButton = view.findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        saveButton.setOnClickListener(v -> saveVehicleData());

        return view;
    }

    private void saveVehicleData() {
        String licenseNumber = licensePlate.getText().toString().trim();
        String rfidCode = codeRFID.getText().toString().trim();

        if (TextUtils.isEmpty(licenseNumber) || TextUtils.isEmpty(rfidCode)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection("Users").document(uid);

        Map<String, Object> vehicleData = new HashMap<>();
        vehicleData.put("licensePlate", licenseNumber);
        vehicleData.put("rfidCode", rfidCode);

        userRef.set(vehicleData, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Vehicle data saved successfully!", Toast.LENGTH_SHORT).show();
                    licensePlate.setText("");
                    codeRFID.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
