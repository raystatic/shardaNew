package fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shardatech.shardauniversity.R;
import com.shardatech.shardauniversity.ViewMapCategoryActivity;
import com.shardatech.shardauniversity.ViewPlacesActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import adapter.MapFragAdapter2;
import database.ApplicationContentProvider;
import databaseTable.HomeActivityCategoryTable;
import databaseTable.MapTable;
import method.Constant;
import method.MySharedPref;
import model.CustomDialogClass;


/**
 * Created by sharda on 9/10/2017.
 */

public class FragTypeMap2 extends Fragment implements OnMapReadyCallback, LocationListener, LoaderManager.LoaderCallbacks<Cursor>, MapFragAdapter2.LocationClickListener{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private JSONObject currentMapObj;
    private View view;
    private GoogleMap googleMap;
    private RecyclerView recyclerViewMap;
    private TextView mapName,mapAdd, mapDist,viewAllPlaces,otherPlaces;
    private MapFragAdapter2 mapFragAdapter;
    double lattitude=0.0, longitude=0.0;
    double currLattitude=0.0, currLongitude=0.0;
    LocationManager locationManager;
    String provider;
    int categoryId = 1;
    Marker m;

    JSONObject mainJSONObj;

    ImageView filterImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_type_maps_2, null);
        mapName         = (TextView) view.findViewById(R.id.college_map_text_name);
        mapAdd          = (TextView) view.findViewById(R.id.college_map_text_add);
        mapDist         = (TextView) view.findViewById(R.id.college_map_text_distance);
        viewAllPlaces   = (TextView) view.findViewById(R.id.tv_all_places);
        otherPlaces     = (TextView) view.findViewById(R.id.tv_other_places);

        mapJsonLoad(getActivity().getApplicationContext());

        recyclerViewMap = (RecyclerView) view.findViewById(R.id.rv_sub_maps);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMap.getContext(),
                manager.getOrientation());
        recyclerViewMap.addItemDecoration(dividerItemDecoration);
        recyclerViewMap.setLayoutManager(manager);
        mapFragAdapter = new MapFragAdapter2(getActivity(),null,this);
        recyclerViewMap.setAdapter(mapFragAdapter);
        view.findViewById(R.id.google_map_direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionAndOpenMaps(new LatLng(lattitude,longitude),googleMap);
            }
        });

        viewAllPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                Intent viewAllPlacesActivity = new Intent(getActivity(), ViewMapCategoryActivity.class);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getActivity().getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR));
                viewAllPlacesActivity.putExtras(bundle);
                startActivityForResult(viewAllPlacesActivity,1);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        setHasOptionsMenu(true);
        return view;
    }

    private void showPopUp(JSONObject jsonObject){
        CustomDialogClass custom = new CustomDialogClass(getActivity(),jsonObject);
        custom.show();
    }

    public void mapJsonLoad(final Context mContext)
    {
        int collegeId                 = MySharedPref.getUserCollegeId(getActivity(),Constant.USER_COLLAGE_ID);
    //    String categoryCode           = getArguments().getString(HomeActivityCategoryTable.CATEGORY_CODE);
        int categoryIdForJson         = getArguments().getInt(HomeActivityCategoryTable.CATEGORY_Id);

        int userId          = MySharedPref.getUserId(getActivity(),Constant.USER_ID);


        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.SUB_CATEGORY_URL+"?"+HomeActivityCategoryTable.CATEGORY_Id+"="+categoryIdForJson
                        +/*"&"+HomeActivityCategoryTable.CATEGORY_CODE+"="+categoryCode
                        +*/"&"+Constant.USER_COLLAGE_ID+"="+collegeId
                        +"&"+Constant.USER_ID+"="+userId)
                   .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {
            }
            public void onResponse(Response response)
                    throws IOException {

                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    if (!isAdded()) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                try {
                    JSONArray jsonArray1 = new JSONArray(responseJsonData);
                    ContentValues values = new ContentValues();
                    mContext.getContentResolver().delete(ApplicationContentProvider.CONTENT_URI_MAP_CATEGORY,null,null);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        values.put(MapTable.MAP_PLACE_ID, jsonArray1.getJSONObject(i).optInt("place_id"));
                        values.put(MapTable.MAP_PLACE_CATEGORY, jsonArray1.getJSONObject(i).optInt("place_category"));
                        values.put(MapTable.MAP_PLACE_NAME, jsonArray1.getJSONObject(i).optString("place_name"));
                        values.put(MapTable.MAP_PLACE_ADD, jsonArray1.getJSONObject(i).optString("place_address"));
                        values.put(MapTable.MAP_PLACE_DETAILS, jsonArray1.getJSONObject(i).optString("place_details"));
                        values.put(MapTable.MAP_PLACE_PHONE, jsonArray1.getJSONObject(i).optString("place_phone"));
                        values.put(MapTable.MAP_PLACE_LATTITUDE, jsonArray1.getJSONObject(i).optString("place_latitude"));
                        values.put(MapTable.MAP_PLACE_LONGITUDE, jsonArray1.getJSONObject(i).optString("place_longitude"));
                        values.put(MapTable.MAP_PLACE_CATEGORY_NAME, jsonArray1.getJSONObject(i).optString("place_category_name"));
                        values.put(MapTable.MAP_PLACE_IMAGE_URL, jsonArray1.getJSONObject(i).optString("place_image_url"));
                        values.put(MapTable.MAP_PLACE_ISIMPORTANT, jsonArray1.getJSONObject(i).optInt("isImportant"));
                        mContext.getContentResolver().insert(ApplicationContentProvider.CONTENT_URI_MAP_CATEGORY, values);
                        if (i==0){
                            currentMapObj = jsonArray1.getJSONObject(i);
                            mainJSONObj = currentMapObj;
                            if (!isAdded()) return;
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                createMapObjectForCollege();
                                }
                            });
                        }
                    }
                } catch (JSONException paramResponse) {
                    if (!isAdded()) return;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    });
                    paramResponse.printStackTrace();
                }

            }
        });
    }

    public void createMapObjectForCollege(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showTextData(currentMapObj);
    };

    public void showTextData(JSONObject jsonObject){
        if(jsonObject==null){
            getActivity().finish();
            return;
        }
        String name     = jsonObject.optString("place_name");
        String add      = jsonObject.optString("place_address");
        String phone    = jsonObject.optString("place_phone");
        String details  = jsonObject.optString("place_details");

        StringBuilder subTextBuilder = new StringBuilder();
        if (add.length()>0){
            subTextBuilder.append(add);
        }
        if (details.length()>0){
            subTextBuilder.append("\n").append("").append(details);
        }
        mapName.setText(name);
        mapAdd.setText(subTextBuilder.toString());
    }

    public void onMapReady(final GoogleMap googleMap) {
        if(currentMapObj==null){
            getActivity().finish();
            return;
        }

        this.googleMap = googleMap;
        String universityMarkerName;
        try {
            universityMarkerName = currentMapObj.getString("place_name");
            lattitude = currentMapObj.getDouble("place_latitude");
            longitude = currentMapObj.getDouble("place_longitude");
        } catch (JSONException e) {
            e.printStackTrace();
            lattitude =28.47272;
            longitude = 77.482002;
            universityMarkerName = "Sharda university";
        }

        LatLng college = new LatLng(lattitude,longitude);
        float zoomLevel = 12.0f; //This goes up to 21
        zoomLevel = 16.0f; //This goes up to 21
        if (categoryId>0) zoomLevel = 18.0f;

        if (m != null){
            m.setPosition(new LatLng(lattitude, longitude));
            m.setTitle(universityMarkerName);
        }
        else{
            m = createMarker(lattitude,longitude,universityMarkerName);
        }
//        m.hideInfoWindow();
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(college,zoomLevel));
        m.showInfoWindow();

        updateLocation();
    }

    private void updateLocation(){
        if (currLattitude>0.0 && lattitude >0.0) {
            getDistanceInfo(currLattitude,currLongitude,lattitude,longitude);
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
            Toast.makeText(getActivity(),"Please select a point to open in maps", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"opening maps", Toast.LENGTH_SHORT).show();
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

    protected Marker createMarker(double latitude, double longitude, String title) {

        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title));
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Need Location Permission")
                        .setMessage("This page needs location permission to show the distance needed to reach a point")
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    getActivity().finish();
                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mapFragAdapter.updateLocation(location);
        currLattitude=location.getLatitude();
        currLongitude=location.getLongitude();
        updateLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onResume() {
        super.onResume();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        List<String> providers = locationManager.getProviders(true);
        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                for (String provider : providers) {
                    locationManager.requestLocationUpdates(provider,100,100,this);
                }
            }
        }



        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if (categoryId<=1){
                        getActivity().finish();
                    }
                    else {
                        locationClickCallback(mainJSONObj,1);
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (checkLocationPermission()) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (categoryId<=0){
                    getActivity().finish();
                }
                else {
                    locationClickCallback(mainJSONObj,-1);
                }
                return true;
            case R.id.action_filter:
                Bundle bundle = new Bundle();
                Intent viewAllPlacesActivity = new Intent(getActivity(), ViewPlacesActivity.class);
                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getActivity().getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR));
                viewAllPlacesActivity.putExtras(bundle);
                startActivityForResult(viewAllPlacesActivity,1);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//
//                Bundle bundle=new Bundle();
//                Intent viewAllPlacesActivity = new Intent(getActivity(), ViewPlacesActivity.class);
//                bundle.putString(HomeActivityCategoryTable.CATEGORY_COLOR, getActivity().getIntent().getStringExtra(HomeActivityCategoryTable.CATEGORY_COLOR));
//                viewAllPlacesActivity.putExtras(bundle);
//                startActivity(viewAllPlacesActivity);
//                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = MapTable.MAP_PLACE_CATEGORY+ ">?";
        String[] selectionArgs = { String.valueOf(1) };
        if (categoryId>1){
            selection = selection+" AND "+MapTable.MAP_PLACE_CATEGORY+" =?" +" AND "+MapTable.MAP_PLACE_ID+" <>?" ;
            selectionArgs = new String[]{String.valueOf(1),String.valueOf(categoryId),String.valueOf(currentMapObj.optString(MapTable.MAP_PLACE_ID))};
        }
        else{
            selection = selection+" AND "+MapTable.MAP_PLACE_ISIMPORTANT+" =?";
            selectionArgs = new String[]{String.valueOf(1),String.valueOf(1)};
        }
        return new CursorLoader(getActivity(), ApplicationContentProvider.CONTENT_URI_MAP_CATEGORY.buildUpon().build(), null,selection, selectionArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0 && !Constant.isConnectingToInternet(getActivity()) )
        {
            Toast.makeText(getActivity(),"No internet Connection",Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        mapFragAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private double getDistanceInfo(double lat1, double lng1, double lat2, double lng2) {
        Double dist = 0.0;
        try {
            String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lng1 + "&destination=" + lat2 + "," + lng2 + "&mode=driving&sensor=false";
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                public void onFailure(Request paramRequest, IOException paramIOException) {
                }

                public void onResponse(Response response)
                        throws IOException {
                    String responseJsonData = response.body().string();
                    if (!response.isSuccessful()) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseJsonData);
                        JSONArray array = jsonObject.getJSONArray("routes");
                        JSONObject routes = array.getJSONObject(0);
                        JSONArray legs = routes.getJSONArray("legs");
                        JSONObject steps = legs.getJSONObject(0);
                        JSONObject distance = steps.getJSONObject("distance");
                        Log.i("Distance", distance.toString());
                        final double dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]", ""));
                        if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                mapDist.setText("" + dist+" KM from here");
                            }
                        });}
                    } catch (JSONException paramResponse) {
                        paramResponse.printStackTrace();
                    }
                }
            });

        }
        catch (Exception e){
            mapDist.setVisibility(View.GONE);
        }
        return dist;
    }

    public void locationClickCallback(JSONObject jsonObject, int categoryId){
        this.currentMapObj=jsonObject;
        this.categoryId =categoryId;
        createMapObjectForCollege();
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void locationClickCallback(int id, double latitude, double longitude,int categoryId, String name, String address, String details, String phone) {
        try {
            currentMapObj = new JSONObject();
            currentMapObj.put("place_id",id);
            currentMapObj.put("place_name",name);
            currentMapObj.put("place_address",address);
            currentMapObj.put("place_details",details);
            currentMapObj.put("place_phone",phone);
            currentMapObj.put("place_latitude",latitude);
            currentMapObj.put("place_longitude",longitude);
            locationClickCallback(currentMapObj,categoryId);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"An error occured", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("json");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    locationClickCallback(jsonObject,jsonObject.optInt(MapTable.MAP_PLACE_CATEGORY));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            //    Toast.makeText(getActivity(),"An error occured",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
