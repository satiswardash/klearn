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

import com.kortain.klearn.CreateNewFeedActivity;
import com.kortain.klearn.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputArticleFeedUrl extends Fragment {


    private CreateNewFeedActivity mActivity;
    private ImageView mBackNavigation;
    private MaterialEditText mWebArticleAddressEditText;
    private Button mContinue;

    /**
     * Required empty public constructor
     */
    public InputArticleFeedUrl() { }

    /**
     *
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
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_article_feed_url, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBackNavigation = view.findViewById(R.id.af_web_nav_back);
        mWebArticleAddressEditText = view.findViewById(R.id.af_web_materialEditText);
        mContinue = view.findViewById(R.id.af_web_next_button);

        mBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        });

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mWebArticleAddressEditText.getText().toString().isEmpty()) {
                    mActivity.articleUrl = mWebArticleAddressEditText.getText().toString();
                    if (validate(mActivity.articleUrl)) {
                        //TODO
                        CreateFeedDialog dialog = new CreateFeedDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("message", "Would you like to add some description to your article?");
                        bundle.putString("positive", "yes");
                        bundle.putString("negative", "no");
                        dialog.setArguments(bundle);
                        dialog.setCancelable(true);
                        dialog.show(getFragmentManager(), CreateFeedDialog.class.toString());
                    } else
                        Toast.makeText(getContext(), "The article URL seems to be invalid!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     *
     * @param articleUrl
     * @return
     */
    private boolean validate(String articleUrl) {
        return true;
    }
}
