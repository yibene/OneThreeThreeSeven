package com.el.uso.onethreethreeseven;

import android.app.Application;
import android.content.Context;

import com.el.uso.onethreethreeseven.helper.Config;

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = this.getApplicationContext();

        Config.inst().init(context);
    }
}