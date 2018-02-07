package com.kortain.klearn.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kortain.klearn.CreateNewFeedActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputFeedCategory extends Fragment {

    private static final String[] DATA = new String[]{
            "History", "General Knowledge", "Current Affairs", "India", "Politics", "Science and Technology"
    };

    private CreateNewFeedActivity mActivity;
    private MaterialAutoCompleteTextView mFeedCategoryEditText;
    private Button mContinueButton;
    private ArrayAdapter<String> mSuggestionAdapter;
    private View.OnClickListener mListener;
    private ImageView mBackNavigation;

    public InputFeedCategory() {
        // Required empty public constructor
    }

    /**
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewFeedActivity) {
            mActivity = (CreateNewFeedActivity) context;
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Suggestions list to be fetched from Network
        mSuggestionAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, DATA);
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_feed_category, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFeedCategoryEditText = view.findViewById(R.id.category_materialEditText);
        mContinueButton = view.findViewById(R.id.cf_category_next_button);
        mFeedCategoryEditText.setAdapter(mSuggestionAdapter);
        mListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContinueButtonPressed();
            }
        };
        mContinueButton.setOnClickListener(mListener);
        mBackNavigation = view.findViewById(R.id.category_nav_back);
        mBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }

    /**
     *
     */
    private void onContinueButtonPressed() {
        if (!mFeedCategoryEditText.getText().toString().isEmpty()) {
            mActivity.feedCategory = mFeedCategoryEditText.getText().toString();
            if (mActivity.selectedFeedType.equals(Constants.FEED_CATEGORY_OBJECTIVE)) {
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.anf_frame_layout, new TextEditor())
                        .addToBackStack(TextEditor.class.toString())
                        .commit();
            } else {
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.anf_frame_layout, new InputFeedTitle())
                        .addToBackStack(InputFeedTitle.class.toString())
                        .commit();
            }
        } else {
            Toast.makeText(mActivity, "You should enter a category name before publishing your post!", Toast.LENGTH_SHORT).show();
        }
    }
}
