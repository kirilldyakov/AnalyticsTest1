package com.tests4522.test1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import static com.tests4522.test1.TestsApp.APP_PREFERENCES;
import static com.tests4522.test1.TestsApp.ERROR;
import static com.tests4522.test1.TestsApp.KEY_REFERRER;


public class MainActivity extends AppCompatActivity {
    String TAG = "TAG";

    private WebView webView;
    private TextView tvResults;

    String baseUrl = "http://1.arrrrrr.net/?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
            ((WebView)findViewById(R.id.webView)).restoreState(savedInstanceState.getBundle("webViewState"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));

        }


        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news");
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = token;//getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });


        initViews();

        initSharedPreferencesListener();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        webView.saveState(bundle);
        outState.putBundle("webViewState", bundle);
    }

    private void initSharedPreferencesListener() {

        SharedPreferences sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        sPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                String ref = readSP(KEY_REFERRER);

                if (!ref.equals(ERROR))
                    loadWWW(makeLink(ref));

                log("onChange: " + ref);

            }
        });
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.webView);

        tvResults = (TextView) findViewById(R.id.tvResults);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String referrer = readSP(KEY_REFERRER);

        //if (!referrer.equals(ERROR))
            loadWWW(makeLink(referrer));

        log("onStart: " + referrer);

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void loadWWW(final String url) {

        webView.getSettings().setJavaScriptEnabled(true);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);

        try {

            webView.loadUrl(url);

            webView.setWebViewClient(new WebViewClient() {
                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, int errorCode
                        , String description, String failingUrl) {

                    super.onReceivedError(view, errorCode, description, failingUrl);

                    webView.loadData(getString(R.string.WebViewNoConnectionPage)
                            , "text/html; charset=utf-8"
                            , "utf-8");
                }
            });

        } catch (Exception e) {

            log(e.getMessage());

        }

    }


    private String readSP(String key) {

        SharedPreferences sPref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        return sPref.getString(key, ERROR);

    }

    String makeLink(String referer) {

        String[] arr = {"empty", "empty", "empty"};
        String[] arrSplits = referer.split("_");

        for (int i = 0; i < arrSplits.length; i++) {
            arr[i] = arrSplits[i];
        }

        String url = baseUrl;


        for (int i = 0; i < arr.length; i++) {    //arrSplits.length

            if (i > 0) url = url + "&s" + (i + 1) + "=";

            url = url + arr[i];
        }

        log(url);

        return url;
    }


    void log(String l) {
        tvResults.append(l + "\n");
    }

}

