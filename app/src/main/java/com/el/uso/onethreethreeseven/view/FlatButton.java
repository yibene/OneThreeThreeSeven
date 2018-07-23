package com.el.uso.onethreethreeseven.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.helper.FontManager;
import com.el.uso.onethreethreeseven.log.L;


/**
 * Created by skyer on 14/7/28.
 * Known issues:
 * - Set gravity as center_vertical will cause wrong height of text view
 */

public class FlatButton extends AppCompatTextView {

    private PressStateController mPressStateController;
    private Paint mPaint;
    private boolean isDebug;
    private boolean isBound;
    private int mOriPaddingTop;
    private int mOriPaddingLeft;
    private int mOriPaddingRight;
    private int mOriPaddingBottom;
    private int mPaddingTop;
    private int mPaddingLeft;
    private int mBoundWidth;
    private int mBoundHeight;
    private Rect mTextBound;
    private Rect mTestBound;
    private int mExtraHeight;
    private int mExtraWidth;
    private int mLinkPage;
    private boolean mFixedLayout;
    private String mUSFontName;
    private String mNonUSFontName;
    private float mNonUSFontSize;
    private int mNonUSFontLineSpacing;
    private int mUSFontLineSpacing = -1;
    private Rect mClipBound = new Rect();
    private Rect mTextBoundTemp = new Rect();
    private Rect mDrawChangeBound = new Rect();
    private boolean mInit;
    private boolean isMeasureBound;

    public FlatButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, @SuppressWarnings("unused") int defStyle, @SuppressWarnings("unused") int defStyleRes) {

        setIncludeFontPadding(false);

        mTextBound = new Rect();
        mTextBound.setEmpty();
        mTestBound = new Rect();
        mTestBound.setEmpty();

        mBoundWidth = mBoundHeight = -1;
        mPaddingTop = mPaddingLeft = 99999;

        mOriPaddingTop = -1;
        mOriPaddingLeft = -1;
        mOriPaddingRight = -1;
        mOriPaddingBottom = -1;

        mPressStateController = new PressStateController(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlatButton);
        mUSFontName = typedArray.getString(R.styleable.FlatButton_font_name);
        mNonUSFontName = typedArray.getString(R.styleable.FlatButton_non_us_font_name);
        mNonUSFontSize = (float) typedArray.getDimensionPixelSize(R.styleable.FlatButton_non_us_font_size, 0);
        mPressStateController.setEnablePressState(typedArray.getBoolean(R.styleable.FlatButton_press_state, true));
        mPressStateController.setScene(typedArray.getInt(R.styleable.FlatButton_scene, 0));
        isDebug = typedArray.getBoolean(R.styleable.FlatButton_debug, false);
        mExtraHeight = typedArray.getDimensionPixelSize(R.styleable.FlatButton_extra_height, 0);
        mExtraWidth = typedArray.getDimensionPixelSize(R.styleable.FlatButton_extra_width, 0);
        mLinkPage = typedArray.getInt(R.styleable.FlatButton_link_page, -1);
        mFixedLayout = typedArray.getBoolean(R.styleable.FlatButton_fix_layout, false);
        mUSFontLineSpacing = (int) getLineSpacingExtra();
        mNonUSFontLineSpacing = typedArray.getDimensionPixelSize(R.styleable.FlatButton_non_us_font_line_spacing, mUSFontLineSpacing);
        isBound = typedArray.getBoolean(R.styleable.FlatButton_isBound, false);

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xffff0000);

        if (mNonUSFontName == null || mNonUSFontName.length() == 0) {
            mNonUSFontName = mUSFontName;
        }

        if (mNonUSFontSize == 0)
            mNonUSFontSize = getTextSize();

        if (mUSFontName != null && mUSFontName.length() > 0) {
            Typeface typeface = FontManager.inst().getFont(context, mUSFontName);
            setTypeface(typeface);
            mPaint.setTypeface(typeface);
            mPaint.setTextSize(getTextSize());
        }

        mInit = true;

        Typeface us = FontManager.inst().getFont(context, mUSFontName);
        Typeface nonUs = FontManager.inst().getFont(context, mNonUSFontName);
        if (us != null && nonUs != null) {
            setText(setFont(getText(), us, getTextSize(), nonUs, mNonUSFontSize));
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        mPressStateController.onDraw(canvas);

        if (isDebug) {
            mPaint.setColor(Color.WHITE);
            canvas.getClipBounds(mClipBound);
            canvas.drawRect(mClipBound, mPaint);

            mPaint.setColor(Color.DKGRAY);
            mPaint.setStrokeWidth(3);
            canvas.drawRect(mTextBound, mPaint);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isDebug) {
            L.d("onMeasure");
        }

        if (isInEditMode())
            return;

        if (getLayout() == null || getText() == null || getText().length() == 0) {
            if (isDebug) L.w("no layout or text");
            return;
        }

        if (mOriPaddingTop == -1) mOriPaddingTop = getPaddingTop();
        if (mOriPaddingLeft == -1) mOriPaddingLeft = getPaddingLeft();
        if (mOriPaddingRight == -1) mOriPaddingRight = getPaddingRight();
        if (mOriPaddingBottom == -1) mOriPaddingBottom = getPaddingBottom();

        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        mTextBoundTemp.setEmpty();
        getTextBoundWithoutPadding(mTextBoundTemp, measuredWidth, measuredHeight);

        int gravity = getGravity();

        mTextBound.set(mTextBoundTemp);
        if (isDebug) {
            L.w("onMeasure update, heightSpec = " + getSpec(heightSpec) + ", mBoundHeight = " + mBoundHeight + ", text bound height = " + mTextBound.height() + ", temp bound height = " + mTextBoundTemp.height() + ", measure height = " + measuredHeight + ", spec.getSize = " + MeasureSpec.getSize(heightMeasureSpec));
        }
        if (heightSpec == MeasureSpec.EXACTLY) {
            mBoundHeight = -1;
        } else if (mBoundHeight == -1 && ((gravity & Gravity.CENTER_VERTICAL) == 0 || ((gravity & Gravity.TOP) == Gravity.TOP || (gravity & Gravity.BOTTOM) == Gravity.BOTTOM))) {
            mTextBound.set(mTextBoundTemp);
            mBoundHeight = mTextBound.height();
            if (isDebug) L.w("BH1 = " + mBoundHeight);
            if (heightSpec == MeasureSpec.AT_MOST) {
                mBoundHeight = Math.min(mBoundHeight, MeasureSpec.getSize(heightMeasureSpec));
                if (isDebug) L.w("BH2 = " + mBoundHeight);
            }
            mPaddingTop = mOriPaddingTop - mTextBound.top;
            if (isDebug) {
                L.d("paddingHeight: left = " + getPaddingLeft() + ", top = " + mPaddingTop + ", right = " + getPaddingRight() + ", bottom = " + getPaddingBottom());
            }
            setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), getPaddingBottom());
        }

        if (widthSpec == MeasureSpec.EXACTLY) {
            mBoundWidth = -1;
        } else if (mBoundWidth == -1 && ((gravity & Gravity.CENTER_HORIZONTAL) == 0 || ((gravity & Gravity.LEFT) == Gravity.LEFT || (gravity & Gravity.RIGHT) == Gravity.RIGHT))) {
            mTextBound.set(mTextBoundTemp);
            mBoundWidth = mTextBound.width();
            if (widthSpec == MeasureSpec.AT_MOST) {
                mBoundWidth = Math.min(mBoundWidth, MeasureSpec.getSize(widthMeasureSpec));
            }
            mPaddingLeft = mOriPaddingLeft - mTextBound.left;
            if (isDebug) {
                L.d("paddingWidth: left = " + mPaddingLeft + ", top = " + getPaddingTop() + ", right = " + getPaddingRight() + ", bottom = " + getPaddingBottom());
            }
            setPadding(mPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }

        if (getLayout() == null) {// Layout change due to setPadding
            if (isDebug) {
                L.w("layout change due to setPadding (getLayout() == null)");
            }
            return;
        }

        if (isDebug) {
            L.w("[Flat] paddingLeft: " + mOriPaddingLeft);
            L.w("[Flat] onMeasure W: " + ((widthSpec >> 30) & 0x3) + ", " + measuredWidth + ", H: " + ((heightSpec >> 30) & 0x3) + ", " + measuredHeight);
            L.w("[Flat] Bound: " + mTextBoundTemp.toString() + ", BW: " + mBoundWidth + ", BH: " + mBoundHeight);
        }

        measuredHeight = ((mBoundHeight == -1 || isMeasureBound) ? measuredHeight : mBoundHeight) + mExtraHeight;
        measuredWidth = ((mBoundWidth == -1 || isMeasureBound) ? measuredWidth : mBoundWidth) + mExtraWidth;

        if (isDebug) {
            L.i("result: measureHeight = " + measuredHeight);
        }

        int lineCount = getLayout().getLineCount();

        int count = 50;
        while ((count--) > 0) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
            if (getLayout().getLineCount() <= lineCount) {
                break;
            }
            mBoundWidth = ++measuredWidth;
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean retValue = super.onTouchEvent(event);
        boolean handled = mPressStateController.onTouchEvent(event);

        return retValue || handled;
    }

    public int getLinkPage() {
        return mLinkPage;
    }

    private void getTextBoundWithoutPadding(Rect rect, int measureWidth, int measureHeight) {

        int width = measureWidth;

        int paddingLeft = mOriPaddingLeft;
        int paddingTop = mOriPaddingTop;
        int paddingRight = mOriPaddingRight;
        int paddingBottom = mOriPaddingBottom;

        Layout sl = getLayout();
        String text = getText().toString();
        if (sl == null || text.length() == 0) {
            rect.set(0, 0, width, measureHeight);
            return;
        }

        CharSequence charSequence = getText();
        CustomTypefaceSpan[] spans = null;
        SpannedString ss = null;
        if (charSequence instanceof SpannedString) {
            ss = (SpannedString) charSequence;
            spans = ss.getSpans(0, ss.length(), CustomTypefaceSpan.class);
        }

        Context context = getContext();
        boolean isAscii = isAscii(text);
        if (isAscii) {
            mPaint.setTypeface(FontManager.inst().getFont(context, mUSFontName));
            mPaint.setTextSize(getTextSize());
        } else {
            mPaint.setTypeface(FontManager.inst().getFont(context, mNonUSFontName));
            mPaint.setTextSize(mNonUSFontSize);
        }

//        if (isDebug) {
//            Bitmap bmp = Bitmap.createBitmap(measureWidth, measureHeight, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bmp);
//            sl.draw(canvas);
//            try {
//                FileOutputStream fos = new FileOutputStream("/sdcard/a.png");
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        int right = 0;
        int left = 0x7fffffff;
        int bottom = 0;
        int top = sl.getLineTop(0);

        for (int i = 0; i < sl.getLineCount(); ++i) {
            int bl = sl.getLineBaseline(i) + paddingTop;

            int start = sl.getLineStart(i);
            int end = sl.getLineEnd(i);

            Rect r = new Rect();
            if (spans == null) {
                String t = text.substring(start, end);
                mPaint.getTextBounds(t, 0, t.length(), r);
            } else {
                try {
                    Rect ir = new Rect();
                    for (CustomTypefaceSpan span : spans) {
                        int spanStart = ss.getSpanStart(span);
                        int spanEnd = ss.getSpanEnd(span);
                        if (spanEnd < start || spanStart > end) continue;
                        mPaint.setTypeface(span.getTypeface());
                        mPaint.setTextSize(span.getFontSize());
                        if (spanEnd > end) spanEnd = end;
                        if (spanStart < start) spanStart = start;
                        mPaint.getTextBounds(text, spanStart, spanEnd, ir);
                        ir.offset(r.right, 0);
                        r.union(ir);
                    }
                } catch (Exception e) {
                    L.w("[FlatButton] get layout fail. " + e);
                }
            }
            if (isDebug) {
                L.d("r = " + r.toString());
                L.d("base line = " + bl);
            }

            left = Math.min(left, r.left);
            right = Math.max(right, r.right);
            if (i == 0)
                top = r.top + bl;

            if (i == sl.getLineCount() - 1) {
                bottom = bl + r.bottom;
            }
        }

        Drawable[] drawables = getCompoundDrawables();
        int drawablesPadding = getCompoundDrawablePadding();

        if (drawables[0] != null) {
            left = 0;
            right += drawables[0].getIntrinsicWidth() + drawablesPadding;
        }
        if (drawables[2] != null) {
            right = width - 1;
        }
        if (drawables[1] != null) {
            top = 0;
            bottom += drawables[1].getIntrinsicHeight() + drawablesPadding;
        }
        if (drawables[3] != null) {
            bottom = measureHeight - 1;
        }

        if (paddingLeft + paddingRight > 0)
            right += paddingLeft + paddingRight;

        if (paddingBottom > 0)
            bottom += paddingBottom;

        top = (top < 0) ? 0 : top;
        left = (left < 0) ? 0 : left;
        right = (right >= width) ? width - 1 : right;
        bottom = (bottom >= measureHeight) ? measureHeight - 1 : bottom;

        rect.top = top;
        rect.left = left;
        rect.right = right;
        rect.bottom = bottom;

        if (isDebug) {
            L.d("[FlatButton]: " + rect.toString());
            L.w("[Flat] getTextBoundWithoutPadding: text bound height = " + rect.height() + ", BH = " + mBoundHeight);
        }
        isMeasureBound = isBound && (mBoundHeight != -1) && (rect.height() >= mBoundHeight);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!mFixedLayout)
            mBoundHeight = mBoundWidth = -1;

        boolean isChanged = !TextUtils.equals(text, getText());

        CharSequence span = setFont(text);
        boolean isAscii = isAscii(text);

        if (mInit)
            setLineSpacing(isAscii ? mUSFontLineSpacing : mNonUSFontLineSpacing, getLineSpacingMultiplier());

        super.setText(span, type);

        // re-layout if text is changed
        if (isChanged) {
            requestLayout();
        }
    }

    public void setFont(String usFontName, String nonUsFontName) {
        mUSFontName = usFontName;
        mNonUSFontName = nonUsFontName;
    }

    private CharSequence setFont(CharSequence text) {
        Context context = getContext();
        Typeface us = FontManager.inst().getFont(context, mUSFontName);
        Typeface nonUs = FontManager.inst().getFont(context, mNonUSFontName);
        return setFont(text, us, getTextSize(), nonUs, mNonUSFontSize);
    }

    private CharSequence setFont(CharSequence text, Typeface usTypeFace, float textSize, Typeface nonUsTypeFace, float nonUsTextSize) {
        if (text == null) text = "";
        SpannableString cs;
        if (text instanceof SpannableStringBuilder) {
            return setFont((SpannableStringBuilder) text, usTypeFace, textSize, nonUsTypeFace, nonUsTextSize);
        } else if (text instanceof SpannableString) {
            cs = (SpannableString) text;
        } else {
            cs = new SpannableString(text.toString());
        }

        boolean currAscii, prevAscii = false;
        int start = 0, end = 0;

        int count = cs.length();
        for (int i = 0; i < count; ++i) {
            char currChar = cs.charAt(i);

            currAscii = isAscii(currChar);
            if (i == 0) {
                prevAscii = isAscii(currChar);
            } else {
                end = i;
            }

            if (currAscii != prevAscii || count == 1) {
                Typeface typeface = (prevAscii) ? usTypeFace : nonUsTypeFace;
                float size = (prevAscii) ? textSize : nonUsTextSize;
                if (isDebug) {
                    L.w("[FlatButton] span " + start + "-" + end + ", " + prevAscii);
                }
                cs.setSpan(new CustomTypefaceSpan(typeface, size), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                start = i;
            }

            prevAscii = currAscii;
        }

        if (start < count) {
            currAscii = isAscii(cs.charAt(start));
            Typeface typeface = (currAscii) ? usTypeFace : nonUsTypeFace;
            float size = (prevAscii) ? textSize : nonUsTextSize;
            if (isDebug) {
                L.w("[FlatButton] span " + start + "-" + end + ", " + currAscii);
            }
            cs.setSpan(new CustomTypefaceSpan(typeface, size), start, count, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return cs;
    }

    private CharSequence setFont(SpannableStringBuilder text, Typeface usTypeFace, float textSize, Typeface nonUsTypeFace, float nonUsTextSize) {
        if (text == null) text = new SpannableStringBuilder();
        SpannableStringBuilder cs = text;

        boolean currAscii, prevAscii = false;
        int start = 0, end = 0;

        int count = cs.length();
        for (int i = 0; i < count; ++i) {
            char currChar = cs.charAt(i);

            currAscii = isAscii(currChar);
            if (i == 0) {
                prevAscii = isAscii(currChar);
            } else {
                end = i;
            }

            if (currAscii != prevAscii || count == 1) {
                Typeface typeface = (prevAscii) ? usTypeFace : nonUsTypeFace;
                float size = (prevAscii) ? textSize : nonUsTextSize;
                if (isDebug) {
                    L.w("[FlatButton] span " + start + "-" + end + ", " + prevAscii);
                }
                cs.setSpan(new CustomTypefaceSpan(typeface, size), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                start = i;
            }

            prevAscii = currAscii;
        }

        if (start < count) {
            currAscii = isAscii(cs.charAt(start));
            Typeface typeface = (currAscii) ? usTypeFace : nonUsTypeFace;
            float size = (prevAscii) ? textSize : nonUsTextSize;
            if (isDebug) {
                L.w("[FlatButton] span " + start + "-" + end + ", " + currAscii);
            }
            cs.setSpan(new CustomTypefaceSpan(typeface, size), start, count, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return cs;
    }

    private boolean isAscii(char c) {
        return (c > 0 && c < 128);
    }

    private boolean isAscii(CharSequence text) {
        if (text == null) return true;

        boolean result = true;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c >= 128) {
                result = false;
                break;
            }
        }

        return result;
    }

    public float getNonUsTextSize() {
        return mNonUSFontSize;
    }

    public void setNonUsFontSize(float size) {
        mNonUSFontSize = size;
    }

    public void setDebug(boolean flag) {
        isDebug = flag;
    }

    public void setEnablePressState(boolean flag) {
        mPressStateController.setEnablePressState(flag);
    }

    public void autoSize(int width, String text, int size, int minSize) {
        Rect bounds = new Rect();
        Paint textPaint = this.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int textheight = bounds.height();
        int textwidth = bounds.width();
        while (textwidth > (width)) {
            if (size < minSize)
                break;
            size -= 5;
            this.setTextSize(size);
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            textheight = bounds.height();
            textwidth = bounds.width();
        }
    }


    private int mData;

    public void setIntData(int nData) {
        mData = nData;
    }

    public int getIntData() {
        return mData;
    }

    private String getIdName(int id) {
        switch (id) {
            case R.id.test_message:
                return "test_message";
            case R.id.test_message2:
                return "test_message2";
            default:
                return "unknown";
        }
    }

    private String getSpec(int spec) {
        switch (spec) {
            case MeasureSpec.AT_MOST:
                return "AT_MOST";
            case MeasureSpec.EXACTLY:
                return "EXACTLY";
            case MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                return "Unknown";
        }
    }

}
