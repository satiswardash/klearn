package com.kortain.klearn.Utility;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by satiswardash on 04/12/17.
 */

public class ScreenUtility {
    private final String TAG = ScreenUtility.class.toString();

    private Activity activity;
    private float dpWidth;
    private float dpHeight;
    private float density;

    private ScreenUtility(Activity activity) {
        this.activity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        density = activity.getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels;
        dpWidth = outMetrics.widthPixels;

    }

    public static ScreenUtility getInstance(Activity activity) {
        return new ScreenUtility(activity);
    }

    public ScreenSize getScreenDimension() {
        int apiLevel = Build.VERSION.SDK_INT;

        if (apiLevel >= Build.VERSION_CODES.M) {

        } else {

        }
        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return ScreenSize.NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return ScreenSize.LARGE;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return ScreenSize.XLARGE;
        }
        return ScreenSize.LARGE;
    }

    public float getWidth() {
        return dpWidth;
    }

    public float getHeight() {
        return dpHeight;
    }

    public float getDensity() {
        return density;
    }
}
