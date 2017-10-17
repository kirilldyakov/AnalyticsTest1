package com.tests4522.test1;

import android.app.Application;
import android.content.Context;



public class TestsApp extends Application {

    public static String APP_PREFERENCES = "APP_PREFERENCES";
    public static String KEY_REFERRER = "KEY_REFERRER";
    public static String ERROR = "ERROR";

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }
}
