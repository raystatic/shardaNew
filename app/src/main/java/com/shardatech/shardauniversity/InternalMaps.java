package com.shardatech.shardauniversity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by sharda on 9/19/2017.
 */

public class InternalMaps extends AppCompatActivity {

    private GoogleMap googleMap;
    double lattitude=0.0, longitude=0.0;
    Marker myMarker;
    private TextView mapName,mapAdd;
    FloatingActionButton fab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = "";

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.com_facebook_blue)));
        getSupportActionBar().setTitle(title);

    }

}
