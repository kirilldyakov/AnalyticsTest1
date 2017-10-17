package com.tests4522.test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Tracker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) {
            String referrer = "";

            Bundle extras = intent.getExtras();
            if (extras != null) {
                referrer = extras.getString("referrer");
            }

            SharedPreferences sPref = context.getSharedPreferences(
                    TestsApp.APP_PREFERENCES, Context.MODE_PRIVATE);

            String sp = sPref.getString(TestsApp.KEY_REFERRER,"notFound");

            if (sp.equals("notFound")) {
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(TestsApp.KEY_REFERRER, referrer);
                ed.apply();
            }

        }
    }

}