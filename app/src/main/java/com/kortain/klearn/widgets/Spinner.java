package com.kortain.klearn.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.kortain.klearn.R;

import org.angmarch.views.NiceSpinner;

/**
 * Created by satiswardash on 30/01/18.
 */

public class Spinner extends NiceSpinner {


    public Spinner(Context context) {
        super(context);
    }

    public Spinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_rectangle_stroke));
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
    }
}
