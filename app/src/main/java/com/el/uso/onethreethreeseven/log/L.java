package com.el.uso.onethreethreeseven.log;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.el.uso.onethreethreeseven.BuildFlags.ENABLE_LOGS;

public class L {
    private static final String TAG = "1337";

    public static void d(String msg) {
        if (!ENABLE_LOGS) return;
        Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (!ENABLE_LOGS) return;
        Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (!ENABLE_LOGS) return;
        Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (!ENABLE_LOGS) return;
        Log.e(TAG, msg);
    }

    // For temporary checking code, should not be presented in actual code.
    public static void t(String msg) {
        if (!ENABLE_LOGS) return;

        w(msg);
    }

    public static void stackTrace(String tag, Throwable exception) {
        if (!ENABLE_LOGS) return;

        String log;
        try {
            StringWriter logText = new StringWriter();
            exception.printStackTrace(new PrintWriter(logText));
            log = String.format("[%s]: %s", tag, logText.toString());
        } catch (Exception ex) {
            log = String.format("[%s]: %s", tag, "error writing stacktrace...");
        }
        Log.e(TAG, log);
    }

    public static void stackTrace(Thread thread, Throwable exception) {
        if (!ENABLE_LOGS) return;

        String logPrefix = String.format("1337 crash:%s:%s: ", thread.getId(), thread.getName());
        stackTrace(logPrefix, exception);
    }

    public static void stackTrace(String tag, Thread thread) {
        if (!ENABLE_LOGS) return;

        StackTraceElement[] elements = thread.getStackTrace();

        if (null == elements || 0 == elements.length) return;

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : elements) {
            sb.append(element.toString());
            sb.append("\n\t >>>>: ");
        }
        String log = String.format("[%s]: %s", tag, sb.toString());
        Log.w(TAG, log);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    public static void d(String tag, String msg) {
        if (!ENABLE_LOGS) return;
        Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (!ENABLE_LOGS) return;
        Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (!ENABLE_LOGS) return;
        Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!ENABLE_LOGS) return;
        Log.e(tag, msg);
    }
}
