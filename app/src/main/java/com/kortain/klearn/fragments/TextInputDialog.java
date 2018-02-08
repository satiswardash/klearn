package com.kortain.klearn.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.kortain.klearn.R;
import com.kortain.klearn.Utility.ProgressLoaderUtility;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextInputDialog extends DialogFragment {

    private static final String TAG = TextInputDialog.class.toString();
    private EditText materialEditText;
    private ImageView mSubmit;
    private TextInputDialogListener mListener;

    public TextInputDialog() {
        // Required empty public constructor
    }

    /**
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (TextInputDialog.TextInputDialogListener) getActivity();
        } catch (ClassCastException ex) {
            Log.e(TAG, "onAttach: You must implement the MessageDialogListeners methods. \n", ex.getCause());
        }
    }

    /**
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
        View view = inflater.inflate(R.layout.dialog_text_input, null);
        builder.setView(view);

        final Dialog dialog = builder.create();

        materialEditText = view.findViewById(R.id.dti_info_edit_text);
        mSubmit = view.findViewById(R.id.dti_submit_button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    ProgressLoaderUtility.getInstance(R.id.anf_frame_layout, getFragmentManager()).setLoadingScreen(View.VISIBLE);
                    mListener.submit(materialEditText.getText().toString());
                }
            }
        });

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public interface TextInputDialogListener {
        void submit(String text);
    }
}
