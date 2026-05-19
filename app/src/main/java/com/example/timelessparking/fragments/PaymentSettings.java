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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaymentSettings extends Fragment {

    private EditText cardHolderName, cardNumber, expiryDate;
    private Button savePaymentButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public PaymentSettings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_settings, container, false);

        cardHolderName = view.findViewById(R.id.cardHolderName);
        cardNumber = view.findViewById(R.id.cardNumber);
        expiryDate = view.findViewById(R.id.expiryDate);
        savePaymentButton = view.findViewById(R.id.savePaymentButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        savePaymentButton.setOnClickListener(v -> savePaymentInfo());

        loadPaymentInfo();

        return view;
    }

    private void savePaymentInfo() {
        String name = cardHolderName.getText().toString().trim();
        String number = cardNumber.getText().toString().trim();
        String expiry = expiryDate.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(expiry)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Optional: Mask the card number before storing
        String maskedNumber = maskCardNumber(number);

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("cardHolderName", name);
        paymentData.put("cardNumber", maskedNumber);
        paymentData.put("expiryDate", expiry);

        db.collection("Users")
                .document(mAuth.getUid())
                .update("paymentInfo", paymentData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getContext(), "Payment info saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to save info", Toast.LENGTH_SHORT).show());
    }

    private void loadPaymentInfo() {
        db.collection("Users")
                .document(mAuth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> paymentInfo = (Map<String, Object>) documentSnapshot.get("paymentInfo");
                        if (paymentInfo != null) {
                            cardHolderName.setText((String) paymentInfo.get("cardHolderName"));
                            cardNumber.setText((String) paymentInfo.get("cardNumber"));
                            expiryDate.setText((String) paymentInfo.get("expiryDate"));
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to load payment info", Toast.LENGTH_SHORT).show());
    }

    private String maskCardNumber(String fullNumber) {
        if (fullNumber.length() >= 4) {
            return "**** **** **** " + fullNumber.substring(fullNumber.length() - 4);
        } else {
            return fullNumber;
        }
    }
}
