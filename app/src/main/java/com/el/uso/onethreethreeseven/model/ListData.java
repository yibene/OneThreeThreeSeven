package com.el.uso.onethreethreeseven.model;

import com.el.uso.onethreethreeseven.helper.JsonUtils;
import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;

import java.util.UUID;

public class ListData {

    @Expose
    private StationData[] StationList;
    @Expose
    private ServiceCenterData[] ServiceCenterList;

    public StationData[] getStationList() {
        return StationList;
    }

    public void setStationList(StationData[] list) {
        StationList = list;
    }

    public ServiceCenterData[] getServiceCenterList() {
        return ServiceCenterList;
    }

    public void setServiceCenterList(ServiceCenterData[] list) {
        ServiceCenterList = list;
    }

    public static ListData fromJson(String json) {
        return JsonUtils.deserialize(json, ListData.class);
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }
}
