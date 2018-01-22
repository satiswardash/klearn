package com.kortain.klearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.NetworkUtility;
import com.kortain.klearn.Utility.StringUtility;
import com.kortain.klearn.fragments.NoNetworkAlertDialog;

public class ForgetPasswordActivity extends AppCompatActivity implements
        NoNetworkAlertDialog.NoNetworkDialogListeners,
        OnCompleteListener<Void>,
        OnFailureListener {

    private EditText mEmailId;
    private DialogFragment dialogFragment;
    private Button mForgotPasswordButton;
    private ProgressBar mProgressBar;

    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * calling managedQuery(android.net.Uri, String[], String, String[], String)
     * to retrieve cursors for data being displayed, etc.
     * <p>
     * Here we are getting the FirebaseAuth instance for future login actions
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
    }

    /**
     * Initialize the activity layout
     * mEmail,
     * mForgotPasswordButton,
     * mProgressBar
     * <p>
     * Set the actionbar visibility property to SYSTEM_UI_FLAG_FULLSCREEN
     * Set the default text for mRegister text view using HTML runtime text formatting
     */
    private void initLayout() {
        setContentView(R.layout.activity_forget_password);
        ApplicationUtility.getInstance(getApplicationContext())
                .setStatusBarVisibility(this, View.INVISIBLE);

        mEmailId = findViewById(R.id.afp_email_editText);
        mForgotPasswordButton = findViewById(R.id.afp_reset_button);
        mProgressBar = findViewById(R.id.afp_progressBar);
    }

    /**
     * Forgot password button on click handler
     * Uses firebase authentication API to send the password reset instructions,
     * to user's registered mail Id from mEmailId EditText field
     *
     * @param view
     */
    public void forgotPassword(View view) {
        String email = mEmailId.getText().toString();

        //Validate user's email Id first using StringUtility
        if (!StringUtility.getInstance(this).isValidEmail(email)) {
            Toast.makeText(this, R.string.invalid_mail_id, Toast.LENGTH_SHORT).show();
            return;
        }

        //Check for the network availability using NetworkUtility, if not available then show No Network Dialog
        if (NetworkUtility.hasNetworkAccess(this)) {
            setProgressBarVisibility(View.VISIBLE);
            setResetButtonState(false);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this)
                    .addOnFailureListener(this);
        } else {
            showNoNetworkDialog();
        }
    }

    /**
     * Firebase authentication callback on completion of task
     *
     * @param task
     */
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        setProgressBarVisibility(View.INVISIBLE);
        setResetButtonState(true);
        if (task.isSuccessful()) {
            Intent resetPasswordSuccessIntent =
                    new Intent(getApplicationContext(), ResetPasswordSuccessActivity.class);
            startActivity(resetPasswordSuccessIntent);
        } else {
            Toast.makeText(this, R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Firebase authentication exception callback
     *
     * @param e
     */
    @Override
    public void onFailure(@NonNull Exception e) {
        setProgressBarVisibility(View.INVISIBLE);
        setResetButtonState(true);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Retry callback from NoNetworkDialog fragment
     *
     * @param view
     */
    @Override
    public void retry(View view) {
        forgotPassword(null);
    }

    /**
     * On back pressed event handler
     * Navigate to back stack activity
     *
     * @param view
     */
    public void onBackPressed(View view) {
        onBackPressed();
    }

    /**
     * Set login progress bar visibility property before and after user presses the login button
     *
     * @param visibility
     */
    private void setProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    /**
     * Set login button enabled property before and after user presses the login button
     *
     * @param activated
     */
    private void setResetButtonState(boolean activated) {
        mForgotPasswordButton.setEnabled(activated);
        if (!activated) {
            mForgotPasswordButton.setTextColor(getResources().getColor(R.color.colorBackgroundPrimary));
        } else {
            mForgotPasswordButton.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    /**
     * Inflate No Network dialog when user clicks on login button with out network connectivity
     */
    private void showNoNetworkDialog() {
        dialogFragment = new NoNetworkAlertDialog();
        dialogFragment.setCancelable(true);
        dialogFragment.show(getSupportFragmentManager(), "FORGOT_PASSWORD_NO_NETWORK_DIALOG");

    }
}
