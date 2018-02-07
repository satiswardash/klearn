package com.kortain.klearn.Utility;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.kortain.klearn.fragments.LoadingFragment;

/**
 * Created by satiswardash on 07/02/18.
 */

public class ProgressLoaderUtility {

    private int mLayoutId;
    private FragmentManager mFragmentManager;

    /**
     *
     * @param layout
     * @param fragmentManager
     */
    private ProgressLoaderUtility(int layout, FragmentManager fragmentManager) {
        mLayoutId = layout;
        mFragmentManager = fragmentManager;
    }

    /**
     *
     * @param layout
     * @param fragmentManager
     * @return
     */
    public static ProgressLoaderUtility getInstance(int layout, FragmentManager fragmentManager) {
        return new ProgressLoaderUtility(layout, fragmentManager);
    }

    /**
     *
     * @param visibility
     */
    public void setLoadingScreen(int visibility) {
        if (visibility == View.VISIBLE) {
            mFragmentManager
                    .beginTransaction()
                    .add(mLayoutId, new LoadingFragment(), LoadingFragment.class.toString())
                    .addToBackStack(LoadingFragment.class.toString())
                    .commit();
        } else {
            mFragmentManager.popBackStackImmediate();
        }

    }
}
