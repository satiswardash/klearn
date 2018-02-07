package com.kortain.klearn.Utility;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by satiswardash on 07/02/18.
 */

public class CreateFeedUtility {

    private CreateFeedUtility() {}

    public static CreateFeedUtility getInstance() {
        return new CreateFeedUtility();
    }

    /**
     * Creates Feed object by using below parameters and differentiate by @param type
     * Gives a {@link HashMap<String, Object>} object after putting all the param values with pre-defined feed keys for Firestore db
     *
     * @param userId
     * @param type
     * @param category
     * @param title
     * @param description
     * @param analytics
     * @param likes
     * @param imageUris
     * @param webUrl
     * @param options
     * @param information
     * @param answer
     * @return
     */
    public Map<String, Object> createNewFeed(
            @Nullable String userId,
            @NonNull String type,
            @Nullable String category,
            @Nullable String title,
            @Nullable String description,
            int analytics,
            int likes,
            @Nullable List<String> imageUris,
            @Nullable String webUrl,
            @Nullable List<String> options,
            @Nullable String information,
            int answer) {

        Map<String, Object> feed = new HashMap<>();
        feed.put(Constants.FEED_ANALYTICS, analytics);
        feed.put(Constants.FEED_CATEGORY, category);
        feed.put(Constants.FEED_TIMESTAMP, new Date());
        feed.put(Constants.FEED_LIKES, likes);
        feed.put(Constants.FEED_TITLE, title);
        feed.put(Constants.FEED_TYPE, type);
        feed.put(Constants.FEED_OWNER, userId);

        switch (type) {

            case "Regular Article":{
                feed.put(Constants.FEED_DESCRIPTION, description);
                break;
            }

            case "Image Article":{
                feed.put(Constants.FEED_IMAGES, imageUris);
                feed.put(Constants.FEED_DESCRIPTION, description);
                break;
            }

            case "Web Article":{
                feed.put(Constants.FEED_WEB_URL, webUrl);
                feed.put(Constants.FEED_DESCRIPTION, description);
                break;
            }

            case "Objective":{
                feed.put(Constants.FEED_DESCRIPTION, options);
                feed.put(Constants.FEED_INFORMATION, information);
                feed.put(Constants.FEED_ANSWER, answer);
                break;
            }
        }

        return feed;
    }
}
