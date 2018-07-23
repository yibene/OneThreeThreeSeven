package com.el.uso.onethreethreeseven.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by skyer on 14/7/31.
 */
public class FontManager {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GoFont.FONT_FLAMA_CONDENSED_BOLD,
            GoFont.FONT_FLAMA_SEMICONDENSED_ULTRALIGHT,
            GoFont.FONT_FLAMA_SEMICONDENSED_ULTRALIGHTNEW,
            GoFont.FONT_GRAPHIK_BLACK_ITALIC,
            GoFont.FONT_GRAPHIK_LIGHT,
            GoFont.FONT_GRAPHIK_LIGHT_ITALIC,
            GoFont.FONT_GRAPHIK_SEMIBOLD,
            GoFont.FONT_XINGOTHIC_W3,
            GoFont.FONT_XINGOTHIC_W8,
            GoFont.FONT_SOURCE_HANSANS_EXTRALIGHT
    })
    public @interface GoFont {
        int FONT_FLAMA_CONDENSED_BOLD = 0;
        int FONT_FLAMA_SEMICONDENSED_ULTRALIGHT = 1;
        int FONT_FLAMA_SEMICONDENSED_ULTRALIGHTNEW = 2;

        int FONT_GRAPHIK_BLACK_ITALIC = 3;
        int FONT_GRAPHIK_LIGHT = 4;    // default font
        int FONT_GRAPHIK_LIGHT_ITALIC = 5;
        int FONT_GRAPHIK_SEMIBOLD = 6;

        int FONT_XINGOTHIC_W3 = 7;
        int FONT_XINGOTHIC_W8 = 8;

        int FONT_SOURCE_HANSANS_EXTRALIGHT = 9;
    }

    private static FontManager mInstance;
    private SparseArray<Typeface> mFontCache;

    public static FontManager inst() {
        if (mInstance == null) mInstance = new FontManager();
        return mInstance;
    }

    private FontManager() {
        mFontCache = new SparseArray<>();
    }

    public Typeface getFont(Context context, String fontName) {
        Typeface font = null;

        if (fontName != null && fontName.length() > 0) {
            String fontPath = "fonts/" + fontName;
            int index = mFontCache.indexOfKey(fontPath.hashCode());

            if (index >= 0) {
                font = mFontCache.valueAt(index);
            } else {
                font = Typeface.createFromAsset(context.getAssets(), fontPath);
                if (font != null) {
                    mFontCache.append(fontPath.hashCode(), font);
                }
            }
        }
        return font;
    }

    public Typeface getFont(Context context, @GoFont int goFont) {
        return getFont(context, getFontName(goFont));
    }

    private static String getFontName(@GoFont int goFont) {
        String fontName;

        switch (goFont) {
            case GoFont.FONT_FLAMA_CONDENSED_BOLD:
                fontName = "FlamaCondensed-Bold.otf";
                break;
            case GoFont.FONT_FLAMA_SEMICONDENSED_ULTRALIGHT:
                fontName = "FlamaSemicondensed-Ultralight.otf";
                break;
            case GoFont.FONT_FLAMA_SEMICONDENSED_ULTRALIGHTNEW:
                fontName = "FlamaSemicondensed-UltralightNew.otf";
                break;
            case GoFont.FONT_GRAPHIK_BLACK_ITALIC:
                fontName = "Graphik-BlackItalic.otf";
                break;
            case GoFont.FONT_GRAPHIK_LIGHT:
                fontName = "Graphik-Light.otf";
                break;
            case GoFont.FONT_GRAPHIK_LIGHT_ITALIC:
                fontName = "Graphik-LightItalic.otf";
                break;
            case GoFont.FONT_GRAPHIK_SEMIBOLD:
                fontName = "Graphik-Semibold.otf";
                break;
            case GoFont.FONT_XINGOTHIC_W3:
                fontName = "SourceHanSansTW-Light.otf";
                break;
            case GoFont.FONT_XINGOTHIC_W8:
                fontName = "SourceHanSansTW-Bold.otf";
                break;
            case GoFont.FONT_SOURCE_HANSANS_EXTRALIGHT:
                fontName = "SourceHanSansTC-ExtraLight.otf";
                break;
            default:
                fontName = "Graphik-Light.otf";
                break;
        }
        return fontName;
    }
}
