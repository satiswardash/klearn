package com.kortain.klearn.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.adapters.NewsFeedAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by satiswardash on 20/01/18.
 */

public class ObjectiveFeedViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private NewsFeedAdapter mAdapter;

    private TextView category;
    private TextView timestamp;
    private ImageView userImage;
    private TextView title;
    private ImageButton menuButton;
    private Button likeButton;

    private RadioGroup options;
    private TextView analytics;
    private ImageView viewAnswer;
    private TextView message;
    private ImageView information;

    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;

    private boolean likeFlag = false;
    private ApplicationUtility mUtility;

    /**
     * Overloaded parameter constructor
     *
     * @param itemView
     * @param context
     * @param adapter
     */
    public ObjectiveFeedViewHolder(View itemView, Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        super(itemView);
        mContext = context;
        mAdapter = (NewsFeedAdapter) adapter;
        mUtility = ApplicationUtility.getInstance(context);
        initLayout();
    }

    /**
     * Initialize the view elements
     */
    private void initLayout() {
        category = itemView.findViewById(R.id.cof_category);
        timestamp = itemView.findViewById(R.id.cof_timestamp);
        userImage = itemView.findViewById(R.id.cof_user_image);
        title = itemView.findViewById(R.id.cof_title);
        menuButton = itemView.findViewById(R.id.cof_menu_button);
        likeButton = itemView.findViewById(R.id.cof_like_button);

        options = itemView.findViewById(R.id.obl_radio_group);
        analytics = itemView.findViewById(R.id.obl_analytics);
        viewAnswer = itemView.findViewById(R.id.obl_view_button);
        message = itemView.findViewById(R.id.obl_message);
        information = itemView.findViewById(R.id.obl_info_button);

        option1 = itemView.findViewById(R.id.obl_radioButton1);
        option2 = itemView.findViewById(R.id.obl_radioButton2);
        option3 = itemView.findViewById(R.id.obl_radioButton3);
        option4 = itemView.findViewById(R.id.obl_radioButton4);

    }

    /**
     * bind the data item to the View object
     *
     * @param position
     */
    public void bind(int position) {

        DocumentSnapshot item = mAdapter.getmDataItems().get(position);
        if (item.contains(Constants.FEED_ANALYTICS)) {
            long data = (long) item.get(Constants.FEED_ANALYTICS);
            if (data > 0) {
                //TODO Convert the data into proper format and add HTML text format for multiple color support
                analytics.setText(data+" people answered correctly");
            }
        }
        if (item.contains(Constants.FEED_ANSWER)) {
            //TODO
            long data = (long) item.get(Constants.FEED_ANSWER);
        }
        if (item.contains(Constants.FEED_CATEGORY)) {
            String data = item.getString(Constants.FEED_CATEGORY);
            category.setText(data);
        }
        if (item.contains(Constants.FEED_TIMESTAMP)) {
            //TODO change the date value into appropriate format
            Date data = item.getDate(Constants.FEED_TIMESTAMP);
            CharSequence format = DateUtils.getRelativeTimeSpanString(data.getTime());
            if (format.toString().equals("0 minutes ago"))
                timestamp.setText("Just now");
            else
                timestamp.setText(format.toString());
        }
        if (item.contains(Constants.FEED_DESCRIPTION)) {
            ArrayList<String> data = (ArrayList<String>) item.get(Constants.FEED_DESCRIPTION);
            option1.setText(data.get(0));
            option2.setText(data.get(1));
            option3.setText(data.get(2));
            option4.setText(data.get(3));
        }
        if (item.contains(Constants.FEED_INFORMATION)) {
            String data = item.getString(Constants.FEED_INFORMATION);
            //TODO create a new fragment dialog to show extra info
        }
        if (item.contains(Constants.FEED_LIKES)) {
            long data = (long) item.get(Constants.FEED_LIKES);
            likeButton.setText(data+"");
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLikeButtonPressed();
                }
            });
        }
        if (item.contains(Constants.FEED_TITLE)) {
            String data = item.getString(Constants.FEED_TITLE);
            title.setText(data);
        }
    }

    /**
     * Like button handler
     */
    private void onLikeButtonPressed() {
        if (likeFlag == false) {
            int likes = Integer.parseInt(likeButton.getText().toString());

            likeButton.setText((likes+1)+"");
            likeButton.setBackground(mContext.getResources().getDrawable(R.drawable.color_primary_gradient));
            likeButton.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
            mUtility.setLeftDrawableForButton(likeButton, R.drawable.ic_favorite_primary_16dp, R.color.colorWhite);

            likeFlag = true;

            //TODO Update the firestore
        }
        else {
            int likes = Integer.parseInt(likeButton.getText().toString());

            likeButton.setText((likes-1)+"");
            likeButton.setBackground(mContext.getResources().getDrawable(R.drawable.bg_likebutton_primary_stroke_unpressed));
            likeButton.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mUtility.setLeftDrawableForButton(likeButton, R.drawable.ic_favorite_border_primary_16dp, R.color.colorPrimary);

            likeFlag = false;

            //TODO Update the firestore
        }
    }
}
