package com.tests4522.test1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String TAG = "TAG";

    final String refKey = "referrer";

    final String utm_campaign = "utm_campaign";
    final String utm_source = "utm_source";
    final String utm_medium = "utm_medium";

    String url1 = "http://advancedenglishforprofessionals.com/verbs/images/wait1.gif";
    String url2 = "http://www.yandex.ru";
    String url3 = "https://ru.lipsum.com/";

    //String server_url = "http://1.arrrrrr.net/?{utm_campaign}&s2={utm_source}&s3={utm_medium}";
    String server_url = "https://yandex.ru/search/?text=";

    private WebView webView;
    private TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        AppsFlyerLib.getInstance().startTracking(this.getApplication(), getString(R.string.AppsFlyerKey));

//        AppsFlyerLib.getInstance().setDebugLog(true);

//        AppsFlyerLib.getInstance().startTracking(this.getApplication());

        AppsFlyerLib.getInstance().setMinTimeBetweenSessions(10);

        webView = (WebView) findViewById(R.id.wvBrowser);

        tvResults = (TextView) findViewById(R.id.tvResults);


    }

    @Override
    protected void onStart() {
        super.onStart();

        initAppsFlyerListener();

       // loadWWW(server_url + readSP(utm_campaign));

    }

    private void initAppsFlyerListener() {

        AppsFlyerLib.getInstance().registerConversionListener(this, new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {

                printAllAttributes(map);


                    //if (readSP(utm_campaign).equalsIgnoreCase(getString(R.string.emptyString))) {

                        String refValue = map.get(refKey);

                        Toast.makeText(getApplicationContext(), refKey + " " + refValue, Toast.LENGTH_LONG).show();

                        parseAndWriteRefAttrs(refValue);




                loadWWW(server_url + readSP(utm_campaign));


            }

            @Override
            public void onInstallConversionFailure(String s) {

                String message = "error getting conversion data: " + s;

                Log.d(AppsFlyerLib.LOG_TAG, message);

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                Log.d(TAG, "onAppOpenAttribution: ");
            }

            @Override
            public void onAttributionFailure(String s) {

                Log.d(TAG, "onAttributionFailure: ");
            }
        });
    }

    private void parseAndWriteRefAttrs(String refValue) {
        String[] attrs = refValue.split("_");
        tvResults.append("refValue:|"+refValue+"|");
        writeSP(utm_campaign, refValue);

        if (attrs.length == 3) {

            writeSP(utm_campaign, attrs[0]);

            writeSP(utm_source, attrs[1]);

            writeSP(utm_medium, attrs[2]);

        }
    }

    private void printAllAttributes(Map<String, String> map) {
        String results = "";

        for (String attrName : map.keySet()) {

            results = results + "attribute: " + attrName + " = " + map.get(attrName) + "\n";

        }

        tvResults.append(results);

    }


    private String makeTargetUrl(String input) {
        String utm_campaign = "";
        String utm_source = "";
        String utm_medium = "";
        return utm_campaign;
    }

    private void loadWWW(final String url) {

        webView.getSettings().setJavaScriptEnabled(true);

        try {

            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                    viewx.loadUrl(urlx);
                    return false;
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
                    webView.loadData("<html><body><div align=\"center\"><FONT SIZE=\"5\">Возникла ошибка подключения</FONT></div></body></html>", "text/html; charset=utf-8", "utf-8");
                }
            });

        } catch (Exception e) {


            Log.d(TAG, e.getMessage()); //Get the Exception thrown.

        }

    }

    void writeSP(String key, String value) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(key, value);
        ed.commit();
    }

    String readSP(String key) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(key, "EMPTY");
        return savedText;
    }

    public void log(String l){

    }
}

