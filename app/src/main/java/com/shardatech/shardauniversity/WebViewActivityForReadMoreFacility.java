package com.shardatech.shardauniversity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import databaseTable.HomeActivityCategoryTable;

/**
 * Created by sharda on 9/27/2017.
 */

public class WebViewActivityForReadMoreFacility extends AppCompatActivity {
    private WebView webView;
    String getCategoryColor,getCategoryName;
    Toolbar toolbar;
    private ContentLoadingProgressBar contentLoadingWebView;

    String url;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        getCategoryColor       = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);
        getCategoryName        = getIntent().getStringExtra("facility_name");
        url                    = getIntent().getStringExtra("facility_web_url");
        toolbar                = (Toolbar) findViewById(R.id.toolbar);

        contentLoadingWebView  = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingWebView);

//        contentLoadingWebView.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getCategoryName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getCategoryColor)));

//        webViewJsonLoad();

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    public void webViewJsonLoad()
    {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
