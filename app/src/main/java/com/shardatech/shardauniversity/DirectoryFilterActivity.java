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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import adapter.DirectoryFilterActivityAdapter;
import database.ApplicationContentProvider;
import databaseTable.HomeActivityCategoryTable;
import method.Constant;

/**
 * Created by sharda on 9/19/2017.
 */

public class DirectoryFilterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private String title,getCategoryColor;
    private RecyclerView rvViewPlaces;
    private DirectoryFilterActivityAdapter mapFragAdapter;
    HashMap<Integer, Integer> categoryIdHashmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_filter_activity);
        categoryIdHashmap= new HashMap<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_filter);
        title = getString(R.string.map_filter);
        getCategoryColor = getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR);

        String[] hasmapArray = Constant.hashFacilityStr.split(",");
        for (int i=0; i<hasmapArray.length;i++){
            try{
                categoryIdHashmap.put(Integer.parseInt(hasmapArray[i]),Integer.parseInt(hasmapArray[i]));
            }
            catch (Exception e){

            }
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getCategoryColor)));
        getSupportActionBar().setTitle(title);


        rvViewPlaces        = (RecyclerView) findViewById(R.id.rv_filter_places);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvViewPlaces.getContext(),
                manager.getOrientation());
        rvViewPlaces.addItemDecoration(dividerItemDecoration);
        rvViewPlaces.setLayoutManager(manager);
        mapFragAdapter = new DirectoryFilterActivityAdapter(this,null,categoryIdHashmap,getCategoryColor);
        rvViewPlaces.setAdapter(mapFragAdapter);
        getSupportLoaderManager().initLoader(0, null, DirectoryFilterActivity.this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_filter_done:
                Constant.hashFacilityStr=getStringFromHashmap();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
        }return false;
    }

    public String getStringFromHashmap(){
        Iterator it = categoryIdHashmap.entrySet().iterator();
        StringBuilder hashmapStr = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            hashmapStr.append(pair.getKey()).append(",");
            it.remove(); // avoids a ConcurrentModificationException
        }
        hashmapStr.setLength(Math.max(hashmapStr.length() - 1, 0));
        return hashmapStr.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.map_filter_menu_done, menu);
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
         return new CursorLoader(this,
                 ApplicationContentProvider.CONTENT_URI_CAMPUS_DIRECTORY_HEADING.buildUpon().build(), null,null, null,null);
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


}