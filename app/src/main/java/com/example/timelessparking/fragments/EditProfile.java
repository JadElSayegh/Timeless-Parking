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

public class EditProfile extends Fragment {

    private EditText editUsername;
    private Button saveButton, backButton;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editUsername = view.findViewById(R.id.editUsername);
        saveButton = view.findViewById(R.id.btnSaveUsername);
        backButton = view.findViewById(R.id.btnBack);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        saveButton.setOnClickListener(v -> updateUsername());
        backButton.setOnClickListener(v -> requireActivity().finish()); // Closes ReplacerActivity

        return view;
    }

    private void updateUsername() {
        String newUsername = editUsername.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername)) {
            editUsername.setError("Username required");
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = firestore.collection("Users").document(uid); // Match Profile

        userRef.update("name", newUsername) // Match field used in Profile
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Username updated", Toast.LENGTH_SHORT).show();
                    requireActivity().finish(); // Closes ReplacerActivity
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
