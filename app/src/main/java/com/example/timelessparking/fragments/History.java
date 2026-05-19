package com.example.timelessparking.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.timelessparking.R;
import com.example.timelessparking.adapter.ReceiptAdapter;
import com.example.timelessparking.model.Receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {

    private RecyclerView recyclerView;
    private ReceiptAdapter adapter;
    private List<Receipt> receiptList;

    public History() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.receiptsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        receiptList = new ArrayList<>();
        adapter = new ReceiptAdapter(receiptList);
        recyclerView.setAdapter(adapter);

        loadReceipts();

        return view;
    }

    private void loadReceipts() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid)
                .collection("receipts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    receiptList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Receipt receipt = doc.toObject(Receipt.class);
                        receiptList.add(receipt);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching receipts", e));

    }
}
