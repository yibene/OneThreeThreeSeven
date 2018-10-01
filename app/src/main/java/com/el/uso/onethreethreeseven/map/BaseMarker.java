package com.el.uso.onethreethreeseven.map;

import android.support.annotation.IntDef;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class BaseMarker implements ClusterItem {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            MarkerType.STATION,
            MarkerType.SERVICE_CENTER,
            MarkerType.UNKNOWN
    })

    public @interface MarkerType {
        int UNKNOWN = -1;
        int STATION = 0;
        int SERVICE_CENTER = 1;
    }

    private
    @MarkerType
    int mMarkerType;
    protected String mId;
    protected double mLatitude;
    protected double mLongitude;
    protected String mName;
    protected int mMarkerIcon;
    private String mTitle;
    private String mSnippet;

    protected static final List<String> TAIWAN_CITIES = new ArrayList<>();

    static {
        TAIWAN_CITIES.add("基隆市");
        TAIWAN_CITIES.add("台北市");
        TAIWAN_CITIES.add("新北市");
        TAIWAN_CITIES.add("桃園市");
        TAIWAN_CITIES.add("新竹市");
        TAIWAN_CITIES.add("新竹縣");
        TAIWAN_CITIES.add("苗栗縣");
        TAIWAN_CITIES.add("台中市");
        TAIWAN_CITIES.add("彰化縣");
        TAIWAN_CITIES.add("南投縣");
        TAIWAN_CITIES.add("雲林縣");
        TAIWAN_CITIES.add("嘉義市");
        TAIWAN_CITIES.add("嘉義縣");
        TAIWAN_CITIES.add("台南市");
        TAIWAN_CITIES.add("高雄市");
        TAIWAN_CITIES.add("屏東縣");
        TAIWAN_CITIES.add("台東縣");
        TAIWAN_CITIES.add("花蓮縣");
        TAIWAN_CITIES.add("宜蘭縣");
        TAIWAN_CITIES.add("澎湖縣");
        TAIWAN_CITIES.add("金門縣");
        TAIWAN_CITIES.add("連江縣");
    }

    public BaseMarker(@MarkerType int type) {
        mMarkerType = type;
    }

    public BaseMarker(@MarkerType int type, double lat, double lng) {
        mMarkerType = type;
        mLatitude = lat;
        mLongitude = lng;
    }

    public BaseMarker(double lat, double lng) {
        mMarkerType = MarkerType.UNKNOWN;
        mLatitude = lat;
        mLongitude = lng;
    }

    public BaseMarker(double lat, double lng, String title, String snippet) {
        mMarkerType = MarkerType.UNKNOWN;
        mLatitude = lat;
        mLongitude = lng;
        mTitle = title;
        mSnippet = snippet;
    }

    public
    @MarkerType
    int getMarkerType() {
        return mMarkerType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(mLatitude, mLongitude);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
