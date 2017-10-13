package com.tests4522.test1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    String url1 = "http://www.google.com";
    String url2 = "http://www.yandex.ru";
    String url3 = "https://ru.lipsum.com/";

    String server_url =  "http://1.arrrrrr.net/?{utm_campaign}&s2={utm_source}&s3={utm_medium}";

    private WebView webView;
    private TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppsFlyerLib.getInstance().startTracking(this.getApplication(),"LmU9p3PTjhiLT8rMXVRmKS");
        
        webView = (WebView) findViewById(R.id.wvMain);
        tvResults = (TextView) findViewById(R.id.tvResults);


        loadDataFromUrl();
    }

    @Override
    protected void onStart() {
        super.onStart();

        AppsFlyerLib.getInstance().registerConversionListener(this, new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {

                String results = "";

                for (String attrName : map.keySet()) {

                    results = results+ "attribute: " + attrName + " = " + map.get(attrName) +"\n";

                    Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = "+map.get(attrName));
                }

                showReport(results);

            }

            @Override
            public void onInstallConversionFailure(String s) {
                String message = "error getting conversion data: " + s;
                Log.d(AppsFlyerLib.LOG_TAG, message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        });

        loadWWW(url3);

    }

    private void showReport(String rep) {
        tvResults.setText(rep);
    }


    private void loadDataFromUrl() {

    }

    private String makeTargetUrl(String input){
        String utm_campaign="";
        String utm_source ="";
        String utm_medium="";
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
            });

        }
        catch (Exception e) {

            Log.d(TAG, e.getMessage()); //Get the Exception thrown.

        }

////            webView.loadData("<HTML><BODY><H3><p align=\"center\">Возникла ошибки при загрузке страницы</p></H3></BODY></HTML>","text/html; charset=UTF-8",null);
//        String txt = "<body><a href=\"market://details?id=com.google.earth\" target=\"_blank\">Download for Android</a></body>";
//        txt = "<a href=\"tel:8(920)111-2222\">Call</a>";
//        txt = "<a href=\"adcombo:8(920)111-2222\">Adcombo</a>";
//        webView.loadData(txt,"text/html; charset=UTF-8",null);

    }


    public void writeSP(String key, String value) {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, value);

        editor.commit();
    }

    public String readSP(String key){

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        String emptyValue = getResources().getString(R.string.emptyString);

        String result = sharedPref.getString(key, emptyValue);

        return result;
    }
}