package com.el.uso.onethreethreeseven.map;

import android.support.annotation.NonNull;

import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.model.ServiceCenterData;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class ServiceCenterMarker extends BaseMarker {

    private ServiceCenterData mServiceCenterData;

    public ServiceCenterMarker(double lat, double lng) {
        super(MarkerType.SERVICE_CENTER, lat, lng);
    }

    public ServiceCenterMarker(ServiceCenterData data) {
        super(MarkerType.SERVICE_CENTER);
        mId = data.getId().toString();
        mLatitude = data.getGeoPointData().getLatitude();
        mLongitude = data.getGeoPointData().getLongitude();
        mServiceCenterData = data;
        mName = mServiceCenterData.getName();
        mMarkerIcon = R.drawable.map_pin_service;
    }

    public ServiceCenterMarker(LatLng latLng, String name, int iconResource) {
        this(latLng.latitude, latLng.longitude, name, iconResource);
    }

    public ServiceCenterMarker(double lat, double lng, String name, int iconResource) {
        super(MarkerType.STATION, lat, lng);
        mName = name;
        mMarkerIcon = iconResource;
    }

    protected
    @NonNull
    String getSortKey() {
        return String.format(Locale.US, "%02d%s%s%s", TAIWAN_CITIES.indexOf(mServiceCenterData.getCity()), mServiceCenterData.getCity(), mServiceCenterData.getDistrict(), mServiceCenterData.getAddress());
    }

    @Override
    public int getMarkerIcon(boolean isClustered) {
        return (isClustered) ? R.drawable.map_pin_service_selected : mMarkerIcon;
    }
}
