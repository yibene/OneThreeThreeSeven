package com.el.uso.onethreethreeseven.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by skyer on 14/8/4.
 */
public class PressStateController {
    public static final int SCENE_DARK = 0;
    public static final int SCENE_LIGHT = 1;

    private static final int PRESS_ANIMATION_TIME = 200;

    private static final int SCENE_DARK_ALPHA = 38;
    private static final int SCENE_DARK_COLOR = Color.argb(SCENE_DARK_ALPHA, 0, 0, 0);

    private static final int SCENE_LIGHT_ALPHA = 26;
    private static final int SCENE_LIGHT_COLOR = Color.argb(SCENE_LIGHT_ALPHA, 255, 255, 255);

    private boolean mPressed;
    private int mPressedAlpha;
    private int mScene;
    private boolean mEnablePressState;
    private WeakReference<View> mViewRef;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mDelayPressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPressed) {
                mPressedAlpha = (mScene == SCENE_DARK) ? SCENE_DARK_ALPHA : SCENE_LIGHT_ALPHA;
                View view = mViewRef.get();
                if (view != null)
                    view.invalidate();
            }
        }
    };

    public PressStateController(View view) {
        mViewRef = new WeakReference<View>(view);
        mPressed = false;
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (!mEnablePressState)
            return false;

        boolean retValue = false;
        int action = event.getAction();
        final View view = mViewRef.get();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPressed = true;
                mHandler.postDelayed(mDelayPressRunnable, 100);
                retValue = true;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mPressed) {
                    mPressed = false;
                    mHandler.removeCallbacks(mDelayPressRunnable);
                    if (action == MotionEvent.ACTION_UP) {
                        mPressedAlpha = (mScene == SCENE_DARK) ? SCENE_DARK_ALPHA : SCENE_LIGHT_ALPHA;
                    }
                    ValueAnimator va = ValueAnimator.ofFloat(mPressedAlpha, 0);
                    va.setDuration(PRESS_ANIMATION_TIME);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mPressedAlpha = (int) (float) animation.getAnimatedValue();
                            if (view != null)
                                view.invalidate();
                        }
                    });
                    va.start();
                    retValue = true;
                }
                break;
        }

        return retValue;
    }

    public void setPressState(int state) {
        final View view = mViewRef.get();

        switch (state) {
            case MotionEvent.ACTION_DOWN:
                mPressed = true;
                mHandler.postDelayed(mDelayPressRunnable, 100);
                if (view != null)
                    view.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPressedAlpha = (state == MotionEvent.ACTION_CANCEL) ? 0 : ((mScene == SCENE_DARK) ? SCENE_DARK_ALPHA : SCENE_LIGHT_ALPHA);
                mPressed = false;
                mHandler.removeCallbacks(mDelayPressRunnable);
                ValueAnimator va = ValueAnimator.ofFloat(mPressedAlpha, 0);
                va.setDuration(PRESS_ANIMATION_TIME);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mPressedAlpha = (int) (float) animation.getAnimatedValue();
                        if (view != null)
                            view.invalidate();
                    }
                });
                va.start();

                break;
        }

    }

    public void onDraw(Canvas canvas) {
        if (mEnablePressState && mPressedAlpha > 0) {
            int color = (mScene == SCENE_DARK ? SCENE_DARK_COLOR : SCENE_LIGHT_COLOR) & 0xffffff;

            canvas.drawColor(((mPressedAlpha & 0xff) << 24) | color);
        }
    }

    public void setEnablePressState(boolean isEnable) {
        mEnablePressState = isEnable;
    }

    public void setScene(int scene) {
        mScene = scene;
    }
}
