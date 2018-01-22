package com.kortain.klearn.fragments;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kortain.klearn.MainActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.NetworkUtility;
import com.kortain.klearn.adapters.NewsFeedAdapter;
import com.kortain.klearn.services.NetworkStateService;
import com.kortain.klearn.widgets.BottomNavigation;
import com.kortain.klearn.widgets.ToggleButton;

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

    private JobScheduler mScheduler;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //showSnackBar(getString(R.string.back_online));
            bindNewsFeedAdapter();
            fetchQuerySnapshots();
        }
    };

    /**
     * Default constructor (Required)
     */
    public NewsFeedFragment() {
    }

    /**
     * Called when a fragment is first attached to its context.
     * <p>
     * Here we are initializing the view components attached with the host activity and
     * check for network availability using {@link NetworkUtility} before fetching feeds.
     * This is the method where you can save the Host Activity instance for the first time before the fragment inflates the layout
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
        mFrameTitle = mActivity.findViewById(R.id.ah_frame_title);
        mToggleSwitch = mActivity.findViewById(R.id.ah_toggle_switch);
        if (!NetworkUtility.hasNetworkAccess(getContext())) {
            showSnackBar(getResources().getString(R.string.no_connection));
        }
    }

    /**
     * The system calls this when creating the fragment.
     * Within your implementation, you should initialize essential components of the fragment
     * that you want to retain when the fragment is paused or stopped, then resumed.
     * <p>
     * Here we are initializing the {@link Query} object to fetch the feeds
     * and setting the fragment title and default value for {@link ToggleButton} switch (Today) attached with the Host Activity
     *
     * @param savedInstanceState
     */
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

    /**
     * Called to have the fragment instantiate its user interface view. This is optional,
     * and non-graphical fragments can return null (which is the default implementation).
     * This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
     * <p>
     * Here we are inflating the {@link NewsFeedFragment} layout and initializing its view components
     * Bind the news feed recycler view adapter with an empty {@link ArrayList<DocumentSnapshot>},
     * which will automatically load the incoming feeds.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        mRecyclerView = view.findViewById(R.id.news_feed_recycler_view);
        bindNewsFeedAdapter();
        return view;
    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user.
     * <p>
     * Here we are fetching the feeds from the Firestore using {@link Query} that we have initialized before
     * and setting the fetched {@link DocumentSnapshot} list to mNewsFeeds {@link ArrayList<DocumentSnapshot>}
     * <p>
     * Next we are registering our broadcast receiver and the job scheduler service to receive {@link NetworkStateService} message when Network state is changed/available
     * <p>
     * Here we are registering {@link EventListener<QuerySnapshot>} to our query so that we get notified each time the data changes and handle accordingly
     */
    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(mActivity)
                .registerReceiver(mReceiver, new IntentFilter(NetworkStateService.NETWORK_STATE_ACTION));

        scheduleNetworkStateChangeJob();
        fetchQuerySnapshots();
    }

    /**
     * Schedule {@link NetworkStateService} job for receiving NETWORK_STATE_CHANGE events.
     */
    private void scheduleNetworkStateChangeJob() {
        mScheduler = (JobScheduler) mActivity.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobInfo1 = new JobInfo.Builder(166665,
                new ComponentName(
                        mActivity.getPackageName(),
                        NetworkStateService.class.getName()));
        jobInfo1.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        mScheduler.schedule(jobInfo1.build());
    }

    /**
     * Fetch query results from Firestore and set it to mNewsFeeds
     */
    private void fetchQuerySnapshots() {
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

    /**
     * Bind the {@link NewsFeedAdapter} with mRecyclerView
     */
    private void bindNewsFeedAdapter() {
        mFeedAdapter = new NewsFeedAdapter(mActivity.getApplicationContext(), mNewsFeeds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
        mRecyclerView.setAdapter(mFeedAdapter);
    }

    /**
     * Called when the fragment is no longer attached to its activity.
     * <p>
     * Here we are un-registering any existing {@link ListenerRegistration} object
     */
    @Override
    public void onDetach() {
        super.onDetach();
        if (mRegistration != null) {
            mRegistration.remove();
        }

        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        mScheduler.cancelAll();
    }

    /**
     * show No Network Snackbar
     */
    private void showSnackBar(String s) {
        final BottomNavigation nav = mActivity.findViewById(R.id.ah_bottom_navigation);
        nav.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(R.id.ah_root), s, Snackbar.LENGTH_LONG);
        snackbar.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nav.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
}
