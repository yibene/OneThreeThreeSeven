package com.el.uso.onethreethreeseven.helper;

import android.content.Context;

import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.log.L;

import java.lang.ref.WeakReference;

public class Config {

    private static final String TAG = "Config";

    private static Config mInst;
    private Context mAppContext;
    private int mStatusBarHeight = 0;
    private boolean mIsInitialized = false;

    protected WeakReference<MainActivity> mMainActivity;

    public MainActivity getMainActivity() {

        if (mMainActivity == null) {
            L.w(TAG + " getMainActivity() is null");
            return null;
        }

        return mMainActivity.get();
    }

    private Config() {

    }

    public static Config inst() {
        if (mInst == null) {
            mInst = new Config();
        }

        return mInst;
    }

    public void init(Context appContext) {
        if (mIsInitialized)
            return;

        mAppContext = appContext;
        int resourceId = mAppContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = mAppContext.getResources().getDimensionPixelSize(resourceId);
        } else {
            L.e("Cannot get status_bar_height, may cause incorrect hit dectection in setup page.");
        }
        mIsInitialized = true;
    }
}
