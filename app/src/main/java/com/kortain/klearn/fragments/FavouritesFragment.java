package com.kortain.klearn.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kortain.klearn.MainActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.widgets.ToggleButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    private TextView mFrameTitle;
    private ToggleButton mToggleSwitch;
    private MainActivity mActivity;

    public FavouritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
        mFrameTitle = mActivity.findViewById(R.id.ah_frame_title);
        mToggleSwitch = mActivity.findViewById(R.id.ah_toggle_switch);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameTitle.setText(R.string.fav);
        mToggleSwitch.setSwitchElements(
                mActivity.getResources().getStringArray(R.array.favourites_toggle_array),
                new boolean[]{false, true, false});

        AppBarLayout appBarLayout = getActivity().findViewById(R.id.ah_appbar);
        if (appBarLayout.getVisibility() == View.INVISIBLE) {
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }
}
