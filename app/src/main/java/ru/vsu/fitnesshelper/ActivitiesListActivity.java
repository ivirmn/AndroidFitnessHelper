package ru.vsu.fitnesshelper;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import android.webkit.WebSettings;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitiesListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);
        WebView webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);


        webView.loadUrl("file:///android_asset/activity_web_view.html");


        webView.setWebViewClient(new WebViewClient());
    }
}
