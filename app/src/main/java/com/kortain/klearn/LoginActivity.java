package com.kortain.klearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.NetworkUtility;
import com.kortain.klearn.Utility.StringUtility;
import com.kortain.klearn.fragments.NoNetworkAlertDialog;

public class LoginActivity extends AppCompatActivity implements
        OnCompleteListener<AuthResult>,
        OnFailureListener,
        NoNetworkAlertDialog.NoNetworkDialogListeners {

    private FirebaseAuth mAuth;
    private EditText mEmailId;
    private EditText mPassword;
    private ProgressBar mProgressBar;
    private Button mLoginButton;
    private TextView mRegister;
    private DialogFragment dialogFragment;

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
        mAuth = FirebaseAuth.getInstance();
        initLayout();
    }

    /**
     * Initialize the activity layout
     * mEmail,
     * mPassword,
     * mProgressBar,
     * mLoginButton,
     * mRegister
     * <p>
     * Set the actionbar visibility property to SYSTEM_UI_FLAG_FULLSCREEN
     * Set the default text for mRegister text view using HTML runtime text formatting
     */
    private void initLayout() {
        setContentView(R.layout.activity_login);
        ApplicationUtility.getInstance(getApplicationContext())
                .setStatusBarVisibility(this, View.INVISIBLE);

        mEmailId = findViewById(R.id.al_email_editText);
        mPassword = findViewById(R.id.al_password_editText);
        mProgressBar = findViewById(R.id.al_progressBar);
        mLoginButton = findViewById(R.id.al_login_button);
        mRegister = findViewById(R.id.al_register);
        mRegister.setText(Html.fromHtml(getString(R.string.nav_from_login_to_register)));
    }

    /**
     * Login existing user with email and password event handler
     *
     * @param view
     */
    public void login(View view) {
        String email = mEmailId.getText().toString();
        String password = mPassword.getText().toString();

        //Validate Email Id and Password
        if (!StringUtility.getInstance(this).isValidEmail(email)) {
            Toast.makeText(this, R.string.invalid_mail_id, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null || password.isEmpty()) {
            Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            return;
        }

        //Check for the Network availability using NetworkUtility
        if (NetworkUtility.hasNetworkAccess(this)) {
            setLoginButtonState(false);
            setProgressBarVisibility(View.VISIBLE);

            //Call Firebase API's signInWithEmailAndPassword method
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this)
                    .addOnFailureListener(this);
        } else {
            setLoginButtonState(true);
            setProgressBarVisibility(View.INVISIBLE);
            showNoNetworkDialog();
        }
    }

    /**
     * Firebase authentication callback on completion of task
     *
     * @param task
     */
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        setLoginButtonState(true);
        setProgressBarVisibility(View.INVISIBLE);
        if (task.isSuccessful()) {
            if (task.getResult().getUser().isEmailVerified()) {
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                finishAffinity();
            } else {
                Intent emailNotVerifiedIntent = new Intent(this, EmailNotVerifiedActivity.class);
                startActivity(emailNotVerifiedIntent);
            }
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
        setLoginButtonState(true);
        setProgressBarVisibility(View.INVISIBLE);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Retry callback from NoNetworkDialog fragment
     *
     * @param view
     */
    @Override
    public void retry(View view) {
        login(null);
    }

    /**
     * Reset password on click handler
     *
     * @param view
     */
    public void resetPassword(View view) {
        Intent resetPasswordIntent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(resetPasswordIntent);
    }

    /**
     * Navigate to Register New User activity
     *
     * @param view
     */
    public void onRegisterClick(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(registerIntent);
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
    private void setLoginButtonState(boolean activated) {
        mLoginButton.setEnabled(activated);
        if (!activated) {
            mLoginButton.setTextColor(getResources().getColor(R.color.colorBackgroundPrimary));
        } else {
            mLoginButton.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    /**
     * Inflate No Network dialog when user clicks on login button with out network connectivity
     */
    private void showNoNetworkDialog() {
        dialogFragment = new NoNetworkAlertDialog();
        dialogFragment.setCancelable(true);
        dialogFragment.show(getSupportFragmentManager(), "LOGIN_NO_NETWORK_DIALOG");

    }
}
