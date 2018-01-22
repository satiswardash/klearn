package com.kortain.klearn.Utility;

/**
 * Created by satiswardash on 20/01/18.
 */

public interface Constants {

    String FEED_TYPE = "type";
    String FEED_CATEGORY = "category";
    String FEED_TIMESTAMP = "createdTs";
    String FEED_TITLE = "title";
    String FEED_DESCRIPTION = "description";
    String FEED_LIKES = "likes";
    String FEED_OWNER = "uid";
    String FEED_INFORMATION = "information";
    String FEED_ANSWER = "answer";
    String FEED_ANALYTICS = "analytics";
    String FEED_IMAGES = "images";

    String FEED_CATEGORY_OBJECTIVE = "Objective";
    String FEED_CATEGORY_WEB = "Web Article";
    String FEED_CATEGORY_REGULAR = "Regular Article";
    String FEED_CATEGORY_IMAGE = "Image Article";

    String COLLECTION_FEEDS = "feeds";
    String COLLECTION_USERS = "users";

    String USER_NAME = "nme";
    String USER_PHONE = "phone";
    String USER_EMAIL = "email";
    String USER_POINTS = "points";
    String USER_PICTURE = "pictureUri";
    String USER_QUOTA = "isAdmin";
    String USER_INTERESTS = "interests";
    String USER_BOOKMARKS = "bookmarks";
    String USER_FAVOURITES = "favourites";

    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
}
