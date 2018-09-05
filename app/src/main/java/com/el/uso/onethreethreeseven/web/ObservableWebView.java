package com.el.uso.onethreethreeseven.web;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ObservableWebView extends WebView {
    private OnScrollChangeListener mOnScrollChangedListener;
    private int mFooterViewHeight;

    public ObservableWebView(final Context context) {
        super(context);
    }

    public ObservableWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldL, final int oldT) {
        super.onScrollChanged(l, t, oldL, oldT);
        if (mOnScrollChangedListener != null) mOnScrollChangedListener.onScrollChange(this, l, t, oldL, oldT);
    }

    public void setOnScrollChangedListener(final OnScrollChangeListener onScrollChangedListener) {
        mOnScrollChangedListener = onScrollChangedListener;
    }

    public void setFooterViewHeight(int height) {
        mFooterViewHeight = height;
    }

    public boolean isScrollToBottom() {
        return getScrollY() >= (computeVerticalScrollRange() - getMeasuredHeight() - mFooterViewHeight);
    }

    /**
     * Implement in the activity/fragment/view that you want to listen to the web view
     */
    public interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v          The view whose scroll position has changed.
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(ObservableWebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
