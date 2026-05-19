package com.example.timelessparking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timelessparking.R;
import com.example.timelessparking.ReplacerActivity;

public class Help extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);

        LinearLayout contactUsLayout = view.findViewById(R.id.contact_us_layout);
        LinearLayout feedbackLayout = view.findViewById(R.id.feedback_layout);
        ImageView backArrow = view.findViewById(R.id.back_arrow);

        contactUsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReplacerActivity.class);
            intent.putExtra("fragmentToLoad", "contactus");
            startActivity(intent);
        });

        feedbackLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ReplacerActivity.class);
            intent.putExtra("fragmentToLoad", "feedback");
            startActivity(intent);;
        });


        backArrow.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}
