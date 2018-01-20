package com.kortain.klearn;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.ScreenUtility;
import com.kortain.klearn.fragments.FavouritesFragment;
import com.kortain.klearn.fragments.MenuFragment;
import com.kortain.klearn.fragments.NewsFeedFragment;
import com.kortain.klearn.widgets.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    public static ScreenUtility SCREEN_UTILITY;
    private BottomNavigation mBottomNavigation;
    private BottomNavigation.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigation.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home: {
                    initFragment(new NewsFeedFragment());
                    break;
                }

                case R.id.navigation_favourites: {
                    initFragment(new FavouritesFragment());
                    break;
                }

                case R.id.navigation_menu: {
                    initFragment(new MenuFragment());
                    break;
                }
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
        SCREEN_UTILITY = ScreenUtility.getInstance(this);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    private void initActivity() {
        setContentView(R.layout.activity_main);
        ApplicationUtility.getInstance(this).setStatusBarBackground(this, R.color.colorPrimaryTransparent);
        mBottomNavigation = findViewById(R.id.ah_bottom_navigation);
    }

    private void initFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ah_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
