package com.kortain.klearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.eftimoff.androipathview.PathView;
import com.kortain.klearn.Utility.ApplicationUtility;

public class ResetPasswordSuccessActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_reset_password_success);
        ApplicationUtility.getInstance(getApplicationContext()).setStatusBarVisibility(this, View.INVISIBLE);
        play();
    }

    /**
     * Navigate to {@link LoginActivity} after successful password reset
     *
     * @param view
     */
    public void resetSuccessLogin(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Navigate to the activity from back stack
     *
     * @param view
     */
    public void onBackPressed(View view) {
        onBackPressed();
    }

    /**
     * Using custom path animation API {@link PathView} and using {@link AccelerateInterpolator}
     */
    private void play() {
        PathView pathView = findViewById(R.id.ars_success_logo);
        pathView.useNaturalColors();
        pathView.getPathAnimator()
                .delay(300)
                .duration(400)
                .interpolator(new AccelerateInterpolator())
                .start();
    }
}
