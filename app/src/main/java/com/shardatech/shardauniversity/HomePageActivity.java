package com.shardatech.shardauniversity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.CategoryRecyclerViewAdapter;
import adapter.CustomDrawerAdapter;
import adapter.SliderViewPagerAdapter;
import adapter.UniversityRecyclerViewAdapter;
import adapter.ViewPagerAdapter;
import database.ApplicationContentProvider;
import databaseTable.DrawableTable;
import databaseTable.GenericFacilityActivityTable;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.SubCategoryTable;
import method.Constant;
import method.MySharedPref;
import model.DrawerItem;

import static java.security.AccessController.getContext;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener,View.OnClickListener, /*CategoryRecyclerViewAdapter.ItemListener,*/
        LoaderManager.LoaderCallbacks<Cursor>
{
    private ViewPager intro_images, slider_pager;
    private SliderViewPagerAdapter sliderViewPagerAdapter;
    private UniversityRecyclerViewAdapter adapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    RecyclerView recyclerView, recyclerViewUni, recyclerViewUni2;
    private CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    private CustomDrawerAdapter drawerAdapter;
    List<DrawerItem> dataList;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ContentLoadingProgressBar contentLoadingProgressBarSlider;
    private ContentLoadingProgressBar contentLoadingProgressBarCategory;
    private FloatingActionButton fab;
    private String emerContactNumber;
    int currentPage = 0, currentSliderData=0;
    Timer timer;
    final long DELAY_MS = 200;
    final long PERIOD_MS = 3000;
    int collegeId,userId;
    TextView tvReadNotificationCount;
    Boolean userAppVersion;
    ImageButton leftArrow,rightArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_college_category);
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        contentLoadingProgressBarSlider = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingPBViewPager);
        contentLoadingProgressBarCategory = (ContentLoadingProgressBar) findViewById(R.id.contentLoadingPBCategory);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        contentLoadingProgressBarSlider.setVisibility(View.VISIBLE);
        contentLoadingProgressBarCategory.setVisibility(View.VISIBLE);
        collegeId = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
        userId          = MySharedPref.getUserId(this,Constant.USER_ID);

        slider_pager=findViewById(R.id.home_slider_viewPager);
        leftArrow=findViewById(R.id.left_arrow_home);
        rightArrow=findViewById(R.id.right_arrow_home);
        recyclerViewUni=findViewById(R.id.rv_uni_home);
        recyclerViewUni2=findViewById(R.id.rv_uni2_home);


        sliderJsonLoad();
        categoryJsonLoad();
        drawerJsonLoad();
        myProfileJsonLoad();
        showDrawerData();
        showSliderData();
        showCategoryData();
        showbottomSlider();
        setUniRecyclerView(recyclerViewUni);
        setUniRecyclerView(recyclerViewUni2);
      //  showNavigationData();
        refreshData();

        if(!Constant.isConnectingToInternet(HomePageActivity.this)){
            Snackbar.make(mDrawerLayout,getResources().getString(R.string.aleart_dialog_for_internet_title)+"!  "+
                    (getResources().getString(R.string.aleart_dialog_for_internet_message)), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                if((MySharedPref.getHomePageEmergNumber(HomePageActivity.this,HomeActivityCategoryTable.CATEGORY_COLLEGE_EMER_NUMBER)).isEmpty())
                {
                    emerContactNumber = Constant.contactNumber;
                }
                else{
                    emerContactNumber = "tel:"+(MySharedPref.getHomePageEmergNumber(HomePageActivity.this,HomeActivityCategoryTable.CATEGORY_COLLEGE_EMER_NUMBER));
                }
                intent.setData(Uri.parse(emerContactNumber));
                startActivity(intent);
            }
        });


        userAppVersion = MySharedPref.getAppVersion(this,Constant.ANDROID_APP_VERSION);

        if(userAppVersion){
            showPopupForUpdateApp();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
        verticalDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);
    }


    public void showPopupForUpdateApp()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);
        builder.setTitle("Update New Version");
        builder.setMessage("Please update new version from play store.");
        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName()));
                startActivity(i);
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }


    public void sliderJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.HOME_PAGER_URL+"?"+Constant.USER_COLLAGE_ID+"="+collegeId)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {
                boolean failed = true;
                boolean x =false;
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {

                    ContentValues values = new ContentValues();
                    JSONArray jsonArray = new JSONArray(responseJsonData);
                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).optJSONArray("homepage_banner");
                    HomePageActivity.this.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_PAGER,null,null);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put("home_pager_image_id", jsonArray1.getJSONObject(i).optString("banner_id"));
                        values.put("home_pager_image_title", jsonArray1.getJSONObject(i).optString("banner_name"));
                        values.put("home_pager_image_time", jsonArray1.getJSONObject(i).optString("banner_description"));
                        values.put("home_pager_image_address", jsonArray1.getJSONObject(i).optString("banner_description"));
                        values.put("home_pager_image_url", jsonArray1.getJSONObject(i).optString("image_path"));

                        HomePageActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_PAGER, values);
                    }
                } catch (JSONException paramResponse) {
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void showSliderData() {
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new ViewPagerAdapter(HomePageActivity.this, null);
        intro_images.setAdapter(mAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == mAdapter.getCount()) {
                    currentPage = 0;
                }
                intro_images.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
//        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
    }

    public void setUniRecyclerView(RecyclerView recyclerView)
    {
        ArrayList<String> image=new ArrayList<>();
        image.add("https://timesofindia.indiatimes.com/thumb/msid-59023402,width-400,resizemode-4/59023402.jpg");
        image.add("https://timesofindia.indiatimes.com/thumb/msid-59023402,width-400,resizemode-4/59023402.jpg");
        image.add("https://timesofindia.indiatimes.com/thumb/msid-59023402,width-400,resizemode-4/59023402.jpg");
        image.add("https://timesofindia.indiatimes.com/thumb/msid-59023402,width-400,resizemode-4/59023402.jpg");
        image.add("https://timesofindia.indiatimes.com/thumb/msid-59023402,width-400,resizemode-4/59023402.jpg");

        ArrayList<String> icon=new ArrayList<>();
        icon.add("https://www.reviewadda.com/assets/uploads/college/logo/LOGO_2922.png");
        icon.add("https://www.reviewadda.com/assets/uploads/college/logo/LOGO_2922.png");
        icon.add("https://www.reviewadda.com/assets/uploads/college/logo/LOGO_2922.png");
        icon.add("https://www.reviewadda.com/assets/uploads/college/logo/LOGO_2922.png");
        icon.add("https://www.reviewadda.com/assets/uploads/college/logo/LOGO_2922.png");

        ArrayList<String> name=new ArrayList<>();
        name.add("Sharda University");
        name.add("Sharda University");
        name.add("Sharda University");
        name.add("Sharda University");
        name.add("Sharda University");

        ArrayList<String> location=new ArrayList<>();
        location.add("Greater Noida, Uttar Pradesh");
        location.add("Greater Noida, Uttar Pradesh");
        location.add("Greater Noida, Uttar Pradesh");
        location.add("Greater Noida, Uttar Pradesh");
        location.add("Greater Noida, Uttar Pradesh");


        recyclerView.setHasFixedSize(true);
        recyclerView.removeAllViews();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.removeAllViewsInLayout();
        StaggeredGridLayoutManager mStaggeredVerticalLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL); // (int spanCount, int orientation)
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
        adapter =
                new UniversityRecyclerViewAdapter(HomePageActivity.this,image,icon,location,name);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);

        adapter.notifyDataSetChanged();



    }

    public void showbottomSlider()
    {
        sliderViewPagerAdapter=new SliderViewPagerAdapter(HomePageActivity.this);
        slider_pager.setAdapter(sliderViewPagerAdapter);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSliderData>0)
                {
                    slider_pager.setCurrentItem(--currentSliderData,true);
                    Log.d("leftArrow",currentSliderData+"");
                }
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSliderData<sliderViewPagerAdapter.getCount())
                {
                    slider_pager.setCurrentItem(++currentSliderData,true);
                    Log.d("rightArrow",currentSliderData+"");
                }
            }
        });

    }


    public void categoryJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.CATEGORY_URL+"?"+Constant.USER_COLLAGE_ID+"="+collegeId+"&"+Constant.USER_ID+"="+userId)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            public void onFailure(Request paramRequest, IOException paramIOException) {

            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
                    MySharedPref.setHomePageEmergNumber(HomePageActivity.this,HomeActivityCategoryTable.CATEGORY_COLLEGE_EMER_NUMBER,jsonArray.getJSONObject(0).optString("college_emer_num"));
                    values.put(HomeActivityCategoryTable.CATEGORY_COLLEGE_EMER_NUMBER, jsonArray.getJSONObject(0).optString("college_emer_num"));
                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("cat");
                    HomePageActivity.this.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_CATEGORY,null,null);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put(HomeActivityCategoryTable.CATEGORY_Id, jsonArray1.getJSONObject(i).optString("cat_id"));
                        values.put(HomeActivityCategoryTable.CATEGORY_NAME, jsonArray1.getJSONObject(i).optString("cat_name"));
                        values.put(HomeActivityCategoryTable.CATEGORY_IMAGE_URL, jsonArray1.getJSONObject(i).optString("cat_image_url"));
                        values.put(HomeActivityCategoryTable.CATEGORY_COLOR, jsonArray1.getJSONObject(i).optString("cat_color"));
                        values.put(HomeActivityCategoryTable.CATEGORY_COLOR_BG_DARK, jsonArray1.getJSONObject(i).optString("cat_color_bg_dark"));
                        values.put(HomeActivityCategoryTable.CATEGORY_COLOR_BG_LIGHT, jsonArray1.getJSONObject(i).optString("cat_color_bg_light"));
                        values.put(HomeActivityCategoryTable.CATEGORY_CODE, jsonArray1.getJSONObject(i).optString("cat_code"));
                        HomePageActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_CATEGORY, values);
                    }
                } catch (JSONException paramResponse) {
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void drawerJsonLoad()
    {
        int collegeId = MySharedPref.getUserCollegeId(this,Constant.USER_COLLAGE_ID);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                  .url(Constant.DRAWABLE_URL+"?"+Constant.USER_COLLAGE_ID+"="+collegeId+"&"+Constant.USER_ID+"="+userId)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            public void onFailure(Request paramRequest, IOException paramIOException) {
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).optJSONArray("drawer");
                    HomePageActivity.this.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_DRAWABLE,null,null);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put(DrawableTable.DRAWABLE_Id, jsonArray1.getJSONObject(i).optInt("id_value"));
                        values.put(DrawableTable.DRAWABLE_NAME, jsonArray1.getJSONObject(i).optString("drawer_name"));
                        values.put(DrawableTable.DRAWABLE_CODE, jsonArray1.getJSONObject(i).optString("drawer_code"));
                        values.put(DrawableTable.DRAWABLE_COLOR_CODE, jsonArray1.getJSONObject(i).optString("drawer_color_code"));
                        values.put(DrawableTable.DRAWABLE_PATH_URL, jsonArray1.getJSONObject(i).optString("drawer_path_url"));
                        values.put(DrawableTable.DRAWABLE_VIEW_TYPE, jsonArray1.getJSONObject(i).optString("view_type"));
                        values.put(DrawableTable.DRAWABLE_IMAGE_URL, jsonArray1.getJSONObject(i).optString("drawer_image_url"));
                        HomePageActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_DRAWABLE, values);
                    }

                    JSONArray jsonArray2 = jsonArray.getJSONObject(0).optJSONArray("sub_cat");
                    ContentValues values1 = new ContentValues();
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        values1.put(SubCategoryTable.SUB_CATEGORY_Id, jsonArray2.getJSONObject(i).optString("sub_cat_id"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_NAME, jsonArray2.getJSONObject(i).optString("sub_cat_name"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_IMAGE_URL, jsonArray2.getJSONObject(i).optString("sub_cat_image_url"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID, jsonArray2.getJSONObject(i).optString("sub_cat_heading_id"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE, jsonArray2.getJSONObject(i).optString("sub_cat_taken_offline"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_CAT_CODE, jsonArray2.getJSONObject(i).optString("sub_cat_cat_code"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_USER_TYPE, jsonArray2.getJSONObject(i).optString("sub_cat_user_type"));
                        values1.put(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER, jsonArray2.getJSONObject(i).optString("sub_cat_contact_number"));
                        HomePageActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_SUB_CATEGORY, values1);
                    }

                } catch (JSONException paramResponse) {
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void myProfileJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.USER_MY_PROFILE_URL+"?"
                        +Constant.USER_COLLAGE_ID+"="+collegeId+"&"
                        +Constant.USER_ID+"="+userId+"&"
                        +Constant.USER_ANDROID_APP_VERSION+"="+MySharedPref.getUserAppVersion(HomePageActivity.this,Constant.USER_ANDROID_APP_VERSION))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {
                boolean failed = true;
                boolean x =false;
            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseJsonData);
                    for (int i = 0; i < jsonObject.length(); i++) {
                        MySharedPref.setUserName(HomePageActivity.this,Constant.USER_NAME, jsonObject.optString(Constant.USER_FIRST_NAME));
                        MySharedPref.setUserLastName(HomePageActivity.this,Constant.USER_LAST_NAME, jsonObject.optString(Constant.USER_LAST_NAME));
                        MySharedPref.setUserEmail(HomePageActivity.this,Constant.USER_EMAIL, jsonObject.optString(Constant.USER_EMAIL));
                        MySharedPref.setUserContactNumber(HomePageActivity.this,Constant.USER_CONTACT_NUMBER, jsonObject.optString(Constant.USER_CONTACT_NUMBER));
                        MySharedPref.setUserDisplayPic(HomePageActivity.this,Constant.USER_PROFILE_PIC, jsonObject.optString(Constant.USER_PROFILE_PIC));
                        MySharedPref.setUserGender(HomePageActivity.this,Constant.USER_GENDER, jsonObject.optString(Constant.USER_GENDER));
                        MySharedPref.setUserAdd(HomePageActivity.this,Constant.USER_ADD, jsonObject.optString(Constant.USER_ADD));
                        MySharedPref.setUserCity(HomePageActivity.this,Constant.USER_CITY, jsonObject.optString(Constant.USER_CITY));
                        MySharedPref.setUserDOB(HomePageActivity.this,Constant.USER_DOB, jsonObject.optString(Constant.USER_DOB));
                        MySharedPref.setUserIam(HomePageActivity.this,Constant.USER_ROLE, jsonObject.optString(Constant.USER_ROLE));
                        MySharedPref.setAppVersion(HomePageActivity.this,Constant.ANDROID_APP_VERSION, jsonObject.optBoolean("error"));
                    }
                } catch (JSONException paramResponse) {
                    HomePageActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(HomePageActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void jsonLoadSubCat(final String getSubCategoryId, final String getSubCategoryCode)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.SUB_SUB_CATEGORY_URL
                        +"?"+ SubCategoryTable.SUB_CATEGORY_Id+"="+getSubCategoryId
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
                try {
//                    if (getSubCategoryCode.equals("facility")){
                        JSONArray jsonArray = new JSONArray(responseJsonData);
                        ContentValues values = new ContentValues();
                        values.put(GenericFacilityActivityTable.FACILITY_SUB_CATEGORY_ID, getSubCategoryId);
                        values.put(GenericFacilityActivityTable.FACILITY_JSON_ARRAY, responseJsonData);
                        HomePageActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_FACILITY, values);
//                    }
//                    else if(getSubCategoryCode.equals("web_view") || getSubCategoryCode.equals("call") || getSubCategoryCode.equals("pdf") || getSubCategoryCode.equals("pdf1")){
//                        ContentValues values = new ContentValues();
//                        JSONObject jsonObject = new JSONObject(responseJsonData);
//                        values.put(SubCategoryTable.SUB_CATEGORY_Id, jsonObject.optString("sub_cat_id"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_NAME, jsonObject.optString("sub_cat_name"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_IMAGE_URL, jsonObject.optString("sub_cat_image_url"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_SUB_HEADING_ID, jsonObject.optString("sub_cat_heading_id"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_CAN_BE_TAKEN_OFFLINE, jsonObject.optString("sub_cat_taken_offline"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_CAT_CODE, jsonObject.optString("sub_cat_cat_code"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_USER_TYPE, jsonObject.optString("sub_cat_user_type"));
//                        values.put(SubCategoryTable.SUB_CATEGORY_CONTACT_NUMBER, jsonObject.optString("sub_cat_contact_number"));
//                        HomePageActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_SUB_CATEGORY, values);
//                    }
                }
                catch (JSONException paramResponse) {
//                    GenericFacilityActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(GenericFacilityActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    // Aleart Dialog for Net Connection...
    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this, R.style.AppCompatAlertDialogStyle);
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
                finish();
            }
        });
        builder.show();
    }

    public void showCategoryData() {
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(this,null);
        recyclerView.setAdapter(categoryRecyclerViewAdapter);
    }

    public void showDrawerData() {
        drawerAdapter = new CustomDrawerAdapter(this,null,mDrawerLayout);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDrawerList.setLayoutManager(manager);
        mDrawerList.setAdapter(drawerAdapter);
    }



    public void showNavigationData() {
        drawerAdapter = new CustomDrawerAdapter(this,null,mDrawerLayout);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDrawerList.setLayoutManager(manager);
        mDrawerList.setAdapter(drawerAdapter);
    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        pager_indicator.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            if(i==0){
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }
            else{
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
      //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void refreshNotificationCount(){
        if (tvReadNotificationCount!=null){
            this.getLoaderManager().restartLoader(3, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
            getMenuInflater().inflate(R.menu.home_notification_icon, menu);
           // MenuItem menuItem = menu.findItem(R.id.action_notification);
            tvReadNotificationCount = (TextView) menu.findItem(R.id.action_notification).getActionView().findViewById(R.id.badge_textView);
            refreshNotificationCount();
            menu.findItem(R.id.action_notification).getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(HomePageActivity.this);
                    int userId    = MySharedPref.getUserId(HomePageActivity.this,Constant.USER_ID);
                    Bundle params = new Bundle();
                    params.putInt(FirebaseAnalytics.Param.ITEM_ID, userId);
                    params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Notification Button Clicked");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
                    mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                    mFirebaseAnalytics.setMinimumSessionDuration(2000);
                    mFirebaseAnalytics.setSessionTimeoutDuration(300000);
                    mFirebaseAnalytics.setUserProperty("FAVOURITE_AUTHOR","");

                    onOptionsItemSelected(menu.findItem(R.id.action_notification));
                }
            });
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification) {
            Intent intent = new Intent(HomePageActivity.this, NotificationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
           // Toast.makeText(HomePageActivity.this,"notification clicked",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshData(){
        this.getLoaderManager().initLoader(0, null, this);
        this.getLoaderManager().initLoader(1, null, this);
//        this.getLoaderManager().initLoader(2, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if(id==0){
            return new CursorLoader(HomePageActivity.this, ApplicationContentProvider.CONTENT_URI_PAGER.buildUpon().build(), null,null,null,null);
        }
        else if(id==1){
            return new CursorLoader(HomePageActivity.this, ApplicationContentProvider.CONTENT_URI_CATEGORY.buildUpon().build(), null,null,null,null);
        }
        else if(id==2){
            return new CursorLoader(HomePageActivity.this, ApplicationContentProvider.CONTENT_URI_DRAWABLE.buildUpon().build(), null,null,null,null);
        }
        else {
            return new CursorLoader(HomePageActivity.this, ApplicationContentProvider.CONTENT_URI_NOTIFICATION_READ_COUNT.buildUpon().build(), null,null,null,null);
        }
//        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() == 0 && !Constant.isConnectingToInternet(HomePageActivity.this) ){
            aleartDialogForInternetConnectivity();
        }

        switch (loader.getId()) {
            case 0:
                if(data.getCount()>0)
                {
                    contentLoadingProgressBarSlider.setVisibility(View.INVISIBLE);
                }
                mAdapter.swapCursor(data);
                setUiPageViewController();
                break;
            case 1:
                if(data.getCount()>0)
                {
                    contentLoadingProgressBarCategory.setVisibility(View.INVISIBLE);
                }
                categoryRecyclerViewAdapter.changeCursor(data);
                /*if(data.moveToFirst()){
                    MatrixCursor matrixCursor = new MatrixCursor(HomeActivityCategoryTable.arrayOfCols);
                    MatrixCursor matrixCursor2 = new MatrixCursor(HomeActivityCategoryTable.arrayOfCols);
                    matrixCursor.addRow(HomeActivityCategoryTable.defaultValues);
                    matrixCursor2.addRow(HomeActivityCategoryTable.defaultValues);
                    MergeCursor mergeCursor = new MergeCursor(new Cursor[] { matrixCursor, data, matrixCursor2});
                    drawerAdapter.changeCursor(mergeCursor);
                }*/
                break;

            case 2:
                if(data.moveToFirst()){
                    MatrixCursor matrixCursor = new MatrixCursor(DrawableTable.arrayOfCols);
                    matrixCursor.addRow(DrawableTable.defaultValues);
                    MergeCursor mergeCursor = new MergeCursor(new Cursor[] { matrixCursor, data});
                    drawerAdapter.changeCursor(mergeCursor);
                }
                break;

            case 3:
                if(data.moveToFirst()){
                    int notificationCount = data.getInt(0);

                    if(notificationCount==0){
                        tvReadNotificationCount.setVisibility(View.INVISIBLE);
                    }
                    else
                        tvReadNotificationCount.setVisibility(View.VISIBLE);
                        tvReadNotificationCount.setText(""+notificationCount);
                       // Toast.makeText(HomePageActivity.this,"notification = "+notificationCount,Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResume(){
        super.onResume();
        refreshNotificationCount();
        this.getLoaderManager().restartLoader(2, null, this);
        drawerAdapter.notifyDataSetChanged();
    }
}
