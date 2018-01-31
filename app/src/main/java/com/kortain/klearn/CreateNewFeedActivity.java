package com.kortain.klearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kortain.klearn.fragments.ChooseFeedType;
import com.kortain.klearn.fragments.InputFeedTitle;

public class CreateNewFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_feed);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.anf_frame_layout, new ChooseFeedType())
                .commit();

    }
}
