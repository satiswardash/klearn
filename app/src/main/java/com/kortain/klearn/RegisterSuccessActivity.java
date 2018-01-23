package com.kortain.klearn;

import android.content.Intent;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.PathInterpolator;

import com.eftimoff.androipathview.PathView;
import com.kortain.klearn.Utility.ApplicationUtility;

public class RegisterSuccessActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_register_success);
        ApplicationUtility.getInstance(getApplicationContext()).setStatusBarVisibility(this, View.INVISIBLE);
        play();
    }

    /**
     * Using custom path animation API {@link PathView} and using {@link AccelerateInterpolator}
     */
    private void play() {
        PathView pathView = findViewById(R.id.ars_success_logo);
        pathView.useNaturalColors();
        pathView.getPathAnimator()
                .delay(500)
                .duration(400)
                .interpolator(new AccelerateInterpolator())
                .start();
    }

    /**
     * Navigate to the {@link RegisterInterestsActivity}
     *
     * @param view
     */
    public void onContinue(View view) {
        Intent intent = new Intent(this, RegisterInterestsActivity.class);
        startActivity(intent);
    }
}
