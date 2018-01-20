package com.kortain.klearn.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.kortain.klearn.MainActivity;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.ApplicationUtility;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.Utility.ScreenUtility;
import com.kortain.klearn.adapters.NewsFeedAdapter;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by satiswardash on 20/01/18.
 */

public class WebFeedViewHolder extends RecyclerView.ViewHolder implements LinkPreviewCallback {

    private Context mContext;
    private NewsFeedAdapter mAdapter;
    private WebFeedViewHolder mViewHolder = this;

    private TextView category;
    private TextView timestamp;
    private ImageView userImage;
    private TextView title;
    private TextView description;
    private ImageButton menuButton;
    private Button likeButton;

    private ImageView urlPreview;
    private TextView urlTitle;
    private TextView urlAddress;
    private ImageView urlBookmarkButton;

    private boolean likeFlag = false;
    private boolean bookmarkFlag = false;
    private ApplicationUtility mUtility;

    /**
     * Overloaded parameter constructor
     *
     * @param itemView
     * @param context
     * @param adapter
     */
    public WebFeedViewHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
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
        category = itemView.findViewById(R.id.cwf_category);
        timestamp = itemView.findViewById(R.id.cwf_timestamp);
        userImage = itemView.findViewById(R.id.cwf_user_image);
        title = itemView.findViewById(R.id.cwf_title);
        description = itemView.findViewById(R.id.cwf_description);
        menuButton = itemView.findViewById(R.id.cwf_menu_button);
        likeButton = itemView.findViewById(R.id.cwf_like_button);
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
            timestamp.setText(data.toString());
        }
        if (item.contains(Constants.FEED_DESCRIPTION)) {
            String data = item.getString(Constants.FEED_DESCRIPTION);
            TextCrawler mCrawler = new TextCrawler();
            //TODO Validate the data whether its a web url or not
            mCrawler.makePreview(mViewHolder, data);
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

    @Override
    public void onPre() {
        urlPreview = itemView.findViewById(R.id.lwp_preview_image);
        urlTitle = itemView.findViewById(R.id.lwp_title);
        urlAddress = itemView.findViewById(R.id.lwp_url);
        urlBookmarkButton = itemView.findViewById(R.id.lwp_bookmark);

        urlBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bookmarkFlag) {
                    urlBookmarkButton.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));
                    bookmarkFlag = true;
                    //TODO Update firestore after adding the article to user's bookmark
                }
                else {
                    urlBookmarkButton.setColorFilter(mContext.getResources().getColor(R.color.colorGrey));
                    bookmarkFlag = false;
                    //TODO Update firestore after removing the article from user's bookmark
                }
            }
        });
    }

    @Override
    public void onPos(SourceContent sourceContent, boolean b) {
        urlTitle.setText(sourceContent.getTitle());
        urlAddress.setText(sourceContent.getCannonicalUrl());
        ScreenUtility sUtility = getScreenUtility();
        Picasso.with(mContext)
                .load(sourceContent.getImages().get(0)).placeholder(R.drawable.ic_placeholder_image)
                .resize((int) (375*sUtility.getDensity()), (int) (500*sUtility.getDensity()))
                .onlyScaleDown()
                .placeholder(R.drawable.ic_placeholder_image)
                .into(urlPreview);
    }

    private ScreenUtility getScreenUtility() {
        return MainActivity.SCREEN_UTILITY;
    }
}
