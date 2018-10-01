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
    private UUID LocId; // only available for vm explorer mode
    @Expose
    private int LocState; // only available for vm explorer mode
    @Expose
    private boolean IsBooked;
    @Expose
    private DateTime BookExpiredTime;
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

    public static String getTypeName(byte[] type) {
        int brand = type[3] & 0xFF;

        switch (brand) {
            case TYPE_7_ELEVEN: {
                return "7-Eleven";
            }
            case TYPE_FAMILY_MART: {
                return "Family Mart";
            }
            case TYPE_OK: {
                return "Circle K";
            }
            case TYPE_HI_LIFE: {
                return "HiLife";
            }
            case TYPE_CPC: {
                return "CPC";
            }
            case TYPE_FPCC: {
                return "FPCC";
            }
            case TYPE_NPCGAS: {
                return "NPCGAS";
            }
            default: {
                return "";
            }
        }
    }

    public static int getTypeIcon(byte[] type) {
        /*
		int brand = type[3] & 0xFF;

		switch (brand) {
		case TYPE_7_ELEVEN: {
			return R.drawable.ic_node_type_7_11;
		}
		case TYPE_FAMILY_MART: {
			return R.drawable.ic_node_type_family_mart;
		}
		case TYPE_OK: {
			return R.drawable.ic_node_type_ok;
		}
		case TYPE_HI_LIFE: {
			return R.drawable.ic_node_type_hi_life;
		}
		case TYPE_CPC: {
			return R.drawable.ic_node_type_cpc;
		}
		case TYPE_FPCC: {
			return R.drawable.ic_node_type_fpcc;
		}
		case TYPE_NPCGAS: {
			return R.drawable.ic_node_type_npcgas;
		}
		default: {
			return R.drawable.ic_node_type_def;
		}
		}
		*/
        return 0;
    }

    public String getTypeName() {
        return getTypeName(Type);
    }

    public int getTypeIcon() {
        return getTypeIcon(Type);
    }
}
