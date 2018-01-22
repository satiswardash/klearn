package com.kortain.klearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kortain.klearn.Utility.ApplicationUtility;

public class EmailNotVerifiedActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_email_not_verified);
        ApplicationUtility.getInstance(getApplicationContext()).setStatusBarVisibility(this, View.INVISIBLE);
    }

    /**
     * Resend Email Id verification event handler
     * Get the current firebase auth user instance and if user's email id is not verified then  trigger the verification email.
     *
     * @param view
     */
    public void sendVerificationAgain(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (!firebaseUser.isEmailVerified()) {
            firebaseUser.sendEmailVerification();

            //Show the verification email sent confirmation acknowledgement message to the user
            TextView textView = findViewById(R.id.verification_confirmation_message);
            textView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * On back button pressed event handler, navigate to activity from back stack
     *
     * @param view
     */
    public void navBack(View view) {
        onBackPressed();
    }
}
