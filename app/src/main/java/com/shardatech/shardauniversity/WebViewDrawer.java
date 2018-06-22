package com.shardatech.shardauniversity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import databaseTable.DrawableTable;

/**
 * Created by sharda on 9/27/2017.
 */

public class WebViewDrawer extends AppCompatActivity {
    private WebView webView;
    String getDrawerColor,getDrawerName;
    String getDrawerPathUrl;
    Toolbar toolbar;
    private ContentLoadingProgressBar contentLoadingWebView;

    String url;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        getDrawerName          = getIntent().getStringExtra(DrawableTable.DRAWABLE_NAME);
        getDrawerColor         = getIntent().getStringExtra(DrawableTable.DRAWABLE_COLOR_CODE);
        getDrawerPathUrl       = getIntent().getStringExtra(DrawableTable.DRAWABLE_PATH_URL);
        toolbar                = (Toolbar) findViewById(R.id.toolbar);

        contentLoadingWebView  = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingWebView);

        contentLoadingWebView.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getDrawerName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getDrawerColor)));


        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webViewJsonLoad();
    }

    public void webViewJsonLoad()
    {
                WebViewDrawer.this.runOnUiThread(new Runnable() {
                        public void run() {
                            contentLoadingWebView.setVisibility(View.GONE);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(getDrawerPathUrl);
                        }
                    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
