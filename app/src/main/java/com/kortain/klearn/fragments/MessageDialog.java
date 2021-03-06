package com.kortain.klearn.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.eftimoff.androipathview.PathView;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.NetworkUtility;

/**
 * Created by satiswardash on 06/02/18.
 */

public class MessageDialog extends DialogFragment {

    private static final String TAG = MessageDialog.class.toString();
    private TextView messageTextView;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private MessageDialogListeners mListener;

    /**
     *Called when a fragment is first attached to its context.
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
        try {
            mListener = (MessageDialogListeners) getActivity();
        } catch (ClassCastException ex) {
            Log.e(TAG, "onAttach: You must implement the MessageDialogListeners methods. \n", ex.getCause());
        }
    }

    /**
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_message, null);
        builder.setView(view);
        play((PathView) view.findViewById(R.id.anf_path_image_view));
        messageTextView = view.findViewById(R.id.anf_dialog_message_textView);
        mNegativeButton = view.findViewById(R.id.anf_dialog_negative_button);
        mPositiveButton = view.findViewById(R.id.anf_dialog_positive_button);

        Bundle bundle = getArguments();
        if (bundle.containsKey("message")) {
            messageTextView.setText(bundle.getString("message"));
        }
        if (bundle.containsKey("positive")) {
            mPositiveButton.setText(bundle.getString("positive"));
        }
        if (bundle.containsKey("negative")) {
            mNegativeButton.setText(bundle.getString("negative"));
        }

        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    dismiss();
                    mListener.onPositiveClickHandler();
                }
            }
        });

        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    dismiss();
                    mListener.onNegativeClickHandler();
                }
            }
        });
        return builder.create();
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     * Using custom path animation API {@link PathView} and using {@link AccelerateInterpolator}
     */
    private void play(PathView pathView) {
        pathView.setPathColor(Color.parseColor("#EDEDED"));
        pathView.setFillAfter(true);
        pathView.getPathAnimator()
                .delay(200)
                .duration(600)
                .interpolator(new AccelerateInterpolator())
                .start();
    }

    /**
     * Listeners callback method for host activity class
     */
    public interface MessageDialogListeners {
        void onPositiveClickHandler();

        void onNegativeClickHandler();
    }
}
