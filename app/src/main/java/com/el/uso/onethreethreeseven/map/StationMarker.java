package com.el.uso.onethreethreeseven.map;

import android.support.annotation.NonNull;

import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.model.StationData;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.util.Locale;

public class StationMarker extends BaseMarker {

    private StationData mStationData;
    private int mBatteryCount;
    private DateTime mBatteryCountUpdateTime;

    public StationMarker(double lat, double lng) {
        super(MarkerType.STATION, lat, lng);
    }

    public StationMarker(StationData data) {
        super(MarkerType.STATION);
        mId = data.getId().toString();
        mLatitude = data.getGeoPointData().getLatitude();
        mLongitude = data.getGeoPointData().getLongitude();
        mStationData = data;
        mName = mStationData.getDisplayName();
        mMarkerIcon = R.drawable.map_pin_station;
    }

    public StationMarker(LatLng latLng, String name, int iconResource) {
        this(latLng.latitude, latLng.longitude, name, iconResource);
    }

    public StationMarker(double lat, double lng, String name, int iconResource) {
        super(MarkerType.STATION, lat, lng);
        mName = name;
        mMarkerIcon = iconResource;
    }

    protected
    @NonNull
    String getSortKey() {
        return String.format(Locale.US, "%02d%s%s%s", TAIWAN_CITIES.indexOf(mStationData.getCity()), mStationData.getCity(), mStationData.getDistrict(), mStationData.getAddress());
    }

    public String getRId() {
        return mStationData.getRId();
    }

    public void setRId(String mRId) {
        mStationData.setRId(mRId);
    }

    public int getBatteryCount() {
        return mBatteryCount;
    }

    public void setBatteryCount(int batteryCount) {
        mBatteryCount = batteryCount;
    }

    public DateTime getBatteryCountUpdateTime() {
        return mBatteryCountUpdateTime;
    }

    public void setBatteryCountUpdateTime(DateTime batteryCountDate) {
        this.mBatteryCountUpdateTime = batteryCountDate;
    }

    @Override
    public int getMarkerIcon(boolean isClustered) {
        return (isClustered) ? R.drawable.map_pin_station_selected : mMarkerIcon;
    }
}
