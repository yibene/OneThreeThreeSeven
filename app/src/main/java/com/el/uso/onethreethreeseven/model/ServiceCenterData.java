package com.el.uso.onethreethreeseven.model;

import com.el.uso.onethreethreeseven.helper.JsonUtils;
import com.google.gson.annotations.Expose;

public class ServiceCenterData extends BaseData {

    public static final int TYPE_SERVICE_CENTER = 0;
    public static final int TYPE_SERVICE_CENTER_AND_STATION = 1;
    public static final int TYPE_SERVICE_STATION = 2;

    @Expose
    private int State;
    @Expose
    private String Name;
    @Expose
    private String Phone;
    @Expose
    protected byte[] Type;

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public byte[] getType() {
        return Type;
    }

    public void setType(byte[] type) {
        Type = type;
    }

    public static ServiceCenterData fromJson(String json) {
        return JsonUtils.deserialize(json, ServiceCenterData.class);
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }
}
