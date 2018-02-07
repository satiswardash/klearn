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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kortain.klearn.CreateNewFeedActivity;
import com.kortain.klearn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputOptions extends Fragment {


    private CreateNewFeedActivity mActivity;
    private Button mPublishButton;
    private RadioGroup mRadioGroup;
    private EditText mOptionEditText1;
    private EditText mOptionEditText2;
    private EditText mOptionEditText3;
    private EditText mOptionEditText4;

    public InputOptions() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_input_options, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        mPublishButton = view.findViewById(R.id.fio_publish_button);
        mRadioGroup = view.findViewById(R.id.fio_objective_radio_group);

        mOptionEditText1 = view.findViewById(R.id.fio_option1_editText);
        mOptionEditText2 = view.findViewById(R.id.fio_option2_editText);
        mOptionEditText3 = view.findViewById(R.id.fio_option3_editText);
        mOptionEditText4 = view.findViewById(R.id.fio_option4_editText);

        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setOptionValues(view)) {
                    MessageDialog dialog = new MessageDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", "Would you like to add some extra information to your post?");
                    bundle.putString("positive", "yes");
                    bundle.putString("negative", "no");
                    dialog.setArguments(bundle);
                    dialog.setCancelable(true);
                    dialog.show(getFragmentManager(), MessageDialog.class.toString());
                } else
                    Toast.makeText(mActivity, "You should enter all the four options and select the correct one!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     *
     * @param view
     * @return
     */
    private boolean setOptionValues(View view) {

        if (mOptionEditText1.getText().toString().isEmpty() ||
                mOptionEditText2.getText().toString().isEmpty() ||
                mOptionEditText3.getText().toString().isEmpty() ||
                mOptionEditText4.getText().toString().isEmpty())
            return false;

        int i = mRadioGroup.indexOfChild(view.findViewById(mRadioGroup.getCheckedRadioButtonId()));
        if (i == -1)
            return false;
        else
            mActivity.answer = i;

        List<String> options = new ArrayList<>();
        if (mOptionEditText1.getText().toString().isEmpty())
            return false;
        else
            options.add(mOptionEditText1.getText().toString());

        if (mOptionEditText2.getText().toString().isEmpty())
            return false;
        else
            options.add(mOptionEditText2.getText().toString());

        if (mOptionEditText3.getText().toString().isEmpty())
            return false;
        else
            options.add(mOptionEditText3.getText().toString());

        if (mOptionEditText4.getText().toString().isEmpty())
            return false;
        else
            options.add(mOptionEditText4.getText().toString());

        mActivity.options = options;
        return true;
    }
}
