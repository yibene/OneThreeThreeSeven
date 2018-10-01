package com.el.uso.onethreethreeseven.model;

import com.el.uso.onethreethreeseven.helper.JsonUtils;
import com.google.gson.annotations.Expose;

public class GeoPointData {

    @Expose
    private double Latitude;
    @Expose
    private double Longitude;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public GeoPointData(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public static GeoPointData fromJson(String json) {
        return JsonUtils.deserialize(json, GeoPointData.class);
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }
}
