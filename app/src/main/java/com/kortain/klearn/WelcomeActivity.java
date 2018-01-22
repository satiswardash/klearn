package com.kortain.klearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kortain.klearn.Utility.ApplicationUtility;

public class WelcomeActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * calling managedQuery(android.net.Uri, String[], String, String[], String)
     * to retrieve cursors for data being displayed, etc.
     * <p>
     * Here we are initializing the layout view components and Set the actionbar visibility property to SYSTEM_UI_FLAG_FULLSCREEN
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ApplicationUtility.getInstance(getApplicationContext()).setStatusBarVisibility(this, View.INVISIBLE);
    }

    /**
     * Navigate to {@link LoginActivity}
     *
     * @param view
     */
    public void onLoginClicked(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    /**
     * Navigate to {@link RegisterActivity}
     *
     * @param view
     */
    public void onRegisterClicked(View view) {
        Intent loginIntent = new Intent(this, RegisterActivity.class);
        startActivity(loginIntent);
    }
}
