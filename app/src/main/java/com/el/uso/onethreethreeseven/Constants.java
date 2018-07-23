package com.el.uso.onethreethreeseven;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class Constants {

    public static final String DialogTag = "Dialog";
    public static final String TipsTag = "Tips";
    public static final String ServiceHostDev = "https://mobile-dev.gogoroapp.com:443/WebService/";
    public static final String ServiceHostPA = "https://mobile-pa.gogoroapp.com:443/WebService/";
    public static final String ServiceHostDemo = "https://mobile-demo.gogoroapp.com:443/WebService/";
    public static final String ServiceHostPro = "https://mobile-pro.gogoroapp.com:443/WebService/";
    public static final String URL_SIGN_UP = "https://my.gogoro.com/tw/account/sign-up";
    public static final String URL_FORGET_PASSWORD = "https://my.gogoro.com/tw/account/forget-password";
    public static final String URL_EDIT_PROFILE = "https://my.gogoro.com/tw/account/edit";

    // map internal 10.0.1.35:80
    public static final String LogServerHost = "https://alarm.gogoroapp.com/LogNotifyService/Web/NotifyItems";

    public static String GcmSenderId;

    public static final LatLng CenterOfTaiwan;

    ////////////////////////////////////////////////////////////////////////////
    // constants for demo mode
    public static final UUID ScooterId = UUID.fromString("526A4B9D-94A0-4728-BB84-666095CD6BAB");
    public static final LatLng Headquarter;
    public static final LatLng Destination;
    public static final LatLng Lot21GeoParking;
    public static final LatLng Lot21Geo;
    public static final LatLng WynnGeo;
    public static final LatLng WynnGeoParking;

    //public static final int NOTIFICATION_ID_BT_CONNECTED = 1;
    public static final int NOTIFICATION_ID_WARNINGS = 2;
    public static final int NOTIFICATION_ID_ERRORS = 3;
    public static final int NOTIFICATION_ID_RESERVED_BATTERY = 4;
    public static final int NOTIFICATION_ID_ONSALE = 5;

    public static final int NOTIFICATION_ID_HC_ERRORS = 6;
    public static final int NOTIFICATION_ID_HC_USER_WARNINGS = 7;
    public static final int NOTIFICATION_ID_HC_FOTA = 8;


    public static final int NOTIFICATION_ID_BATTERY_LOW = 9;


    public static final int NOTIFICATION_ID_FOTA_SUCCESS = 10;

    public static final int NOTIFICATION_ID_FOTA_CARD = 11;

    public static final int NOTIFICATION_ID_GCM_PUSHMESSAGE = 12;

    public static final int NOTIFICATION_ID_GCM_PUSHMESSAGE_EX = 13;

    public static final int NOTIFICATION_ID_SECURITY_BOOST = 14;

    public static final int NOTIFICATION_ID_TOP_INDEX = 15; // no use for enum

    public static final int NOTIFICATION_ID_OBC_WARNINGS = 16;

    public static final int NOTIFICATION_ID_OBC_ERRORS = 17;

    public static final int NOTIFICATION_ID_OBC_SUCCESS = 18;

    public static final int NOTIFICATION_ID_OBC_CHARGE_FULL = 19;



    //public static final int NOTIFICATION_ID_POST_FACEBOOK = 6;

    public static final int DASHBOARD_COLOR_LEFT_HUE_INDEX = 4;
    public static final int DASHBOARD_COLOR_RIGHT_HUE_INDEX = 5;

    public static final int HANDLER_ID_TASK_RESULT_CALLBACK = 0x3001;

    public static final int TASK_RUN_NO_RUNNING = 0;
    public static final int TASK_RUN_ID_REMOVE_NEW_FLAG = 1;
    public static final int TASK_RUN_SYNC_BADGE = 2;
    public static final int TASK_RUN_GET_DB_BADGES = 3;
    public static final int TASK_RUN_CARD_INIT = 4;
    public static final int TASK_RUN_CARD_REFRESH = 5;
    public static final int TASK_RUN_CARD_SAVE = 6;
    public static final int TASK_RUN_SYNC_PATTERNS = 7;
    @Deprecated
    public static final int TASK_RUN_REG_GCM = 8; //obsolete 16.06.28
    public static final int TASK_RUN_AD_LOAD_DB = 9;
    public static final int TASK_RUN_AD_REFRESH_LIST = 10;
    public static final int TASK_RUN_AD_UPDATE_LIST = 11;
    public static final int TASK_RUN_AD_DECODE_BITMAP = 12;
    public static final int TASK_RUN_AD_DOWNLOAD = 13;
    public static final int TASK_RUN_DECODE_BITMAP = 14;
    public static final int TASK_RUN_SYNC_FEATURES = 15;

    public static final int TASK_RUN_LOGIN_TASK = 1001;
    public static final int TASK_RUN_GET_AD_TASK = 1002;
    public static final int TASK_RUN_GET_GOSTATION_TASK = 1003;
    public static final int TASK_RUN_GET_HC_LIST_TASK = 1004;
    public static final int TASK_RUN_GET_MAP_ROUTE_TASK = 1005;
    public static final int TASK_RUN_GET_NEARBY_BATTERY_TASK = 1006;
    public static final int TASK_RUN_RESERVE_BATTERY_TASK = 1007;
    public static final int TASK_RUN_GET_LIGHT_PATTERNS_TASK = 1008;
    public static final int TASK_RUN_GET_SOUND_PATTERNS_TASK = 1009;
    public static final int TASK_RUN_GET_RIDING_PROFILE_TASK = 1010;
    public static final int TASK_RUN_GET_SCOOTER_FAULT_TASK = 1011;
    public static final int TASK_RUN_GET_SCOOTER_LIST_TASK = 1012;
    public static final int TASK_RUN_GET_SCOOTER_SETTINGS_TASK = 1013;
    @Deprecated
    public static final int TASK_RUN_REG_GCM_TASK = 1014; //obsolete 16.06.28
    public static final int TASK_RUN_SET_FB_ID_TASK = 1015;
    public static final int TASK_RUN_SET_SCOOTER_FAULT_TASK = 1016;
    public static final int TASK_RUN_SET_SCOOTER_INFO_TASK = 1017;
    public static final int TASK_RUN_SET_SCOOTER_SETTINGS_TASK = 1018;
    public static final int TASK_RUN_SET_SCOOTER_TRACK_TASK = 1019;
    @Deprecated
    public static final int TASK_RUN_UNREG_GCM_TASK = 1020; //obsolete 16.06.28

    public static final int TASK_RUN_VISIT_AD_TASK = 1021;
    public static final int TASK_RUN_GET_USER_PROFILE_TASK = 1022;
    public static final int TASK_RUN_GET_MAP_ALL_DATA = 1023;
    public static final int TASK_RUN_MAP_QUERY_ADDRESS = 1024;
    public static final int TASK_RUN_CANCEL_RESERVATION = 1025;
    public static final int TASK_RUN_GET_BATTERY_COUNT = 1026;
    public static final int TASK_RUN_GET_BATTERY_STATUS_ONHC = 1027;
    public static final int TASK_RUN_GET_ALL_VM_AND_REFRESH = 1028;
    public static final int TASK_RUN_REG_GCM_TASK_V2 = 1029;
    public static final int TASK_RUN_UNREG_GCM_TASK_V2 = 1030;
    public static final int TASK_RUN_GET_SCOOTER_TIRE_LIST = 1031;
    public static final int TASK_RUN_GET_MAP_AUTO_REFRESH_DATA = 1032;
    public static final int TASK_RUN_SET_TRIP_STATISTICS_TASK = 1033;
    public static final int TASK_RUN_LOAD_MAP_AUTO_REFRESH_DATA = 1034;
    public static final int TASK_RUN_GET_MAP_DATA_TASK = 1035;
    public static final int TASK_RUN_GET_ERRORS_MAPPING = 1036;
    public static final int TASK_RUN_GET_SC_RECOMMEND_TASK = 1037;
    public static final int TASK_RUN_GET_APP_FORCE_UPDATE_TASK = 1038;


    public static final int UPDATE_MOUNT_DATA_STATISTIC_DATA_INTERVAL = 30;


    public static final String GOGORO_SOS_CALL_NUMBER = "0800365996";
    public static final String GOGORO_SERVICE_CENTER = "0800365996";


    public static final UUID NO_CUSTOMIZE_SCOOTER_MODEL_ID = UUID.fromString("0fc29afc-52c1-4e58-b79b-bad9e2dcfd41");

    static {
        CenterOfTaiwan = new LatLng(23.97565, 120.973882);
        Headquarter = new LatLng(25.04244, 121.36236);
        //Headquarter = new LatLng(-6.174421, 106.821788); //indonedia
        Destination = new LatLng(24.981495, 121.540985);
        Lot21GeoParking = new LatLng(37.75987, -122.39051);
        Lot21Geo = new LatLng(37.760215, -122.390721);
        WynnGeo = new LatLng(36.126812, -115.165919);
        WynnGeoParking = new LatLng(36.128222, -115.166269);
    }

    public static final boolean DASHBOARD_FAKE_GRAPH_ONLY = false;

    public static final int SC_RESERVATION_ARROW_DOWN_ALPHA = 51;

    public static final int SC_TIME_SLOT_QUERY_INTERVAL = 5000;
}
