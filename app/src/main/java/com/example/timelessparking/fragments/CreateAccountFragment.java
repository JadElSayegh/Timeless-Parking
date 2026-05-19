package com.example.timelessparking.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timelessparking.MainActivity;
import com.example.timelessparking.R;
import com.example.timelessparking.ReplacerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccountFragment extends Fragment {

    public static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private EditText nameEt, emailEt, passwordEt, confirmPasswordEt;
    private ProgressBar progressBar;
    private TextView loginTv;
    private Button signUpBtn;
    private FirebaseAuth auth;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        clickListener();

    }

    private void init(View view) {

        nameEt = view.findViewById(R.id.nameET);
        emailEt = view.findViewById(R.id.emailET);
        passwordEt = view.findViewById(R.id.passwordET);
        confirmPasswordEt = view.findViewById(R.id.confirmPassET);
        loginTv = view.findViewById(R.id.loginTV);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        progressBar = view.findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

    }

    private void clickListener() {

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReplacerActivity) getActivity()).setFragment(new LoginFragment());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getText().toString();
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirmPassword = confirmPasswordEt.getText().toString();

                if (name.isEmpty() || name.equals(" ")) {
                    nameEt.setError("Please input valid name");
                    return;
                }

                if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                    emailEt.setError("Please input valid email");
                    return;
                }

                if (password.isEmpty() || password.length() < 6) {
                    passwordEt.setError("Please input valid password");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    confirmPasswordEt.setError("Password not match");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                createAccount(name, email, password);


            }
        });

    }

    private void createAccount(final String name, final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification().addOnCompleteListener(verifyTask -> {
                                if (verifyTask.isSuccessful()) {
                                    Toast.makeText(getContext(), "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show();
                                    uploadUserToFirestore(user, name, email);
                                } else {
                                    showError("Verification email could not be sent.");
                                }
                            });
                        } else {
                            showError("User creation failed.");
                        }
                    } else {
                        showError("Error: " + task.getException().getMessage());
                    }
                });
    }

    private void uploadUserToFirestore(FirebaseUser user, String name, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("profileImage", " ");
        userMap.put("uid", user.getUid());

        FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid())
                .set(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Account created successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        requireActivity().finish();
                    } else {
                        showError("Firestore error: " + task.getException().getMessage());
                    }
                });
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}