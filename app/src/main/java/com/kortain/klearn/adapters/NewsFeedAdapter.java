package com.kortain.klearn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.kortain.klearn.R;
import com.kortain.klearn.Utility.Constants;
import com.kortain.klearn.viewholders.ImageFeedViewHolder;
import com.kortain.klearn.viewholders.ObjectiveFeedViewHolder;
import com.kortain.klearn.viewholders.RegularFeedViewHolder;
import com.kortain.klearn.viewholders.WebFeedViewHolder;

import java.util.List;

/**
 * Created by satiswardash on 19/01/18.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = NewsFeedAdapter.class.toString();
    private Context mContext;
    private List<DocumentSnapshot> mDataItems;

    public NewsFeedAdapter(Context mContext, List<DocumentSnapshot> mDataItems) {
        this.mContext = mContext;
        this.mDataItems = mDataItems;
    }

    public List<DocumentSnapshot> getmDataItems() {
        return mDataItems;
    }

    public void setmDataItems(List<DocumentSnapshot> mDataItems) {
        this.mDataItems = mDataItems;
    }

    @Override
    public int getItemViewType(int position) {

        int viewType = 0;
        DocumentSnapshot item = mDataItems.get(position);
        if (item != null && item.contains(Constants.FEED_TYPE)) {
            String itemType = item.getString(Constants.FEED_TYPE);
            switch (itemType) {

                case Constants.FEED_CATEGORY_OBJECTIVE: {
                    viewType = 1;
                    break;
                }
                case Constants.FEED_CATEGORY_WEB: {
                    viewType = 2;
                    break;
                }
                case Constants.FEED_CATEGORY_REGULAR: {
                    viewType = 3;
                    break;
                }
                case Constants.FEED_CATEGORY_IMAGE: {
                    viewType = 4;
                    break;
                }
            }
        }
        else {
            Log.e(TAG, "getItemViewType: DocumentSnapshot type exception.");
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1: {
                return new ObjectiveFeedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_objective_feed, parent, false), mContext, this);
            }
            case 2: {
                return new WebFeedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_webpreview_feed, parent, false), mContext, this);
            }
            case 3: {
                return new RegularFeedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_regular_feed, parent, false), mContext, this);
            }
            case 4: {
                return new ImageFeedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.card_image_feed, parent, false), mContext, this);
            }
            default: {
                Log.w(TAG, "onCreateViewHolder: ViewHolder viewType warning, default switch statement executed.");
                return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataItems.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {

            case 1: {
                ObjectiveFeedViewHolder viewHolder = (ObjectiveFeedViewHolder) holder;
                viewHolder.bind(position);
                break;
            }
            case 2: {
                WebFeedViewHolder viewHolder = (WebFeedViewHolder) holder;
                viewHolder.bind(position);
                break;
            }
            case 3: {
                RegularFeedViewHolder viewHolder = (RegularFeedViewHolder) holder;
                viewHolder.bind(position);
                break;
            }
            case 4: {
                ImageFeedViewHolder viewHolder = (ImageFeedViewHolder) holder;
                viewHolder.bind(position);
                break;
            }
            default: {
                //TODO
                break;
            }
        }
    }
}
