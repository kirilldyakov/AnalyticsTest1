package com.tests4522.test1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

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

