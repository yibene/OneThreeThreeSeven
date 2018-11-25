package com.el.uso.onethreethreeseven.map;

import android.util.SparseArray;

import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.helper.Utils;
import com.el.uso.onethreethreeseven.log.L;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MarkerIconManager {
    private static final String TAG = "MarkerIconManager";
    private SparseArray<BitmapDescriptor> mPinCache = new SparseArray<>();

    public MarkerIconManager() {

    }

    public BitmapDescriptor createMapPinBitmapDescriptor(int pinRes) {
        BitmapDescriptor bitmapDescriptor = null;
        try {
            // return cache if exists
            if (cacheablePin(pinRes)) bitmapDescriptor = mPinCache.get(pinRes);
            if (null != bitmapDescriptor) return bitmapDescriptor;

            bitmapDescriptor = BitmapDescriptorFactory.fromResource(pinRes);

            // add to cache if necessary
            if (cacheablePin(pinRes)) mPinCache.put(pinRes, bitmapDescriptor);

            return bitmapDescriptor;
        } catch (Exception e) {
            L.e("createVmMapPinBitmapDescriptor:" + e.toString() + "," + Utils.getStackTrace(e));
        }

        return null;
    }

    private static boolean cacheablePin(int pinRes) {
        boolean isCacheable;
        switch (pinRes) {
            case R.drawable.map_pin_station:
            case R.drawable.map_pin_service:
                isCacheable = true;
                break;
            default:
                isCacheable = false;
        }
        return isCacheable;
    }
}
