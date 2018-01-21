package com.kortain.klearn.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kortain.klearn.Klearn;
import com.kortain.klearn.MainActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.NetworkUtility;
import com.kortain.klearn.Utility.ScreenUtility;
import com.kortain.klearn.adapters.NewsFeedAdapter;
import com.kortain.klearn.widgets.BottomNavigation;
import com.kortain.klearn.widgets.ToggleButton;
import com.leocardz.link.preview.library.TextCrawler;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    private TextView mFrameTitle;
    private ToggleButton mToggleSwitch;
    private MainActivity mActivity;

    private Query mQuery;
    private ListenerRegistration mRegistration;
    private List<DocumentSnapshot> mNewsFeeds = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private NewsFeedAdapter mFeedAdapter;

    public NewsFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        mRecyclerView = view.findViewById(R.id.news_feed_recycler_view);
        bindNewsFeedAdapter();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
        mFrameTitle = mActivity.findViewById(R.id.ah_frame_title);
        mToggleSwitch = mActivity.findViewById(R.id.ah_toggle_switch);
        if (!NetworkUtility.hasNetworkAccess(getContext())) {
            showNoNetworkSnack();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrameTitle.setText(R.string.news_feed);
        mToggleSwitch.setSwitchElements(
                mActivity.getResources().getStringArray(R.array.news_feeds_toggle_array),
                new boolean[]{false, true, false});

        AppBarLayout appBarLayout = mActivity.findViewById(R.id.ah_appbar);
        if (appBarLayout.getVisibility() == View.INVISIBLE) {
            appBarLayout.setVisibility(View.VISIBLE);
        }

        mQuery = FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTION_FEEDS)
                .orderBy(Constants.FEED_TIMESTAMP, Query.Direction.DESCENDING)
                .limit(25);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRegistration = mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                if (e == null) {
                    if (!snapshot.isEmpty()) {
                        mNewsFeeds = snapshot.getDocuments();
                        mFeedAdapter.setmDataItems(mNewsFeeds);
                        mFeedAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void bindNewsFeedAdapter() {
        mFeedAdapter = new NewsFeedAdapter(mActivity.getApplicationContext(), mNewsFeeds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
        mRecyclerView.setAdapter(mFeedAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mRegistration != null) {
            mRegistration.remove();
        }
    }

    /**
     * show No Network Snackbar
     */
    private void showNoNetworkSnack() {
        final BottomNavigation nav = mActivity.findViewById(R.id.ah_bottom_navigation);
        nav.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(R.id.ah_root), R.string.no_connection, Snackbar.LENGTH_SHORT);
        snackbar.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nav.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }
}
