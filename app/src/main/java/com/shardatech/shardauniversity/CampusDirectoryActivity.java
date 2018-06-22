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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import adapter.AdapterCampusDirectory;
import database.ApplicationContentProvider;
import databaseTable.CampusDirectoryHeadingTable;
import databaseTable.CampusDirectoryMainTable;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.SubCategoryTable;
import method.Constant;

/**
 * Created by sharda on 9/10/2017.
 */

public class CampusDirectoryActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private AdapterCampusDirectory adapterCampusDirectory;
    private int subCategoryId;
    private String subCategoryName,subCategoryCatCode,subCategoryColorCode;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory_list);
        recyclerView    = (RecyclerView) findViewById(R.id.rl_sub_category_directory);

        subCategoryId           = getIntent().getIntExtra(SubCategoryTable.SUB_CATEGORY_Id,0);
        subCategoryName         = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_NAME);
        subCategoryCatCode      = getIntent().getStringExtra(SubCategoryTable.SUB_CATEGORY_CAT_CODE);
        subCategoryColorCode    = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subCategoryName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(subCategoryColorCode)));



        campusDirectoryJsonLoad();
        showCampusDirectoryData();
        refreshData();
    }

    public void campusDirectoryJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.SUB_SUB_CATEGORY_URL
                        +"?"+SubCategoryTable.SUB_CATEGORY_Id+"="+subCategoryId
                        +"&"+SubCategoryTable.SUB_CATEGORY_CAT_CODE+"="+subCategoryCatCode)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {

            }
            public void onResponse(Response response)
                    throws IOException {
                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    CampusDirectoryActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CampusDirectoryActivity.this, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                try {
                    JSONArray jsonArray  = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
                    JSONArray jsonArray1 = jsonArray.getJSONObject(0).getJSONArray("directory");
                    JSONArray jsonArray2 = jsonArray.getJSONObject(0).getJSONArray("department");


                    String selection            = CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID + "=?";
                    String selection_sub_cat    = CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_HEADING_ID+
                            " in (select "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+
                            " from "+CampusDirectoryHeadingTable.TABLE_CAMPUS_DIRECTORY_HEADING+
                            " WHERE "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID+"=?)";
                    String[] selectionArgs      = { String.valueOf(subCategoryId) };

                    CampusDirectoryActivity.this.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_CAMPUS_DIRECTORY_MAIN,selection_sub_cat,selectionArgs);
                    CampusDirectoryActivity.this.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_CAMPUS_DIRECTORY_HEADING,selection,selectionArgs);

                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_HEADING_ID, jsonArray1.getJSONObject(i).optInt("department_id"));
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_Id, jsonArray1.getJSONObject(i).optInt("directory_id"));
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_NAME, jsonArray1.getJSONObject(i).optString("directory_name"));
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_DESIGNATION, jsonArray1.getJSONObject(i).optString("directory_designation"));
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_CONTACT_NUMBER, jsonArray1.getJSONObject(i).optString("directory_contact_number"));
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EXT, jsonArray1.getJSONObject(i).optString("directory_extension"));
                        values.put(CampusDirectoryMainTable.CAMPUS_DIRECTORY_CATEGORY_EMAIL, jsonArray1.getJSONObject(i).optString("directory_email"));
                        CampusDirectoryActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_CAMPUS_DIRECTORY_MAIN, values);
                    }

                    values = new ContentValues();
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        values.put(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID, jsonArray2.getJSONObject(i).optInt("sub_sub_cat_id"));
                        values.put(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id, jsonArray2.getJSONObject(i).optInt("department_id"));
                        values.put(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME, jsonArray2.getJSONObject(i).optString("department_name"));
                        CampusDirectoryActivity.this.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_CAMPUS_DIRECTORY_HEADING, values);
                    }

                } catch (JSONException paramResponse) {
                    CampusDirectoryActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CampusDirectoryActivity.this, "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    paramResponse.printStackTrace();
                }
            }
        });
    }

    public void showCampusDirectoryData() {
        adapterCampusDirectory = new AdapterCampusDirectory(this,null,subCategoryColorCode);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapterCampusDirectory);
    }

    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(CampusDirectoryActivity.this, R.style.AppCompatAlertDialogStyle);
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

    private void refreshData(){
        this.getLoaderManager().initLoader(0, null, this);
    }

    private void refreshDataWRestart(){
        this.getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_filter:
                Bundle bundle=new Bundle();
                Intent intent = new Intent(this, DirectoryFilterActivity.class);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, subCategoryColorCode);
                intent.putExtras(bundle);
                startActivityForResult(intent,2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            /*
            case R.id.search:
                        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
                        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

                        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                Log.d(TAG, "onQueryTextSubmit ");
                                cursor=studentRepo.getStudentListByKeyword(s);
                                if (cursor==null){
                                    Toast.makeText(MainActivity.this,"No records found!",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(MainActivity.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                                }
                                customAdapter.swapCursor(cursor);

                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                Log.d(TAG, "onQueryTextChange ");
                                cursor=studentRepo.getStudentListByKeyword(s);
                                if (cursor!=null){
                                    customAdapter.swapCursor(cursor);
                                }
                                return false;
                            }

                        });
*/





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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.campus_directory_filter_menu, menu);
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.d(TAG, "onQueryTextSubmit ");
                    Cursor cursor;
                    cursor = adapterCampusDirectory.getCursor();
                    this.query = s;
                    refreshData();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    this.query =s;
                    refreshData();
                    return false;
                }
            });

        }*/

        return true;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_SUB_CATEGORY_ID+ "=?";
        String[] selectionArgs = { String.valueOf(subCategoryId) };
        if (Constant.hashFacilityStr.length()>0){
            selection+=" AND "+CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id+" in (?)";
            selectionArgs = new String[]{String.valueOf(subCategoryId),Constant.hashFacilityStr};
        }

        return new CursorLoader(CampusDirectoryActivity.this,
                ApplicationContentProvider.CONTENT_URI_CAMPUS_DIRECTORY_HEADING_JOIN_CAMPUS_DIRECTORY_MAIN.buildUpon().build(),
                null,selection, selectionArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0 && !Constant.isConnectingToInternet(CampusDirectoryActivity.this) )
        {
            aleartDialogForInternetConnectivity();
        }
        adapterCampusDirectory.swapCursor(data);
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
            if(resultCode == Activity.RESULT_OK){
                refreshDataWRestart();
            }
        }
    }

}