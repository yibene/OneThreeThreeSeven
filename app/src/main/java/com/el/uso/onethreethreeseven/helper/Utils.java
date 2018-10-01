package com.el.uso.onethreethreeseven.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.el.uso.onethreethreeseven.BuildFlags;
import com.el.uso.onethreethreeseven.MainActivity;
import com.el.uso.onethreethreeseven.R;
import com.el.uso.onethreethreeseven.log.L;
import com.el.uso.onethreethreeseven.map.BaseMarker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

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

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void writeToRoot(String str) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath());
        File file = new File(dir, "list.json");
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            try {
                pw.print(str);
            } finally {
                pw.close();
            }
        } catch (FileNotFoundException e) {
            L.e("write to root: file not found");
        }
    }

    public static String readFromRoot(Context context, String fileName) {

    }

    public static String readFromRaw(Context context, int rawResource) {
        InputStream inputStream = context.getResources().openRawResource(rawResource);
        return new Scanner(inputStream).next();
    }

    public static List<BaseMarker> fromRawJson(InputStream inputStream) throws JSONException {
        List<BaseMarker> items = new ArrayList<>();
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            String title = null;
            String snippet = null;
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            if (!object.isNull("title")) {
                title = object.getString("title");
            }
            if (!object.isNull("snippet")) {
                snippet = object.getString("snippet");
            }
            items.add(new BaseMarker(lat, lng, title, snippet));
        }
        return items;
    }

    public static String getCurrentLocaleString(String json) {
        String locale = Locale.getDefault().toString().replace("_", "-");
        String result = json;
        String enResult = null;
        JSONArray jsonArray;
        try {
            jsonArray = new JSONObject(json).getJSONArray("List");

            boolean found = false;
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String lan = obj.getString("Lang");
                if (locale.startsWith(lan)) {
                    found = true;
                    result = obj.getString("Value");
                    break;
                } else if ("en-US".equals(obj.getString("Lang"))) {
                    enResult = obj.getString("Value");
                }
            }

            if (!found) {
                result = enResult;
            }
        } catch (Exception e) {
        }

        return result;
    }
}
