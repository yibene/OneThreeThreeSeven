package com.el.uso.onethreethreeseven.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.el.uso.onethreethreeseven.BuildFlags;
import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.log.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static String byteArrayToBase64(byte[] byteArray) {
        return Base64.encodeToString(byteArray, 0, byteArray.length, Base64.DEFAULT);
    }

    public static byte[] base64ToByteArray(String base64) {
        byte[] byteArray = null;
        try {
            byteArray = Base64.decode(base64, Base64.DEFAULT);
        } catch (Exception ex) {
            L.e("Failed to decode Base64 string to byte[]\n" + ex.toString());
        }
        return byteArray;
    }

//    public static boolean checkPermission(Context ctx, String permission, final RequestPermissionRes res, final int permission_code) {
//        return checkPermission(ctx, new String[]{permission}, res, permission_code);
//    }
//
//    public static boolean checkPermission(Context ctx, String[] permissions, final RequestPermissionRes res, final int permission_code) {
//
//        List<String> permissionList = new ArrayList<>();
//        for (String permission : permissions) {
//            if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(permission);
//            }
//        }
//        if (permissionList.size() == 0) return true;
//
//        final MainActivity act = Config.inst().getMainActivity();
//
//        if (act == null) {
//            L.e(" checkPermission skip(act is null), permission=" + permissionList);
//            return false;
//        }
//
//        L.d("checkPermission: not granted, so call requestPermissions(),  permission:" + permissionList);
//
//        act.regRequestPermissionsResultCallBk(new MainActivity.RequestPermissionsResult() {
//            @Override
//            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//                if (BuildFlags.ENABLE_LOGS) {
//                    L.d("checkPermission.onRequestPermissionsResult:" + requestCode
//                            + ",permissions:" + (permissions == null? null : Arrays.toString(permissions))
//                            + ",grantResults:" + (grantResults == null? null : Arrays.toString(grantResults))
//                    );
//                }
//
//                if (requestCode == permission_code) {
//                    if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        res.Success();
//                    } else {
//                        res.Fail();
//                    }
//                }
//                act.unregRequestPermissionsResultCallBk(this);
//            }
//        });
//        String[] requestPermissions = new String[permissionList.size()];
//        ActivityCompat.requestPermissions(act, permissionList.toArray(requestPermissions), permission_code);
//        return false;
//    }
}
