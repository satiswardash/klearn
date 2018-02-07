package com.kortain.klearn.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kortain.klearn.MainActivity;
import com.kortain.klearn.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuHome extends Fragment {

    private TextView mFrameTitle;
    private MainActivity mActivity;

    public MenuHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
        mFrameTitle = mActivity.findViewById(R.id.ah_frame_title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameTitle.setText(R.string.menu);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.ah_appbar);
        if (appBarLayout.getVisibility() == View.VISIBLE) {
            appBarLayout.setVisibility(View.INVISIBLE);
        }
    }
}
