package com.kortain.klearn.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kortain.klearn.MessageActivity;
import com.kortain.klearn.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputFeedTitle extends Fragment {

    private MessageActivity mActivity;
    private Button mContinue;
    private MaterialEditText mTitleEditText;
    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onContinueButtonPressed();
        }
    };
    private ImageView mBackNavigation;

    public InputFeedTitle() {
        // Required empty public constructor
    }

    /**
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessageActivity) {
            mActivity = (MessageActivity) context;
        }
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
        return inflater.inflate(R.layout.fragment_input_feed_title, container, false);
    }

    /**
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mContinue = view.findViewById(R.id.cf_title_next_button);
        mTitleEditText = view.findViewById(R.id.cf_title_materialEditText);
        mContinue.setOnClickListener(mListener);
        mBackNavigation = view.findViewById(R.id.title_nav_back);
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
        if (!mTitleEditText.getText().toString().isEmpty()) {
            mActivity.feedTitle = mTitleEditText.getText().toString();

            switch (mActivity.selectedFeedType) {

                case "Regular Article": {
                    mActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.anf_frame_layout, new TextEditor(), TextEditor.class.toString())
                            .addToBackStack(TextEditor.class.toString())
                            .commit();
                    break;
                }

                case "Image Article": {
                    mActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.anf_frame_layout, new AttachFeedImages(), AttachFeedImages.class.toString())
                            .addToBackStack(AttachFeedImages.class.toString())
                            .commit();
                    break;
                }

                case "Web Article": {
                    mActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.anf_frame_layout, new InputArticleFeedUrl(), InputArticleFeedUrl.class.toString())
                            .addToBackStack(InputArticleFeedUrl.class.toString())
                            .commit();
                    break;
                }

            }

        } else {
            Toast.makeText(mActivity, "Post title cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
