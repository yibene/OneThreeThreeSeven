package com.el.uso.onethreethreeseven.web.model;

import com.el.uso.onethreethreeseven.helper.JsonUtils;
import com.el.uso.onethreethreeseven.model.ServiceCenterData;
import com.el.uso.onethreethreeseven.model.StationData;
import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;

public class GetStationListOutData {

    @Expose
    private StationData[] StationList;
    @Expose
    private ServiceCenterData[] ServiceCenterList;
    @Expose
    private byte[] CheckSum;

    @Expose
    public DateTime CachedTime;

    public StationData[] getStationList() {
        return StationList;
    }

    public ServiceCenterData[] getServiceCenterList() {
        return ServiceCenterList;
    }

    public byte[] getCheckSum() {
        return CheckSum;
    }

    public static GetStationListOutData fromJson(String json) {
        return JsonUtils.deserialize(json, GetStationListOutData.class);
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }
}
