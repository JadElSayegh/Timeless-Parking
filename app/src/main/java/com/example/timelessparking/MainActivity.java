package com.example.timelessparking;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.timelessparking.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        addTabs();
    }

    private void init() {

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

    }

    private void addTabs() {

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_connection));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_user));


        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {

                    case 0:
//                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_fill);
                        tab.setIcon(R.drawable.ic_home_fill);
                        setToolbarTitle("Timeless Parking");
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        setToolbarTitle("Parking Owners");
                        break;

                    case 2:
                        tab.setIcon(R.drawable.ic_connection);
                        setToolbarTitle("Connection");
                        break;

                    case 3:
                        tab.setIcon(R.drawable.ic_user);
                        setToolbarTitle("Profile");
                        break;


                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home);
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        break;

                    case 2:
                        tab.setIcon(R.drawable.ic_connection);
                        break;

                   case 3:
                        tab.setIcon(R.drawable.ic_user);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {

                    case 0:
                        tab.setIcon(R.drawable.ic_home_fill);
                        setToolbarTitle("Timeless Parking");
                        break;

                    case 1:
                        tab.setIcon(R.drawable.ic_search);
                        setToolbarTitle("Parking Owners");
                        break;

                    case 2:
                        tab.setIcon(R.drawable.ic_connection);
                        setToolbarTitle("Connection");
                        break;

                    case 3:
                        tab.setIcon(R.drawable.ic_user);
                        setToolbarTitle("Profile");
                        break;
                }

            }
        });


    }
    private void setToolbarTitle(String title) {
        TextView toolbarTitle = findViewById(R.id.textView);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }
}