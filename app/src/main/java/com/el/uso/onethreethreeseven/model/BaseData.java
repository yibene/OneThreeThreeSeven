package com.el.uso.onethreethreeseven.model;

import com.google.gson.annotations.Expose;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseData {

    @Expose
    protected UUID Id; // only available for battery explorer mode
    @Expose
    protected GeoPointData GeoPoint;
    @Expose
    protected String ZipCode;
    @Expose
    protected String City;
    @Expose
    protected String District;
    @Expose
    protected String Address;
    @Expose
    protected DateTime UpdateTime;
    @Expose
    protected byte[] AvailableTime;
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

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public GeoPointData getGeoPointData() {
        return GeoPoint;
    }

    public void setGeoPointData(GeoPointData gpd) {
        GeoPoint = gpd;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public DateTime getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        UpdateTime = updateTime;
    }

    public byte[] getAvailableTime() {
        return AvailableTime;
    }

}
