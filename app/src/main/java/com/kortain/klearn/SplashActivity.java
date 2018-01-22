package com.kortain.klearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kortain.klearn.Utility.NetworkUtility;

public class SplashActivity extends AppCompatActivity implements
        OnCompleteListener<Void>,
        OnFailureListener {

    private static final String TAG = SplashActivity.class.toString();

    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * calling managedQuery(android.net.Uri, String[], String, String[], String)
     * to retrieve cursors for data being displayed, etc.
     * <p>
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user.
     * <p>
     * Check for network access using {@link NetworkUtility}
     * If there is network access then reload the current user's auth instance and check for {@link FirebaseUser} object
     * if {@link FirebaseUser} != null then check whether the user's email id is verified or not
     * <p>
     * If there is no network fetch the local saved instance of {@link FirebaseUser} object
     * If {@link FirebaseUser} != null then redirect to {@link MainActivity} else redirect to {@link WelcomeActivity}
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (NetworkUtility.hasNetworkAccess(this)) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(this).addOnFailureListener(this);
            } else {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finishAffinity();
                return;
            }
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                finishAffinity();
                return;
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }

    /**
     * Firebase authentication callback for reloading current user
     *
     * @param task
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Intent intent;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                if (user.isEmailVerified()) {
                    intent = new Intent(this, MainActivity.class);
                } else {
                    intent = new Intent(this, EmailNotVerifiedActivity.class);
                }
            } else {
                intent = new Intent(this, WelcomeActivity.class);
            }
            startActivity(intent);
            finishAffinity();
        } else {
            //TODO Navigate the user to Error Activity and pass the exception message

        }
    }

    /**
     * Firebase authentication exception callback
     * @param e
     */
    @Override
    public void onFailure(@NonNull Exception e) {
        //TODO Navigate the user to Error Activity and pass the exception message
        Log.e(TAG, "onFailure: "+e.getMessage());
        finish();
    }
}
