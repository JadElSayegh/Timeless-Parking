package com.example.timelessparking;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.timelessparking.fragments.ContactUs;
import com.example.timelessparking.fragments.CreateAccountFragment;
import com.example.timelessparking.fragments.EditProfile;
import com.example.timelessparking.fragments.Feedback;
import com.example.timelessparking.fragments.Help;
import com.example.timelessparking.fragments.History;
import com.example.timelessparking.fragments.LoginFragment;
import com.example.timelessparking.fragments.ParkingView;
import com.example.timelessparking.fragments.PaymentSettings;
import com.example.timelessparking.fragments.Terms;
import com.example.timelessparking.fragments.VehicleSettings;

public class ReplacerActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacer);

        frameLayout = findViewById(R.id.frameLayout);

        String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");

        if ("login".equals(fragmentToLoad)) {
            setFragment(new LoginFragment());
        } else if ("createAccount".equals(fragmentToLoad)) {
            setFragment(new CreateAccountFragment());
        } else if ("editProfile".equals(fragmentToLoad)) {
            setFragment(new EditProfile());
        } else if ("vehicleSettings".equals(fragmentToLoad)) {
            setFragment(new VehicleSettings());
        } else if ("help".equals(fragmentToLoad)) {
            setFragment(new Help());
        } else if ("contactus".equals(fragmentToLoad)) {
            setFragment(new ContactUs());
        } else if ("feedback".equals(fragmentToLoad)) {
            setFragment(new Feedback());
        } else if ("terms".equals(fragmentToLoad)) {
            setFragment(new Terms());
        } else if ("paymentSettings".equals(fragmentToLoad)) {
            setFragment(new PaymentSettings());
        }else if ("history".equals(fragmentToLoad)) {
            setFragment(new History());
        } else if ("parkingview".equals(fragmentToLoad)) {
            setFragment(new ParkingView());
        } else {
            setFragment(new LoginFragment()); // fallback to LoginFragment if null or unknown
        }
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
