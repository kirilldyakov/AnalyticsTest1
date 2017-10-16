package com.tests4522.test1;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by user on 16.10.17.
 */



//@ReportsCrashes(
//        httpMethod = HttpSender.Method.PUT,
//        reportType = HttpSender.Type.JSON,
//        formUri = "http://31.31.203.186:5984/acra-benzin/_design/acra-storage/_update/report",
//        formUriBasicAuthLogin = "rep01_asdfASDfaseRfw234rwerwa",
//        formUriBasicAuthPassword = "35erWET34terw5ertdsfgdsEWrterttwertertERTzIo8",
//        // Your usual ACRA configuration
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.crash_toast_text)

@ReportsCrashes(mailTo = "Dyakovks@gmail.com",
        customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)

public class TestsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA

        ACRA.init(this);
    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }
}
