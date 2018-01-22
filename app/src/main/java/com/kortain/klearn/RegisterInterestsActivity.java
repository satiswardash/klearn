package com.kortain.klearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kortain.klearn.Utility.ApplicationUtility;
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.LabelToggle;

public class RegisterInterestsActivity extends AppCompatActivity {

    private MultiSelectToggleGroup multiSelectToggleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interests);

        ApplicationUtility.getInstance(getApplicationContext()).setStatusBarVisibility(this, View.INVISIBLE);

        multiSelectToggleGroup = findViewById(R.id.ari_toggle_group);
        populateUserInterestsToggles();
    }

    private void populateUserInterestsToggles() {
        String[] interests;interests = getResources().getStringArray(R.array.interests_array);
        for (String s :
                interests) {

            LabelToggle toggle = new LabelToggle(getApplicationContext());
            toggle.setText(s);
            toggle.setTextSize(11);
            toggle.setTextColor(getResources().getColor(R.color.colorBlackPrimary));
            toggle.setBackground(getResources().getDrawable(R.drawable.bg_toggle_backgroundprimary_solid));
            toggle.setMarkerColor(getResources().getColor(R.color.colorSecondaryDark));
            multiSelectToggleGroup.addView(toggle);
        }
    }

    public void skipToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
