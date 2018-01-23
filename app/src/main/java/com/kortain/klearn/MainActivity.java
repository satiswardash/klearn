package com.kortain.klearn;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.NetworkUtility;
import com.kortain.klearn.Utility.ScreenUtility;
import com.kortain.klearn.fragments.FavouritesFragment;
import com.kortain.klearn.fragments.MenuFragment;
import com.kortain.klearn.fragments.NewsFeedFragment;
import com.kortain.klearn.widgets.BottomNavigation;

public class MainActivity extends AppCompatActivity implements OnCompleteListener<Void> {

    public static ScreenUtility SCREEN_UTILITY;
    private FirebaseUser mUser;
    private BottomNavigation mBottomNavigation;
    private FloatingActionButton fab;

    private ListenerRegistration mRegistration;

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

    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * calling managedQuery(android.net.Uri, String[], String, String[], String)
     * to retrieve cursors for data being displayed, etc.
     * <p>
     * Here we are initializing the layout view components and
     * setting {@link android.support.design.widget.NavigationView.OnNavigationItemSelectedListener} of {@link BottomNavigation}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user.
     * <p>
     * Here we are check whether the current user has verified his/her Email Id or not
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (NetworkUtility.hasNetworkAccess(this)) {
            FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .reload()
                    .addOnCompleteListener(this);
        }
        initFab();
    }

    /**
     * Firebase authentication callback after reloading the current firebase auth user instance
     *
     * @param task
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            if (mUser != null && !mUser.isEmailVerified()) {
                //TODO Implement a fragment dialog for email verification
                showSnackBar("Your Email Id is not verified, please verify.");
            }
        }
    }

    /**
     * Initialize the activity layout
     * mBottomNavigation
     * <p>
     * Set the actionbar visibility property to SYSTEM_UI_FLAG_FULLSCREEN
     * Set the default fragment selection to {@link NewsFeedFragment}
     * Set the {@link ScreenUtility} instance to SCREEN_UTILITY constant
     */
    private void initActivity() {
        setContentView(R.layout.activity_main);
        SCREEN_UTILITY = ScreenUtility.getInstance(this);
        ApplicationUtility.getInstance(this).setStatusBarBackground(this, R.color.colorPrimaryTransparent);
        mBottomNavigation = findViewById(R.id.ah_bottom_navigation);
    }

    /**
     * {@link FloatingActionButton} onClick event handler
     * Navigates to CreateNewPost activity
     *
     * @param view
     */
    public void createNewPost(View view) {

    }

    /**
     * Check the user quota from Firestore users collection,
     * if Admin user then show the Create New Post fab button.
     */
    private void initFab() {
        mRegistration = FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_USERS)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                        if (e == null) {
                            if (snapshot.contains(Constants.USER_IS_ADMIN)) {
                                fab = findViewById(R.id.ah_create_post_fab);
                                if (snapshot.getBoolean(Constants.USER_IS_ADMIN)) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            fab.show();
                                        }
                                    }, 600);
                                } else {
                                    fab.hide();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Initialize selected fragment and load it to {@link android.widget.FrameLayout} of activity_main.xml
     *
     * @param fragment
     */
    private void initFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ah_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Show snackbar by hiding the {@link BottomNavigation}
     *
     * @param message
     */
    private void showSnackBar(String message) {
        final BottomNavigation nav = findViewById(R.id.ah_bottom_navigation);
        nav.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.ah_root), message, Snackbar.LENGTH_LONG);
        snackbar.setAction("RESEND", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUser.sendEmailVerification();
            }
        });
        snackbar.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nav.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /**
     * Deregister any available listeners when activity gets stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mRegistration != null) {
            mRegistration.remove();
        }
    }
}
