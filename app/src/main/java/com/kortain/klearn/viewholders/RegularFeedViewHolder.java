package com.kortain.klearn.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.adapters.NewsFeedAdapter;

import java.util.Date;

/**
 * Created by satiswardash on 20/01/18.
 */

public class RegularFeedViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private NewsFeedAdapter mAdapter;

    private TextView category;
    private TextView timestamp;
    private ImageView userImage;
    private TextView title;
    private TextView description;
    private ImageButton menuButton;
    private Button likeButton;

    private boolean likeFlag = false;
    private ApplicationUtility mUtility;

    /**
     * Overloaded parameter constructor
     *
     * @param itemView
     * @param context
     * @param adapter
     */
    public RegularFeedViewHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
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
        category = itemView.findViewById(R.id.crf_category);
        timestamp = itemView.findViewById(R.id.crf_timestamp);
        userImage = itemView.findViewById(R.id.crf_user_image);
        title = itemView.findViewById(R.id.crf_title);
        description = itemView.findViewById(R.id.crf_description);
        menuButton = itemView.findViewById(R.id.crf_menu_button);
        likeButton = itemView.findViewById(R.id.crf_like_button);
    }

    /**
     * bind the data item to the View object
     *
     * @param position
     */
    public void bind(int position) {

        DocumentSnapshot item = mAdapter.getmDataItems().get(position);
        if (item.contains(Constants.FEED_CATEGORY)) {
            String data = item.getString(Constants.FEED_CATEGORY);
            category.setText(data);
        }
        if (item.contains(Constants.FEED_TIMESTAMP)) {
            Date data = item.getDate(Constants.FEED_TIMESTAMP);
            //TODO change the date value into appropriate format
            CharSequence format = DateUtils.getRelativeTimeSpanString(data.getTime());
            if (format.toString().equals("0 minutes ago"))
                timestamp.setText("Just now");
            else
                timestamp.setText(format.toString());
        }
        if (item.contains(Constants.FEED_DESCRIPTION)) {
            String data = item.getString(Constants.FEED_DESCRIPTION);
            //TODO change ellipsize format from "..." to "more..."
            description.setText(data);
        }
        if (item.contains(Constants.FEED_LIKES)) {
            long data = item.getLong(Constants.FEED_LIKES);
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
