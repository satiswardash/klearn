package com.kortain.klearn.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.kortain.klearn.R;

/**
 * Created by satiswardash on 19/01/18.
 */

public class ApplicationUtility {

    private static ApplicationUtility instance;
    private Context mContext;

    private ApplicationUtility(Context context) {
        mContext = context;
    }

    public static ApplicationUtility getInstance(Context context){
        return new ApplicationUtility(context);
    }

    /**
     * setStatusBarBackground
     *
     * @param activity
     * @param color
     */
    public void setStatusBarBackground(Activity activity, int color) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
    }

    /**
     * setStatusBarVisibility
     *
     * @param activity
     * @param visibility
     */
    public void setStatusBarVisibility(AppCompatActivity activity, int visibility) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = activity.getSupportActionBar();

        if (visibility == View.INVISIBLE) {
            actionBar.hide();
        }
        else if (visibility == View.VISIBLE) {
            actionBar.show();
        }
    }

    /**
     * getDrawableForButton
     *
     * @param button
     * @param drawableId
     * @param tint
     */
    public void setLeftDrawableForButton(Button button, int drawableId, int tint) {
        Drawable[] drawables = button.getCompoundDrawables();
        Drawable leftCompoundDrawable = drawables[0];
        Drawable img = mContext.getResources().getDrawable(drawableId);
        if (tint > 0) {
            img.setTint(mContext.getResources().getColor(tint));
        }
        img.setBounds(leftCompoundDrawable.getBounds());
        button.setCompoundDrawables(img, null, null, null);
    }
}
