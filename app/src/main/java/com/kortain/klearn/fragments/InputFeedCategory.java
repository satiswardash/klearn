package com.kortain.klearn.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kortain.klearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputFeedCategory extends Fragment {


    public InputFeedCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_feed_category, container, false);
    }

}
