package com.shardatech.shardauniversity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import adapter.MapFragAdapter2;
import database.ApplicationContentProvider;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.MapTable;
import method.Constant;

/**
 * Created by sharda on 9/19/2017.
 */

public class ViewMapCategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MapFragAdapter2.LocationClickListener{

    private TextView tvFilterPlaces,tvShortName,tvShortDistance;
    private String title,getCategoryColor;
    private RecyclerView rvViewPlaces;
    private MapFragAdapter2 mapFragAdapter;

    private int LOADER_FOR_DISTANCE = 1;
    private int LOADER_FOR_NAME     = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = getString(R.string.view_map_places);
        getCategoryColor = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(String.valueOf(R.color.colorGreen))));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getCategoryColor)));
        getSupportActionBar().setTitle(title);

        tvFilterPlaces      = (TextView) findViewById(R.id.tv_filter_places);
        tvShortName         = (TextView) findViewById(R.id.tv_short_name);
        tvShortDistance     = (TextView) findViewById(R.id.tv_short_distance);
        rvViewPlaces        = (RecyclerView) findViewById(R.id.rv_view_places);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvViewPlaces.getContext(),
                manager.getOrientation());
        rvViewPlaces.addItemDecoration(dividerItemDecoration);
        rvViewPlaces.setLayoutManager(manager);
        mapFragAdapter = new MapFragAdapter2(this,null,this);
        rvViewPlaces.setAdapter(mapFragAdapter);

        //   getSupportLoaderManager().initLoader(LOADER_FOR_DISTANCE, null, ViewMapCategoryActivity.this);
        getSupportLoaderManager().initLoader(LOADER_FOR_NAME, null, ViewMapCategoryActivity.this);


        tvShortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().initLoader(LOADER_FOR_NAME, null, ViewMapCategoryActivity.this);
                tvShortName.setBackgroundResource(R.drawable.ractangle_shape_solid);
                tvShortName.setTextColor(getResources().getColor(R.color.colorWhite));
                tvShortDistance.setBackgroundResource(R.drawable.ractangle_shape_stroke);
                tvShortDistance.setTextColor(getResources().getColor(R.color.colorGreen));
            }
        });

        tvShortDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportLoaderManager().initLoader(LOADER_FOR_DISTANCE, null, ViewMapCategoryActivity.this);
                tvShortDistance.setBackgroundResource(R.drawable.ractangle_shape_solid);
                tvShortDistance.setTextColor(getResources().getColor(R.color.colorWhite));
                tvShortName.setBackgroundResource(R.drawable.ractangle_shape_stroke);
                tvShortName.setTextColor(getResources().getColor(R.color.colorGreen));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_filter:
                Bundle bundle = new Bundle();
                Intent viewAllPlacesActivity = new Intent(this, ViewPlacesActivity.class);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getCategoryColor);
                viewAllPlacesActivity.putExtras(bundle);
                startActivity(viewAllPlacesActivity);
                this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

/*
                Bundle bundle=new Bundle();
                Intent viewAllPlacesActivity = new Intent(this, ViewPlacesActivity.class);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, this.getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR));
                viewAllPlacesActivity.putExtras(bundle);
                startActivity(viewAllPlacesActivity);
                this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
*/
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.map_filter_menu, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = MapTable.MAP_PLACE_CATEGORY+ ">?";
        String[] selectionArgs = { String.valueOf(1) };

        if (Constant.hashMapStr.length()>0){
            selection+= " AND "+MapTable.MAP_PLACE_CATEGORY+" in ("+Constant.hashMapStr+")";
            selectionArgs = new String[]{ String.valueOf(1)};
        }

        if(id==LOADER_FOR_NAME){
            return new CursorLoader(this, ApplicationContentProvider.CONTENT_URI_MAP_CATEGORY.buildUpon().build(), null,selection,selectionArgs, MapTable.MAP_PLACE_NAME+" ASC");
        }
        else {
            return new CursorLoader(this, ApplicationContentProvider.CONTENT_URI_MAP_CATEGORY.buildUpon().build(), null,selection,selectionArgs, MapTable.MAP_PLACE_NAME+" DESC");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0 && !Constant.isConnectingToInternet(this) )
        {
            Toast.makeText(this,"No internet Connection",Toast.LENGTH_SHORT).show();
            finish();
        }
        mapFragAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void locationClickCallback(int id, double latitude, double longitude, int categoryId, String mapName, String add, String details, String phoneNumber) {
        try {
            JSONObject currentMapObj = new JSONObject();
            currentMapObj.put("place_id",id);
            currentMapObj.put("place_name",mapName);
            currentMapObj.put("place_address",add);
            currentMapObj.put("place_details",details);
            currentMapObj.put("place_phone",phoneNumber);
            currentMapObj.put("place_latitude",latitude);
            currentMapObj.put("place_longitude",longitude);
            currentMapObj.put("place_category",categoryId);
            Bundle bundle=new Bundle();
            Intent intent = new Intent();
            bundle.putString("json",currentMapObj.toString());
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK,intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ViewMapCategoryActivity.this,"An error occured",Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getSupportLoaderManager().restartLoader(LOADER_FOR_DISTANCE, null, ViewMapCategoryActivity.this);
    }


}
