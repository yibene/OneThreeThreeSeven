package com.el.uso.onethreethreeseven.view;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import java.lang.ref.WeakReference;

/**
 * \
 * <p>
 * Created by skyer on 15/2/16.
 */
public class CustomTypefaceSpan extends MetricAffectingSpan {

    private WeakReference<Typeface> mTypeface;
    private float mSize;

    public CustomTypefaceSpan(Typeface typeface, float size) {
        mTypeface = new WeakReference<>(typeface);
        mSize = size;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface.get());
        p.setTextSize(mSize);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface.get());
        tp.setTextSize(mSize);
    }

    public Typeface getTypeface() {
        return mTypeface.get();
    }

    public float getFontSize() {
        return mSize;
    }
}
