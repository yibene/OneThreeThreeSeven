package com.el.uso.onethreethreeseven.model;

import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;

import java.util.UUID;

public class StationData extends BaseData {

    public static final byte TYPE_7_ELEVEN = 0x11;
    public static final byte TYPE_FAMILY_MART = 0x12;
    public static final byte TYPE_OK = 0x13;
    public static final byte TYPE_HI_LIFE = 0x14;
    public static final byte TYPE_CPC = 0x21;
    public static final byte TYPE_FPCC = 0x22;
    public static final byte TYPE_NPCGAS = 0x23;

    @Expose
    private int LocState; // only available for vm explorer mode
    @Expose
    private boolean IsBooked;
    @Expose
    private DateTime BookExpiredTime;
    @Expose
    private DateTime bookExpiredTime;
    @Expose
    private String RId;
    @Expose
    private String DisplayName;
    @Expose
    private String LocName;

    public int getState() {
        return LocState;
    }

    public void setState(int state) {
        LocState = state;
    }

    public boolean isBooked() {
        return IsBooked;
    }

    public void setIsBook(boolean flag) {
        IsBooked = flag;
    }

    public DateTime getBookExpiredTime() {
        return BookExpiredTime;
    }

    public void setBookExpiredTime(DateTime time) {
        BookExpiredTime = time;
    }

    public String getRId() {
        return RId;
    }

    public void setRId(String id) {
        RId = id;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String name) {
        LocName = name;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String name) {
        DisplayName = name;
    }

}
