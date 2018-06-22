package com.shardatech.shardauniversity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import databaseTable.HomeActivityCategoryTable;
import databaseTable.SubCategoryTable;
import method.Constant;

/**
 * Created by sharda on 9/27/2017.
 */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    String getCategoryColor,getCategoryName;
    int getSubCategoryId;
    int getUserId;
    int getCollageId;
    int getUserLoginType;
    String getSubCategoryCode;
    Toolbar toolbar;
    private ContentLoadingProgressBar contentLoadingWebView;

    String url;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        getSubCategoryId       = getIntent().getIntExtra(SubCategoryTable.SUB_CATEGORY_Id,0);
        getSubCategoryCode     = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_CAT_CODE);
        getUserId              = getIntent().getIntExtra(Constant.USER_ID,0);
        getCollageId           = getIntent().getIntExtra(Constant.USER_COLLAGE_ID,0);
        getUserLoginType       = getIntent().getIntExtra(Constant.USER_LOGIN_TYPE,0);
        getCategoryColor       = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);
        getCategoryName        = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_NAME);
        toolbar                = (Toolbar) findViewById(R.id.toolbar);

        contentLoadingWebView  = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingWebView);

        contentLoadingWebView.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getCategoryName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getCategoryColor)));

        webViewJsonLoad();

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    public void webViewJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.SUB_SUB_CATEGORY_URL
                +"?"+SubCategoryTable.SUB_CATEGORY_Id+"="+getSubCategoryId
                +"&"+SubCategoryTable.SUB_CATEGORY_CAT_CODE+"="+getSubCategoryCode)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    WebViewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WebViewActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    final String url = jsonObject.optString("url");
                    WebViewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            contentLoadingWebView.setVisibility(View.GONE);
                            webView.setWebViewClient(new WebViewClient());
                            webView.loadUrl(String.valueOf(url));
                            webView.setDownloadListener(new DownloadListener() {
                                public void onDownloadStart(String url, String userAgent,
                                                            String contentDisposition, String mimetype,
                                                            long contentLength) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            });
                            /* @Override
                                public void onDownloadStart(String url, String userAgent,
                                                            String contentDisposition, String mimetype,
                                                            long contentLength) {
                                    DownloadManager.Request request = new DownloadManager.Request(
                                            Uri.parse(url));

                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Downloads");
                                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    dm.enqueue(request);
                                    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                                            Toast.LENGTH_LONG).show();

                                }
                            });*/

                        }
                    });
                } catch (JSONException paramResponse) {
                    WebViewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(WebViewActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
