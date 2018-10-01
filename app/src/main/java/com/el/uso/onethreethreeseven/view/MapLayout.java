package com.el.uso.onethreethreeseven.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapLayout extends RelativeLayout {
    /**
     * Reference to a GoogleMap object
     */
    private GoogleMap mMap;
    /**
     * Vertical offset in pixels between the bottom edge of our InfoWindow
     * and the marker position (by default it's bottom edge too).
     * It's a good idea to use custom markers and also the InfoWindow frame,
     * because we probably can't rely on the sizes of the default marker and frame.
     */
    private int mBottomOffsetPixels;
    /**
     * A currently selected marker
     */
    private Marker mMarker;
    /**
     * Our custom view which is returned from either the InfoWindowAdapter.getInfoContents
     * or InfoWindowAdapter.getInfoWindow
     */
    private View mInfoWindow;

    public MapLayout(Context context) {
        this(context, null);
    }

    public MapLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(GoogleMap map, int bottomOffsetPixels) {
        mMap = map;
        mBottomOffsetPixels = bottomOffsetPixels;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = false;
        // Make sure that the infoWindow is shown and we have all the needed references
        if (mMarker != null && mMarker.isInfoWindowShown() && mMap != null && mInfoWindow != null) {
            // Get a marker position on the screen
            Point point = mMap.getProjection().toScreenLocation(mMarker.getPosition());

            // Make a copy of the MotionEvent and adjust it's location
            // so it is relative to the infoWindow left top corner
            MotionEvent copyEv = MotionEvent.obtain(ev);
            copyEv.offsetLocation(
                    -point.x + (mInfoWindow.getWidth() / 2),
                    -point.y + mInfoWindow.getHeight() + mBottomOffsetPixels);
            // Dispatch the adjusted MotionEvent to the infoWindow
            ret = mInfoWindow.dispatchTouchEvent(copyEv);
            copyEv.recycle();
        }
        // If the infoWindow consumed the touch event, then just return true.
        // Otherwise pass this event to the super class and return it's result
        return ret || super.dispatchTouchEvent(ev);
    }
}
