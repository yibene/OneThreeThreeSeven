package com.el.uso.onethreethreeseven.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.helper.BitmapPool;

public class CircleLoadingView extends View {
    private Bitmap mBitmap;
    private BitmapShader mShader;
    private Paint mPaint;
    private ValueAnimator mVaStart;
    private ValueAnimator mVaEnd;
    private float mStart;
    private float mEnd;
    private RectF mOvalRect;
    private AnimatorSet mAnimator;
    private boolean mAttach;
    private int mBorderWidth;
    private int mBackgroundBorderColor;

    public CircleLoadingView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
        int res = ta.getResourceId(R.styleable.CircleLoadingView_border_bmp, 0);
        String key = (res != 0) ? "CircleLoadingView" + res : "CircleLoadingViewLoading";
        mBitmap = BitmapPool.inst().getBitmap(key);
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), (res != 0) ? res : R.drawable.db_loading);
            BitmapPool.inst().putBitmap(key, mBitmap);
        }
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.CircleLoadingView_border_width, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.3f, getResources().getDisplayMetrics()));
        mBackgroundBorderColor = ta.getColor(R.styleable.CircleLoadingView_border_background_color, 0xffb9bcbf);
        ta.recycle();

        mStart = mEnd = -90;
        mOvalRect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setShader(mShader);
        if (mBorderWidth > 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
        }
        mPaint.setAntiAlias(true);

        mOvalRect.inset(mBorderWidth / 2, mBorderWidth / 2);

        mVaStart = ValueAnimator.ofFloat(-90, 270);
        mVaStart.setInterpolator(new AccelerateDecelerateInterpolator());
        mVaStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStart = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mVaStart.setDuration(1000);

        mVaEnd = ValueAnimator.ofFloat(-90, 270);
        mVaEnd.setInterpolator(new AccelerateDecelerateInterpolator());
        mVaEnd.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mEnd = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mVaEnd.setDuration(1000);
        mVaEnd.setStartDelay(300);

        mAnimator = new AnimatorSet();
        mAnimator.playTogether(mVaStart, mVaEnd);
        //mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mStart = mEnd = -90;
                if (mAttach) {
                    mAnimator.setStartDelay(300);
                    mAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean useCenter = (mBorderWidth <= 0);
        if (mBackgroundBorderColor != Color.TRANSPARENT) {
            mPaint.setShader(null);
            mPaint.setColor(mBackgroundBorderColor);
            canvas.drawArc(mOvalRect, mStart, 360 - (mStart - mEnd), useCenter, mPaint);
        }

        mPaint.setShader(mShader);
        canvas.drawArc(mOvalRect, mEnd, mStart - mEnd, useCenter, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttach = true;
        mAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttach = false;
        mAnimator.cancel();

        super.onDetachedFromWindow();
    }
}
