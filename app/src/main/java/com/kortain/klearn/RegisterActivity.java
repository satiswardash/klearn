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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.NetworkUtility;
import com.kortain.klearn.Utility.StringUtility;
import com.kortain.klearn.fragments.NoNetworkAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>, OnFailureListener, NoNetworkAlertDialog.NoNetworkDialogListeners {

    private FirebaseAuth mAuth;
    private EditText mEmailId;
    private EditText mPassword;
    private EditText mPhone;
    private ProgressBar mProgressBar;
    private TextView mFooterText;
    private TextView mLogin;
    private Button mRegisterButton;
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
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user.
     * <p>
     * Append the phone number country code at the beginning when user sets the focus
     */
    @Override
    protected void onStart() {
        super.onStart();
        mEmailId.setFocusable(true);
        //TODO
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhone.setText("+91");
            }
        });
    }

    /**
     * Initialize the activity layout
     * mEmail,
     * mPassword,
     * mProgressBar,
     * mRegisterButton,
     * mLogin,
     * mFooterText,
     * <p>
     * Set the actionbar visibility property to SYSTEM_UI_FLAG_FULLSCREEN
     * Set the default text for mRegister text view using HTML runtime text formatting
     */
    private void initLayout() {
        setContentView(R.layout.activity_register);
        ApplicationUtility.getInstance(getApplicationContext())
                .setStatusBarVisibility(this, View.INVISIBLE);
        mEmailId = findViewById(R.id.ar_email_editText);
        mPassword = findViewById(R.id.ar_password_editText);
        mPhone = findViewById(R.id.ar_phone_editText);
        mProgressBar = findViewById(R.id.ar_progressBar);
        mFooterText = findViewById(R.id.ar_view_terms);
        mLogin = findViewById(R.id.ar_login);
        mRegisterButton = findViewById(R.id.ar_register_button);

        mLogin.setText(Html.fromHtml(getString(R.string.nav_from_register_to_login)));
        mFooterText.setText(Html.fromHtml(getString(R.string.view_terms_conditions)));
    }

    /**
     * Register new user with email, password and phone event handler
     *
     * @param view
     */
    public void register(View view) {
        String email = mEmailId.getText().toString();
        String password = mPassword.getText().toString();
        String phone = mPhone.getText().toString();

        //Validate Email Id, Password and Phone
        if (!StringUtility.getInstance(this).isValidEmail(email)) {
            Toast.makeText(this, R.string.invalid_mail_id, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtility.getInstance(this).isValidPassword(password)) {
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtility.getInstance(this).isValidPhone(phone)) {
            Toast.makeText(this, R.string.invalid_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        //Check for the Network availability using NetworkUtility
        if (NetworkUtility.hasNetworkAccess(this)) {
            setRegisterButtonState(false);
            setProgressBarVisibility(View.VISIBLE);

            //Call Firebase API's signInWithEmailAndPassword method
            mAuth.createUserWithEmailAndPassword(email, password)
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
    public void onComplete(@NonNull final Task<AuthResult> task) {
        if (task.isSuccessful()) {
            final AuthResult result = task.getResult();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(Constants.COLLECTION_USERS)
                    .document(task.getResult().getUser().getUid())
                    .set(getUser(result), SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!result.getUser().isEmailVerified()) {
                                result.getUser().sendEmailVerification();
                            }
                            setRegisterButtonState(true);
                            setProgressBarVisibility(View.INVISIBLE);
                            startRegisterSuccessActivity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
        setRegisterButtonState(true);
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
        register(null);
    }

    /**
     * Start RegisterSuccessActivity using intent
     */
    private void startRegisterSuccessActivity() {
        Intent intent = new Intent(this, RegisterSuccessActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to Register New User activity
     *
     * @param view
     */
    public void onLoginClick(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Create User Map<String, Object> object from auth result
     *
     * @param result
     * @return
     */
    private Map<String, Object> getUser(AuthResult result) {
        Map<String, Object> user = new HashMap<>();
        user.put(Constants.USER_NAME, result.getUser().getDisplayName());
        user.put(Constants.USER_PHONE, mPhone.getText().toString());
        user.put(Constants.USER_EMAIL, result.getUser().getEmail());
        user.put(Constants.USER_POINTS, 0l);
        user.put(Constants.USER_PICTURE, new String(""));
        user.put(Constants.USER_QUOTA, false);
        user.put(Constants.USER_INTERESTS, new ArrayList<String>());
        user.put(Constants.USER_BOOKMARKS, new ArrayList<String>());
        user.put(Constants.USER_FAVOURITES, new ArrayList<String>());

        return user;
    }

    /**
     * Set register progress bar visibility property before and after user presses the login button
     *
     * @param visibility
     */
    private void setProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }

    /**
     * Set login button enabled property before and after user presses the register button
     *
     * @param activated
     */
    private void setRegisterButtonState(boolean activated) {
        mRegisterButton.setEnabled(activated);
        if (!activated) {
            mRegisterButton.setTextColor(getResources().getColor(R.color.colorBackgroundPrimary));
        } else {
            mRegisterButton.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    /**
     * Inflate No Network dialog when user clicks on login button with out network connectivity
     */
    private void showNoNetworkDialog() {
        dialogFragment = new NoNetworkAlertDialog();
        dialogFragment.setCancelable(true);
        dialogFragment.show(getSupportFragmentManager(), "REGISTER_NO_NETWORK_DIALOG");

    }
}
