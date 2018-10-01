package com.el.uso.onethreethreeseven.helper;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public class BitmapPool {
    static private BitmapPool mInstance;

    private ConcurrentHashMap<String, WeakReference<Bitmap>> mList = new ConcurrentHashMap<>();

    static public BitmapPool inst() {
        if (mInstance == null)
            mInstance = new BitmapPool();

        return mInstance;
    }

    public Bitmap getBitmap(String key) {
        if (!mList.containsKey(key))
            return null;

        WeakReference<Bitmap> bmpReference = mList.get(key);
        return bmpReference.get();
    }

    public void putBitmap(String key, Bitmap bmp) {
        if (mList.containsKey(key))
            mList.remove(key);

        mList.put(key, new WeakReference<>(bmp));
    }

    public void removeBitmapAndNoLink(Bitmap removeBmp) {
        for (String key : mList.keySet()) {
            WeakReference<Bitmap> bmpReference = mList.get(key);
            Bitmap bmp = bmpReference.get();
            if (bmp == null || bmp == removeBmp) {
                mList.remove(key);
            }
        }
    }

    public void clear() {
        for (WeakReference<Bitmap> bmpReference : mList.values()) {
            Bitmap bmp = bmpReference.get();
            if (bmp != null && !bmp.isRecycled())
                bmp.recycle();
            bmpReference.clear();
        }
        mList.clear();
    }
}
