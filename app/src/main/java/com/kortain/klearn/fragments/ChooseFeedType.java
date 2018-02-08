package com.kortain.klearn.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kortain.klearn.MessageActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.widgets.Spinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFeedType extends Fragment {

    //Default available feed types that user can select before posting a new feed
    private MessageActivity mActivity;
    private Spinner mFeedTypeSpinner;
    private ImageView mNavBack;
    private Button mContinueButton;
    private AdapterView.OnItemSelectedListener mFeedTypeSelectionListener;

    public ChooseFeedType() {
    } // Required empty public constructor

    /**
     * Called when a fragment is first attached to its context.
     * <p>
     * Here we are initializing the view components attached with the host activity and
     * This is the method where you can save the Host Activity instance for the first time before the fragment inflates the layout
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MessageActivity) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional,
     * and non-graphical fragments can return null (which is the default implementation).
     * This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
     * <p>
     * Here we are inflating the layout corresponds to {@link ChooseFeedType} layout and initializing its view components
     * Bind the Spinner menu items.
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
        View view = inflater.inflate(R.layout.fragment_choose_feed_type, container, false);
        initFragmentLayout(view);
        bindSpinnerMenuItems();
        return view;
    }

    /**
     * Called after onCreate(Bundle) — or after onRestart() when the activity had been stopped,
     * but is now again being displayed to the user.
     * <p>
     * Here we are simply attaching the {@link android.widget.AdapterView.OnItemSelectedListener}
     * to our {@link Spinner} object.
     */
    @Override
    public void onStart() {
        super.onStart();

        /**
         * {@link android.widget.AdapterView.OnItemSelectedListener} for {@link Spinner} view
         * Checks the index of selected item and navigate to corresponding fragments.
         */
        mFeedTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                handleSpinnerSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mActivity.selectedFeedType.isEmpty()) {
                    mActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.anf_frame_layout, new InputFeedCategory())
                            .addToBackStack(InputFeedCategory.class.toString())
                            .commit();
                } else {
                    Toast.makeText(mActivity, "You should select one feed type!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mFeedTypeSelectionListener = null;
    }

    /**
     * Initialize the {@link ChooseFeedType} layout view components
     *
     * @param view
     */
    private void initFragmentLayout(View view) {
        mFeedTypeSpinner = view.findViewById(R.id.cf_feed_type_spinner);
        mNavBack = view.findViewById(R.id.cf_nav_back);
        mContinueButton = view.findViewById(R.id.cf_continue_button);
    }

    /**
     * Checks the index of selected item and navigate to corresponding fragments.
     *
     * @param position
     */
    private void handleSpinnerSelection(int position) {
        switch (position) {

            case 0: {
                mActivity.selectedFeedType = Constants.FEED_CATEGORY_REGULAR;
                break;
            }
            case 1: {
                mActivity.selectedFeedType = Constants.FEED_CATEGORY_IMAGE;
                break;
            }
            case 2: {
                mActivity.selectedFeedType = Constants.FEED_CATEGORY_WEB;
                break;
            }
            case 3: {
                mActivity.selectedFeedType = Constants.FEED_CATEGORY_OBJECTIVE;
                break;
            }
        }
    }

    /**
     * Bind the values of FEED_TYPES array to spinner menu items.
     */
    private void bindSpinnerMenuItems() {
        List<String> menuItems = new LinkedList<>(Arrays.asList(mActivity.FEED_TYPES));
        mFeedTypeSpinner.attachDataSource(menuItems);
    }
}
