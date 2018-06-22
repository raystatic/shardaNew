package com.shardatech.shardauniversity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import adapter.NotificationAdapter;
import database.ApplicationContentProvider;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.NotificationTable;
import method.Constant;
import method.MySharedPref;

/**
 * Created by sharda on 9/10/2017.
 */

public class NotificationActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private int subCategoryId;
    private String subCategoryName, subCategoryCatCode, subCategoryColorCode;
    Toolbar toolbar;
    int notificationCount = 0;
    private int collegeId;
    private int userLoginType;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        recyclerView = (RecyclerView) findViewById(R.id.rl_notification);
        collegeId = MySharedPref.getUserCollegeId(this, Constant.USER_COLLAGE_ID);
        userLoginType = Math.max(Constant.LOGIN_TYPE_FOR_FACEBOOK, MySharedPref.getUserLoginType(this, Constant.USER_LOGIN_TYPE));
        userId = MySharedPref.getUserId(this, Constant.USER_ID);
        notificationClicked();

        /* subCategoryId        = getIntent().getIntExtra(SubCategoryTable.SUB_CATEGORY_Id,0);
        subCategoryName         = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_NAME);
        subCategoryCatCode      = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_CAT_CODE);
        subCategoryColorCode    = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR); */

        notificationJsonLoad();
        showNotificationData();
        refreshData();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification(" + notificationCount + ")");
        /*getSupportActionBar().
                setBackgroundC((Color.parseColor(("#1849FD"))));*/
        toolbar.setBackgroundColor(Color.parseColor(("#a856aa")));

       /* if(getIntent().getStringExtra("type").equals("0")){
            aleartDialogForFillInformation(getIntent().getStringExtra("title"),getIntent().getStringExtra("content"));
        }*/

    }


    public void aleartDialogForFillInformation(String title,String content)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public void notificationJsonLoad() {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.NOTIFICATION_URL
                        + "?" + Constant.USER_ID + "=" + userId
                        + "&" + Constant.USER_COLLAGE_ID + "=" + collegeId
                        + "&" + Constant.USER_LOGIN_TYPE + "=" + userLoginType)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {

            }

            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()) {
                    NotificationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(NotificationActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                try {
                    JSONArray jsonArray = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("notification");
//                    NotificationActivity.this.getContentResolver().
//                            delete(ApplicationContentProvider.CONTENT_URI_NOTIFICATION, null, null);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put(NotificationTable.NOTIFICATION_ID, jsonArray1.getJSONObject(i).optInt("notify_id"));
                        values.put(NotificationTable.NOTIFICATION_SUB_SUB_CAT_ID, jsonArray1.getJSONObject(i).optInt("notify_sub_sub_cat_id"));
                        values.put(NotificationTable.NOTIFICATION_SUB_SUB_CAT_NAME, jsonArray1.getJSONObject(i).optString("notify_sub_sub_cat_name"));
                        values.put(NotificationTable.NOTIFICATION_TITLE, jsonArray1.getJSONObject(i).optString("notify_title"));
                        values.put(NotificationTable.NOTIFICATION_DETAILS, jsonArray1.getJSONObject(i).optString("notify_details"));
                        values.put(NotificationTable.NOTIFICATION_ADD_DATE, jsonArray1.getJSONObject(i).optLong("notify_add_date"));
                        values.put(NotificationTable.NOTIFICATION_DATE, jsonArray1.getJSONObject(i).optInt("notify_date"));
                        values.put(NotificationTable.NOTIFICATION_IMAGE_URL, jsonArray1.getJSONObject(i).optString("notify_image_url"));
                        values.put(NotificationTable.NOTIFICATION_IS_IMP, jsonArray1.getJSONObject(i).optInt("is_important"));
                        values.put(NotificationTable.NOTIFICATION_URL, jsonArray1.getJSONObject(i).optString("notify_url"));
                        NotificationActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_NOTIFICATION, values);
                    }
                    NotificationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            refreshData();
                        }
                    });
                } catch (JSONException paramResponse) {
                    NotificationActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(NotificationActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void showNotificationData() {
        notificationAdapter = new NotificationAdapter(this, null);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(notificationAdapter);
    }

    public void aleartDialogForInternetConnectivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this, R.style.AppCompatAlertDialogStyle);
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

    private void refreshData() {
        this.getLoaderManager().restartLoader(0, null, this);
        this.getLoaderManager().restartLoader(1, null, this);
    }

    private void refreshDataWRestart() {
        this.getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_filter:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, DirectoryFilterActivity.class);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, subCategoryColorCode);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id==0){
            return new CursorLoader(NotificationActivity.this,
                    ApplicationContentProvider.CONTENT_URI_NOTIFICATION.buildUpon().build(), null, null, null, null);
        }
        else{
            return new CursorLoader(NotificationActivity.this,
                    ApplicationContentProvider.CONTENT_URI_NOTIFICATION_COUNT.buildUpon().build(), null, null, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case 0:
                if (data.getCount() == 0 && !Constant.isConnectingToInternet(NotificationActivity.this)) {
                    aleartDialogForInternetConnectivity();
                }
                notificationAdapter.swapCursor(data);
                notificationAdapter.notifyDataSetChanged();

                break;
            case 1:
                if(data.moveToFirst()){
                    int notificationCount = data.getInt(0);
                    getSupportActionBar().setTitle("Notification ("+notificationCount+")");
                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                refreshDataWRestart();
            }
        }
    }

    public void notificationClicked(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationTable.NOTIFICATION_READ,1);
        getContentResolver().update(ApplicationContentProvider.CONTENT_URI_NOTIFICATION,contentValues,NotificationTable.NOTIFICATION_READ+"=?",new String[]{"0"});
    }

}