package com.shardatech.shardauniversity;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import adapter.GalleryFacilityAdapter;
import database.ApplicationContentProvider;
import databaseTable.DrawableTable;
import databaseTable.GenericFacilityActivityTable;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.NotificationTable;
import databaseTable.SubCategoryTable;
import fragments.SlideshowDialogFragment;
import method.Constant;

/**
 * Created by sharda on 9/10/2017.
 */

public class GenericFacilityActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private View mFab;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private ContentLoadingProgressBar contentLoadingProgressBarFacility;
    Toolbar toolbar;
    AppBarLayout appbar;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private  JSONObject jsonObject;
    ImageView ivHeaderFacility;
    NestedScrollView nestedScrollView;
    LinearLayout llCardView,rlInflateNotificationFacilityLayout;
    RelativeLayout  rlMainCardView,rlLocationLayoutFacility,rlPhoneLayoutFacility,rlCloseTimeLayoutFacility,rlNotifictionLayoutFacility,
                    rlFacilityDetailsLayoutFacility,rlFacilityGalleryLayoutFacility;
    CardView    cardViewAboutFacility;
    TextView    tvFacilityName,tvLocAdd,tvGetDirection,tvPhone,tvCloseTime,tvPrise,tvNotificationFacilityName,
                tvFacilityDetailsLayoutFacility,tvFacilityDescriptionLayoutFacility,tvFacilityGalleryLayoutFacility,
                tvViewAllGalleryLayoutFacility,tvViewAllNotificationFacility,tvFacilityDownloadPdf,tvFacilityWebView;
    ImageView ivLocation,ivPhone,ivCloseTime,tvCallNow,tvEnquiryNow;

    RecyclerView rvGalleryLayoutFacility;

    String getCategoryColor,getCategoryName;
    int getSubCategoryId;
    int getUserId;
    int getCollageId;
    int getUserLoginType;
    String getSubCategoryCode;
    double lattitude=0.0, longitude=0.0;
    double facilityLong=0.0,facilityLat=0.0;
    private GoogleMap googleMap;
    private RecyclerView.OnItemTouchListener onItemTouchListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_facility);
        coordinatorLayout           = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        collapsingToolbarLayout     = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_facility);
        toolbar                     = (Toolbar) findViewById(R.id.toolbar_layout_facility);
        appbar                      = (AppBarLayout) findViewById(R.id.appbar_layout_facility);
        nestedScrollView            = (NestedScrollView) findViewById(R.id.nested_scroll_view_layout_facility);
        cardViewAboutFacility       = (CardView) findViewById(R.id.cardview_layout_facility);
        contentLoadingProgressBarFacility = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingPBFacility);

        llCardView                          = (LinearLayout) findViewById(R.id.ll_layout_facility);
        rlMainCardView                      = (RelativeLayout) findViewById(R.id.rl_main_address_layout_facility);
        ivHeaderFacility                    = (ImageView) findViewById(R.id.iv_header_facility);

        tvFacilityName                      = (TextView) findViewById(R.id.tv_facility_name);
        rlLocationLayoutFacility            = (RelativeLayout) findViewById(R.id.rl_location_layout_facility);
        ivLocation                          = (ImageView) findViewById(R.id.iv_location);
        tvLocAdd                            = (TextView) findViewById(R.id.tv_loc_add);
        tvGetDirection                      = (TextView) findViewById(R.id.tv_get_direction);
        rlPhoneLayoutFacility               = (RelativeLayout) findViewById(R.id.rl_phone_layout_facility);
        ivPhone                             = (ImageView) findViewById(R.id.iv_phone);
        tvPhone                             = (TextView) findViewById(R.id.tv_phone);
        rlCloseTimeLayoutFacility           = (RelativeLayout) findViewById(R.id.rl_close_time_layout_facility);
        ivCloseTime                         = (ImageView) findViewById(R.id.iv_close_time);
        tvCloseTime                         = (TextView) findViewById(R.id.tv_close_time);
        tvPrise                             = (TextView) findViewById(R.id.tv_facility_charges_prise);
        tvCallNow                           = (ImageView) findViewById(R.id.tv_call_now);
        rlNotifictionLayoutFacility         = (RelativeLayout) findViewById(R.id.rl_notifiction_layout_facility);
        tvNotificationFacilityName          = (TextView) findViewById(R.id.tv_notification_facility_name);
        tvViewAllNotificationFacility       = (TextView) findViewById(R.id.tv_view_all_notification_facility_name);
        rlInflateNotificationFacilityLayout = (LinearLayout) findViewById(R.id.rl_inflate_notification_facility_layout);
        tvFacilityDetailsLayoutFacility     = (TextView) findViewById(R.id.tv_facility_details_layout_facility);
        tvFacilityDescriptionLayoutFacility = (TextView) findViewById(R.id.tv_facility_description_layout_facility);
        tvFacilityDownloadPdf               = (TextView) findViewById(R.id.tv_facility_download_pdf);
        tvFacilityWebView                   = (TextView) findViewById(R.id.tv_facility_web_view);
        tvFacilityGalleryLayoutFacility     = (TextView) findViewById(R.id.tv_facility_gallery_layout_facility);
        tvViewAllGalleryLayoutFacility      = (TextView) findViewById(R.id.tv_view_all_gallery_layout_facility);
        rlFacilityDetailsLayoutFacility     = (RelativeLayout) findViewById(R.id.rl_facility_details_layout_facility);
        rlFacilityGalleryLayoutFacility     = (RelativeLayout) findViewById(R.id.rl_facility_gallery_layout_facility);
        rvGalleryLayoutFacility             = (RecyclerView) findViewById(R.id.rv_gallery_layout_facility);
        tvEnquiryNow                        = (ImageView) findViewById(R.id.tv_Enquiry_now);

        getSubCategoryId       = getIntent().getIntExtra(SubCategoryTable.SUB_CATEGORY_Id,0);
        getSubCategoryCode     = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_CAT_CODE);
        getUserId              = getIntent().getIntExtra(Constant.USER_ID,0);
        getCollageId           = getIntent().getIntExtra(Constant.USER_COLLAGE_ID,0);
        getUserLoginType       = getIntent().getIntExtra(Constant.USER_LOGIN_TYPE,0);
        getCategoryColor       = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);
        getCategoryName        = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_NAME);

        /*  tvFacilityDownloadPdf.setTextColor(Color.parseColor(getCategoryColor));
        tvGetDirection.setTextColor(Color.parseColor(getCategoryColor));
        GradientDrawable bgShape = (GradientDrawable)tvCallNow.getBackground();
        bgShape.setColor(Color.parseColor(getCategoryColor));
        tvViewAllNotificationFacility.setTextColor(Color.parseColor(getCategoryColor));*/

        tvViewAllNotificationFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenericFacilityActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
               // Toast.makeText(GenericFacilityActivity.this,"notification clicked",Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGalleryLayoutFacility.setLayoutManager(layoutManager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        appbar.addOnOffsetChangedListener(this);

        if(!Constant.isConnectingToInternet(GenericFacilityActivity.this)){
            Snackbar.make(coordinatorLayout,getResources().getString(R.string.aleart_dialog_for_internet_title)+"!  "+
                    (getResources().getString(R.string.aleart_dialog_for_internet_message)), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        onItemTouchListener = new GalleryFacilityAdapter.RecyclerTouchListener(
                getApplicationContext(),rvGalleryLayoutFacility, new GalleryFacilityAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                try {
                    bundle.putString("facility_image_gallery",
                            jsonObject.getJSONArray("facility_image_gallery").toString());
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }
        );

        rvGalleryLayoutFacility.addOnItemTouchListener(onItemTouchListener);

        facilityJsonLoad();
        refreshData();

        tvEnquiryNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackUs();
            }
        });
    }

    public void feedbackUs(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(GenericFacilityActivity.this, FeedbackUs.class);
        bundle.putInt(SubCategoryTable.SUB_CATEGORY_Id,getSubCategoryId);
        bundle.putString(DrawableTable.DRAWABLE_NAME,((getCategoryName)));
        bundle.putString(DrawableTable.DRAWABLE_COLOR_CODE,((getCategoryColor)));
        bundle.putString(DrawableTable.DRAWABLE_PATH_URL,(("")));
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void facilityJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.SUB_SUB_CATEGORY_URL
                        +"?"+SubCategoryTable.SUB_CATEGORY_Id+"="+getSubCategoryId
                        +"&"+SubCategoryTable.SUB_CATEGORY_CAT_CODE+"="+getSubCategoryCode)
/*                .url(Constant.CAT_TYPE_MAP_TEMP_URL1)*/
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            public void onFailure(Request paramRequest, IOException paramIOException) {
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    GenericFacilityActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GenericFacilityActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
//                    GenericFacilityActivity.this.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_FACILITY,null,null);
                    values.put(GenericFacilityActivityTable.FACILITY_SUB_CATEGORY_ID, getSubCategoryId);
                    values.put(GenericFacilityActivityTable.FACILITY_JSON_ARRAY, responseJsonData);
                    GenericFacilityActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_FACILITY, values);

                }
                catch (JSONException paramResponse) {
                    GenericFacilityActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GenericFacilityActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(GenericFacilityActivity.this, R.style.AppCompatAlertDialogStyle);
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
                GenericFacilityActivity.this.finish();
            }
        });
        builder.show();
    }

    private void refreshData(){
        this.getLoaderManager().initLoader(0, null, this);
        this.getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if(id==0){
        String selection = GenericFacilityActivityTable.FACILITY_SUB_CATEGORY_ID+ "=?";
        String[] selectionArgs = { String.valueOf(getSubCategoryId) };
             return new CursorLoader(GenericFacilityActivity.this, ApplicationContentProvider.CONTENT_URI_FACILITY.buildUpon().build(), null,selection,selectionArgs,null);
        }
         else {
            String selection =
                    NotificationTable.NOTIFICATION_IS_IMP+ "=?"
            +" AND "+
                            NotificationTable.NOTIFICATION_SUB_SUB_CAT_ID+" in (?)";

            /*String selection =
                    NotificationTable.NOTIFICATION_IS_IMP+ "=?"
                            +" AND "+
                            NotificationTable.NOTIFICATION_SUB_SUB_CAT_ID+" in (?)" + " ORDER BY "+
                            NotificationTable.NOTIFICATION_ADD_DATE+" DESC LIMIT 0,3";*/

            String sort = NotificationTable.NOTIFICATION_ADD_DATE+" DESC";
            String limit = " LIMIT 3";

            String[] selectionArgs = new String[]{
                    String.valueOf(1),
                    String.valueOf(getSubCategoryId)
            };
//            return new CursorLoader(GenericFacilityActivity.this, ApplicationContentProvider.CONTENT_URI_NOTIFICATION_FOR_FACILITY, null,selection,selectionArgs,null);
            return new CursorLoader(GenericFacilityActivity.this, ApplicationContentProvider.CONTENT_URI_NOTIFICATION_FOR_FACILITY, null,selection,selectionArgs,sort+limit);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        switch (loader.getId()) {
            case 0:
                if (data.getCount() == 0 && !Constant.isConnectingToInternet(GenericFacilityActivity.this) ){
                    aleartDialogForInternetConnectivity();
                }
                if(data.getCount()>0 && data.moveToFirst())
                {
                    final String jsonArrStr = data.getString(data.getColumnIndex(GenericFacilityActivityTable.FACILITY_JSON_ARRAY));
                    try {
                        JSONArray jsonArray = new JSONArray(jsonArrStr);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            final String facilityImageUrl;
                            final String facilityName;
                            String facilityContactNumber="";
                            String facilityAdd = "";
                            String facilityOpenTime = "";
                            String facilityPrise="";
                            String facilityDetails = "";
                            String facilityDescription="";
                            String facilityNotificationHeading;
                            String facilityNotificationSubText;
                            final String downloadPdf;
                            final String readMoreForWebView;
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            facilityImageUrl        = jsonArray.getJSONObject(i).optString("facility_image_url");
                            facilityName            = jsonArray.getJSONObject(i).optString("facility_name");
                            downloadPdf             = jsonArray.getJSONObject(i).optString("facility_pdf_url");
                            readMoreForWebView      = jsonArray.getJSONObject(i).optString("facility_web_url");
                            JSONArray jsonArrayAdd  = jsonObject.getJSONArray("facility_address");
                            for (int j = 0; j < jsonArrayAdd.length(); j++) {
                                JSONObject jsonObjectAddress = jsonArrayAdd.getJSONObject(j);
                                if (jsonObjectAddress.has("address")){
                                    facilityAdd           = jsonObjectAddress.optString("address");
                                    facilityLat           = jsonObjectAddress.optDouble("lat");
                                    facilityLong          = jsonObjectAddress.optDouble("long");
                                } else if(jsonObjectAddress.has("contact_number")){
                                    facilityContactNumber = jsonObjectAddress.optString("contact_number");
                                } else if(jsonObjectAddress.has("timing")){
                                    facilityOpenTime      = jsonObjectAddress.optString("timing");
                                    facilityPrise         = jsonObjectAddress.optString("charges");
                                }
                            }


                            JSONArray jsonArrayDetails  = jsonObject.getJSONArray("facility_description");
                            for (int k = 0; k < jsonArrayDetails.length(); k++) {
                                JSONObject jsonObjectDetails = jsonArrayDetails.getJSONObject(k);
                                facilityDetails         = jsonObjectDetails.optString("heading");
                                facilityDescription     = jsonObjectDetails.optString("subtext");
                            }



                            collapsingToolbarLayout.setContentScrimColor(Color.parseColor(getCategoryColor));
                            collapsingToolbarLayout.setTitle(facilityName);
                            Picasso.with(this)
                                    .load(facilityImageUrl)
                                    .placeholder(R.drawable.default_bg_icon)
                                    .fit()
                                    .into(ivHeaderFacility);
                            tvFacilityName.setText(facilityName);
                            tvLocAdd.setText(facilityAdd);
                            tvPhone.setText(facilityContactNumber);

                            if(facilityContactNumber.length()>0){
                                tvCallNow.setVisibility(View.VISIBLE);
                            }else{
                                tvCallNow.setVisibility(View.GONE);
                            }
                            final String number = "tel:"+(facilityContactNumber);
                            tvCallNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse(number));
                                    startActivity(intent);
                                }
                            });

                            rlLocationLayoutFacility.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermissionAndOpenMaps(new LatLng(facilityLat,facilityLong),googleMap);

                                }
                            });

                            if(downloadPdf.length()>0){
                                tvFacilityDownloadPdf.setVisibility(View.VISIBLE);
                            }else{
                                tvFacilityDownloadPdf.setVisibility(View.GONE);
                            }

                            tvFacilityDownloadPdf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    (GenericFacilityActivity.this).runOnUiThread(new Runnable() {
                                        public void run() {
                                            new DownloadFile(GenericFacilityActivity.this).execute(downloadPdf,facilityName);
                                        }
                                    });
                                }
                            });


                            if(readMoreForWebView.length()>0){
                                tvFacilityWebView.setVisibility(View.VISIBLE);
                            }else{
                                tvFacilityWebView.setVisibility(View.GONE);
                            }

                            tvFacilityWebView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    (GenericFacilityActivity.this).runOnUiThread(new Runnable() {
                                        public void run() {

                                    Bundle bundle = new Bundle();
                                    Intent intent = new Intent(GenericFacilityActivity.this, WebViewActivityForReadMoreFacility.class);

                                    bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR,getCategoryColor);
                                    bundle.putString("facility_name",facilityName);
                                    bundle.putString("facility_web_url",readMoreForWebView);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


                                        }
                                    });
                                }
                            });



                            tvCloseTime.setText(facilityOpenTime);
                            tvPrise.setText(facilityPrise);

                            tvFacilityDetailsLayoutFacility.setText(facilityDetails);
                            tvFacilityDescriptionLayoutFacility.setText(facilityDescription);

                            if(jsonObject.getJSONArray("facility_image_gallery").length()>0){
                                rlFacilityGalleryLayoutFacility.setVisibility(View.VISIBLE);
                            }else {
                                rlFacilityGalleryLayoutFacility.setVisibility(View.GONE);
                            }

                            try{
                                GalleryFacilityAdapter galleryFacilityAdapter = new GalleryFacilityAdapter
                                        (jsonObject.getJSONArray("facility_image_gallery"),this);
                                rvGalleryLayoutFacility.setAdapter(galleryFacilityAdapter);
                            }
                            catch (JSONException e){

                            }

                            this.jsonObject = jsonObject;

                            contentLoadingProgressBarFacility.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if(data.getCount()==0)
                {
                    rlNotifictionLayoutFacility.setVisibility(View.GONE);
                    return;
                }
                rlNotifictionLayoutFacility.setVisibility(View.VISIBLE);
                rlInflateNotificationFacilityLayout.setVisibility(View.VISIBLE);
                rlInflateNotificationFacilityLayout.removeAllViews();
                while (data.moveToNext()) {
                    final String notifyTitle =  data.getString(data.getColumnIndex(NotificationTable.NOTIFICATION_TITLE));
                    final String notifyDetails =  data.getString(data.getColumnIndex(NotificationTable.NOTIFICATION_DETAILS));
                    final String notifyImageUrl =  data.getString(data.getColumnIndex(NotificationTable.NOTIFICATION_IMAGE_URL));
                    final String notifyWebView =  data.getString(data.getColumnIndex(NotificationTable.NOTIFICATION_URL));
                    final String notiDate = data.getString(data.getColumnIndex(NotificationTable.NOTIFICATION_DATE));
                    String notifyId = data.getString(data.getColumnIndex(NotificationTable.NOTIFICATION_ID));
                    View child = getLayoutInflater().inflate(R.layout.generic_facility_notification, null,false);
                    rlInflateNotificationFacilityLayout.addView(child,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    if(data.isLast()){
                        child.findViewById(R.id.view_dashed_line).setVisibility(View.GONE);
                        //do this
                    }
                    else {
                        // do this
                    }

                    ((TextView)child.findViewById(R.id.tv_notification_facility_name)).setText(notifyTitle);
                    ((TextView)child.findViewById(R.id.tv_notification_facility_details)).setText(notifyDetails);
                    child.setVisibility(View.VISIBLE);

                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(GenericFacilityActivity.this, NotificationDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(NotificationTable.NOTIFICATION_TITLE,notifyTitle);
                            bundle.putString(NotificationTable.NOTIFICATION_DETAILS,notifyDetails);
                            bundle.putString(NotificationTable.NOTIFICATION_DATE,notiDate);
                            bundle.putString(NotificationTable.NOTIFICATION_IMAGE_URL,notifyImageUrl);
                            bundle.putString(NotificationTable.NOTIFICATION_URL,notifyWebView);
                            //               bundle.putString(NotificationTable.NOTIFICATION_DATE,mCursor.getString(mCursor.getColumnIndex(NotificationTable.NOTIFICATION_DATE)));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });

                }
                break;
            default:
                break;
        }
    }

    private void requestPermissionAndOpenMaps(LatLng latLng, GoogleMap googleMap){
        lattitude = latLng.latitude;
        longitude = latLng.longitude;
        /*googleMap.setMyLocationEnabled(false);*/
        /*googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);*/

        openInGoogleMaps();
    }

    private void openInGoogleMaps(){
        if (this.lattitude==0.0){
            Toast.makeText(this,"Please select a point to open in maps", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"opening maps", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("http://maps.google.com/maps?daddr="
                                    + lattitude + ","
                                    + longitude + ""));
                    startActivity(navigation);
                }
            }, 1000);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(mFab).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFab).scaleY(1).scaleX(1).start();
            }
        }

        //        if (currentScrollPercentage >= 100) {
        //            toolbar.setBackgroundColor(Color.parseColor(getCategoryColor));
        //        }
        //        else{
        //            toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        //        }

    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, GenericFacilityActivity.class));
    }

    private static class DownloadFile extends AsyncTask<String, String, Void> {
        Context context;
        ProgressDialog dialog;
        String fileUrl,fileName;
        public DownloadFile(Context mContext) {
            context = mContext;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            fileUrl = strings[0];
            fileName = strings[1];
            File folder = new File( Environment.getExternalStorageDirectory(), Constant.SQLITE_FOLDER_NAME);
            folder.mkdir();
            File pdfFile = new File(folder + "/" + fileName);
            if (pdfFile.exists() ){
                return null;
            }

            try{
                pdfFile.createNewFile();
                GenericFacilityActivity.FileDownloader.downloadFile(fileUrl, pdfFile,this);
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            boolean isSuccess = openFile(fileName, context);
            if (!isSuccess){
                Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();
            }
            dismissDialog();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

        private void dismissDialog(){
            if (dialog !=null){
                dialog.cancel();
                dialog=null;
            }
        }

        protected Dialog onCreateDialog() {
            this.dialog = new ProgressDialog(context);
            dialog.setMessage("Downloading file. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(true);
            dialog.show();
            return dialog;
        }

        public void publishProgressFromDownloader(float progress){
            publishProgress(""+(int)(100*progress));
        }
    }

    private static boolean openFile(String fileName, Context context){
        File folder = new File(Environment.getExternalStorageDirectory() , Constant.SQLITE_FOLDER_NAME);
        File pdfFile = new File(folder , fileName);
        if (!pdfFile.exists()){
            return false;
        }
        Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".fileprovider",pdfFile);

        context.grantUriPermission(context.getPackageName(),
                path, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try{
            context.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public static class FileDownloader {
        private static final int  MEGABYTE = 1024 * 5;

        public static void downloadFile(String fileUrl, File directory, GenericFacilityActivity.DownloadFile asyncTask){
            int length = 0;
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(directory);
                int totalSize = urlConnection.getContentLength();
                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    length = length+bufferLength;
                    asyncTask.publishProgressFromDownloader(1.0f*length/(1.0f*totalSize));
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}