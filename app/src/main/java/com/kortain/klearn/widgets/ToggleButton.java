package com.kortain.klearn.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.kortain.klearn.R;

import java.util.Arrays;

/**
 * Created by satiswardash on 18/01/18.
 */

public class ToggleButton extends org.honorato.multistatetogglebutton.MultiStateToggleButton {

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setSwitchElements (String[] elements, boolean[] flags){
        setPressedColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorBackgroundSecondary));
        setNotPressedColors(getResources().getColor(R.color.colorBackgroundSecondary), 0);
        setElements(Arrays.asList(elements), flags);
        setBackground(getResources().getDrawable(R.drawable.bg_toggle_white_stroke));
    }

}
