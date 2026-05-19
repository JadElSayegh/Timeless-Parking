package com.example.timelessparking.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.timelessparking.fragments.Connection;
import com.example.timelessparking.fragments.Home;
import com.example.timelessparking.fragments.Profile;
import com.example.timelessparking.fragments.Parking;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    int noOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            default:
            case 0:
                return new Home();

            case 1:
                return new Parking();

            case 2:
                return new Connection();

            case 3:

                return new Profile();

        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
