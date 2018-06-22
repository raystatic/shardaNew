package com.shardatech.shardauniversity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import databaseTable.HomeActivityCategoryTable;
import databaseTable.NotificationTable;
import method.Constant;

/**
 * Created by sharda on 9/10/2017.
 */

public class NotificationDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    AppBarLayout appbar;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NestedScrollView nestedScrollView;
    TextView  tvTitle,tvNotiDate,tvDetails,tvViewMore,  tvFacilityDetailsLayoutFacility,tvFacilityDescriptionLayoutFacility;
    RelativeLayout rlFacilityDetailsLayoutFacility;
    String getTitle,getDetails,getImageUrl,getWebViewUrl;
    long getNotificationDate;
    ImageView ivNotifiactionImage;
    private String timing;
    private String noti_share = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_notification);

        coordinatorLayout           = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        collapsingToolbarLayout     = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_facility);
        toolbar                     = (Toolbar) findViewById(R.id.toolbar_layout_facility);
        appbar                      = (AppBarLayout) findViewById(R.id.appbar_layout_facility);
        nestedScrollView            = (NestedScrollView) findViewById(R.id.nested_scroll_view_layout_facility);
        ivNotifiactionImage         = (ImageView) findViewById(R.id.iv_header_facility);

        tvTitle = (TextView) findViewById(R.id.tv_facility_title_layout_facility);
        tvNotiDate = (TextView) findViewById(R.id.tv_facility_date_on_layout_facility);
        tvDetails = (TextView) findViewById(R.id.tv_facility_description_layout_facility);
        tvViewMore = (TextView) findViewById(R.id.tv_view_more);

        getTitle                = getIntent().getStringExtra(NotificationTable.NOTIFICATION_TITLE);
        getDetails              = getIntent().getStringExtra(NotificationTable.NOTIFICATION_DETAILS);
        getNotificationDate     = Long.parseLong((String.valueOf(getIntent().getStringExtra(NotificationTable.NOTIFICATION_DATE))));
        getImageUrl             = getIntent().getStringExtra(NotificationTable.NOTIFICATION_IMAGE_URL);
        getWebViewUrl           = getIntent().getStringExtra(NotificationTable.NOTIFICATION_URL);

        String notificationAddDate = Constant.getDate(getNotificationDate*1000l);
        String notificationDaysAgo = Constant.getDisplayableTime(getNotificationDate);

        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle(getTitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getNotificationDate>0)
            timing = "Timing: " + notificationAddDate;
         else
            timing = "";

        noti_share = "Sharda University: " + getTitle + " at " + getDetails + " \n" +
                 timing +
                "\n" + "\nDownload the Sharda University App !\n" +
                "https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName();

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getTitle); // Careful! There should be a space between double quote. Otherwise it won't work.
                    isShow = false;
                } else if (!isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = true;
                }
            }
        });

        if (getImageUrl.length() > 0) {
            Picasso.with(this)
                    .load(getImageUrl)
                    .placeholder(R.drawable.default_bg_icon)
                    .into(ivNotifiactionImage);
        } else {
            Picasso.with(this)
                    .load(R.drawable.ic_menu_gallery)
                    .placeholder(R.drawable.default_bg_icon)
                    .into(ivNotifiactionImage);
        }

        tvTitle.setText(getTitle);
        tvDetails.setText(getDetails);

        if(getNotificationDate == 0)
            tvNotiDate.setVisibility(View.GONE);
        else
            tvNotiDate.setVisibility(View.VISIBLE);

        tvNotiDate.setText("Event On - "+notificationAddDate);

        if(getWebViewUrl.length()>0)
            tvViewMore.setVisibility(View.VISIBLE);
        else
            tvViewMore.setVisibility(View.GONE);


        tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (NotificationDetailsActivity.this).runOnUiThread(new Runnable() {
                    public void run() {

                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(NotificationDetailsActivity.this, WebViewActivityForReadMoreFacility.class);


                        bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, "#a856aa");
                        bundle.putString("facility_name",getTitle);
                        bundle.putString("facility_web_url",getWebViewUrl);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                    }
                });
            }
        });


        if(!Constant.isConnectingToInternet(NotificationDetailsActivity.this)){
            Snackbar.make(coordinatorLayout,getResources().getString(R.string.aleart_dialog_for_internet_title)+"!  "+
                    (getResources().getString(R.string.aleart_dialog_for_internet_message)), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
               return true;
            case R.id.action_share:
                shareEvent(noti_share);
                break;
        }
        return false;
    }

    private void shareEvent(String noti_share) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, noti_share);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.aleart_dialog_for_internet_title));
        builder.setMessage(getResources().getString(R.string.aleart_dialog_for_internet_message));
        builder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}