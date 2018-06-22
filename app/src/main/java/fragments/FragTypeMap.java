package fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shardatech.shardauniversity.InternalMaps;
import com.shardatech.shardauniversity.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import adapter.MapFragAdapter;
import method.Constant;
import method.RecyclerItemClickListener;
import model.CustomDialogClass;


/**
 * Created by sharda on 9/10/2017.
 */

public class FragTypeMap extends Fragment implements OnMapReadyCallback{
    private JSONArray mapJsonArray;
    private JSONObject collegeMapObj;
    private View view;
    private GoogleMap googleMap;
    private RecyclerView recyclerViewMap;
    private TextView mapName,mapAdd;
    private MapFragAdapter mapFragAdapter;
    private FloatingActionButton fab;
    private static final int REQUEST_CODE_PERMISSION = 2;
    double lattitude=0.0, longitude=0.0;
    Marker myMarker;
    String[] mPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    int mapId=0,placeId=0, subPlaceId=0;
    public float zoomLevelDefault  = 2.0f; //This goes up to 21


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_type_maps, null);
        mapName = (TextView) view.findViewById(R.id.college_map_text_name);
        mapAdd = (TextView) view.findViewById(R.id.college_map_text_add);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerViewMap = (RecyclerView) view.findViewById(R.id.rv_sub_maps);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewMap.getContext(),
                manager.getOrientation());
        recyclerViewMap.addItemDecoration(dividerItemDecoration);
        recyclerViewMap.setLayoutManager(manager);
        mapFragAdapter = new MapFragAdapter(new JSONArray(),getActivity());
        recyclerViewMap.setAdapter(mapFragAdapter);

        view.findViewById(R.id.google_map_direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionAndOpenMaps(new LatLng(lattitude,longitude),googleMap);
            }
        });

        recyclerViewMap.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerViewMap,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        JSONObject jsonObject = mapFragAdapter.getJSON(position);
                        JSONArray jsonArray = new JSONArray();
                        Intent intent = new Intent(getActivity(), InternalMaps.class);
                        int placeId = jsonObject.optInt("place_id");
                        int mapId = jsonObject.optInt("map_id");
                        int subPlaceId = jsonObject.optInt("sub_place_id");

                        if(subPlaceId>0){
                            try {
                                jsonArray= jsonObject.getJSONArray("sub_sub_places");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("sub_place_id",subPlaceId);
                            intent.putExtra("map_id",mapId);
                            intent.putExtra("place_id",placeId);
                        }
                        else if (placeId>0){
                            try {
                                jsonArray= jsonObject.getJSONArray("sub_places");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("place_id",placeId);
                            intent.putExtra("map_id",mapId);
                            intent.putExtra("sub_place_id",0);
                        }
                        else if (mapId>0){
                            try {
                                jsonArray= jsonObject.getJSONArray("places");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("place_id",0);
                            intent.putExtra("sub_place_id",0);
                            intent.putExtra("map_id",mapId);
                        }
                        else{
                            //no action needed
                            return;
                        }

                        Double latt = jsonObject.optDouble("lattitude");

                        if (jsonArray.length()==0 && jsonObject.optDouble("lattitude")==0.0){
                            showPopUp(jsonObject);
                        }
                        else{
                            intent.putExtra("json",jsonObject.toString());
                            startActivity(intent);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return view;
    }

    private void showPopUp(JSONObject jsonObject){
        CustomDialogClass custom = new CustomDialogClass(getActivity(),jsonObject);
        custom.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        if (getActivity() instanceof InternalMaps){
            try {
                zoomLevelDefault  = 14.0f;
                collegeMapObj = new JSONObject(getActivity().getIntent().getStringExtra("json"));
                mapId = getActivity().getIntent().getIntExtra("map_id",0);
                placeId = getActivity().getIntent().getIntExtra("place_id",0);
                subPlaceId = getActivity().getIntent().getIntExtra("sub_place_id",0);
                updatePlaces(collegeMapObj);
                showTextData(collegeMapObj);
                createMapObjectForCollege();
            } catch (JSONException e) {
                e.printStackTrace();
                getActivity().finish();
            }

        }
        else{
            subCategoryJsonLoad();
        }
        super.onActivityCreated(savedInstance);
    }

    public void subCategoryJsonLoad()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constant.CAT_TYPE_MAP_TEMP_URL)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(Request paramRequest, IOException paramIOException) {
            }
            public void onResponse(Response response)
                    throws IOException {

                String responseJsonData = response.body().string();
                if (!response.isSuccessful()){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                try {
                    mapJsonArray = new JSONArray(responseJsonData).getJSONObject(0).getJSONArray("internal_maps");
                    collegeMapObj = new JSONArray(responseJsonData).getJSONObject(0).getJSONObject("college_map");
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mapFragAdapter.updateMapJson(mapJsonArray);
                            mapFragAdapter.notifyDataSetChanged();
                            createMapObjectForCollege();
                            showTextData(collegeMapObj);
                        }
                    });
                } catch (JSONException paramResponse) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "An error occurred, Please try again", Toast.LENGTH_SHORT).show();
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
    };

    public void showTextData(JSONObject jsonObject){
        String name = jsonObject.optString("name");
        String add = jsonObject.optString("address");
        final String phone = jsonObject.optString("phone");
        String timings = jsonObject.optString("timings");
        String otherDetails = jsonObject.optString("other_details");

        StringBuilder subTextBuilder = new StringBuilder();
        if (add.length()>0){
            subTextBuilder.append("Address: ").append(add).append("\n");
        }
        if (timings.length()>0){
            subTextBuilder.append("Timings: ").append(timings).append("\n");
        }
        if (otherDetails.length()>0){
            subTextBuilder.append("Details: ").append(otherDetails);
        }

        mapName.setText(name);
        mapAdd.setText(subTextBuilder.toString());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });
    }


    public void aleartDialogForInternetConnectivity()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
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


   /* @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        Double lattitude;
        Double longitude;
        String universityMarkerName;
        try {
            universityMarkerName = collegeMapObj.getString("name");
            lattitude = collegeMapObj.getDouble("lattitude");
            longitude = collegeMapObj.getDouble("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
            lattitude =28.47272;
            longitude = 77.482002;
            universityMarkerName = "Sharda university";
        }

        JSONArray jsonArray = new JSONArray();
        this.googleMap.clear();

        if(mapId>0){
            try {
                if (placeId==0){
                    jsonArray = collegeMapObj.getJSONArray("places");
                }
                else if(subPlaceId==0){
                    jsonArray = collegeMapObj.getJSONArray("sub_places");
                }
                for(int i=0; i<jsonArray.length();i++){
                    lattitude = jsonArray.getJSONObject(i).getDouble("lattitude");
                    longitude = jsonArray.getJSONObject(i).getDouble("longitude");
                    universityMarkerName = jsonArray.getJSONObject(i).getString("name");
                    createMarker(lattitude,longitude,universityMarkerName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LatLng college = new LatLng(lattitude,longitude);
        float zoomLevel = 12.0f; //This goes up to 21
        if (jsonArray.length()==0){
            zoomLevel = 14.0f; //This goes up to 21
            createMarker(lattitude,longitude,universityMarkerName);
            this.lattitude=lattitude;
            this.longitude=longitude;
        }
        else{
            this.lattitude=0.0;
            this.longitude=0.0;
            view.findViewById(R.id.ll_google_map).setVisibility(View.GONE);
        }

        this.googleMap.setMyLocationEnabled(false);
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(college,12.0f));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(college,zoomLevel));
//        googleMap.setMyLocationEnabled(true);
    }*/





    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        Double lattitude;
        Double longitude;
        String universityMarkerName;
        try {
            universityMarkerName = collegeMapObj.getString("name");
            lattitude = collegeMapObj.getDouble("lattitude");
            longitude = collegeMapObj.getDouble("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
            lattitude =28.47272;
            longitude = 77.482002;
            universityMarkerName = "Sharda university";
        }

        JSONArray jsonArray = new JSONArray();
        this.googleMap.clear();

        if(mapId>0){
            try {
                if (placeId==0){
                    jsonArray = collegeMapObj.getJSONArray("places");
                }
                else if(subPlaceId==0){
                    jsonArray = collegeMapObj.getJSONArray("sub_places");
                }
                for(int i=0; i<jsonArray.length();i++){
                    lattitude = jsonArray.getJSONObject(i).getDouble("lattitude");
                    longitude = jsonArray.getJSONObject(i).getDouble("longitude");
                    universityMarkerName = jsonArray.getJSONObject(i).getString("name");
                    createMarker(lattitude,longitude,universityMarkerName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LatLng college = new LatLng(lattitude,longitude);
        float zoomLevel = 12.0f; //This goes up to 21
        if (jsonArray.length()==0){
            zoomLevel = 14.0f; //This goes up to 21
            createMarker(lattitude,longitude,universityMarkerName);
            this.lattitude=lattitude;
            this.longitude=longitude;
        }
        else{
            this.lattitude=0.0;
            this.longitude=0.0;
            view.findViewById(R.id.ll_google_map).setVisibility(View.GONE);
        }

        this.googleMap.setMyLocationEnabled(false);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(college,zoomLevelDefault));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(college,zoomLevel));
//        googleMap.setMyLocationEnabled(true);
    }








    private void requestPermissionAndOpenMaps(LatLng latLng, GoogleMap googleMap){
        lattitude = latLng.latitude;
        longitude = latLng.longitude;
        googleMap.setMyLocationEnabled(false);
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

    private void updateMap(JSONObject jsonObject) throws JSONException{
        try {
            lattitude = jsonObject.getDouble("lattitude");
            longitude = jsonObject.getDouble("longitude");
            myMarker.setPosition(new LatLng(lattitude,longitude));
            float zoomLevel = 14.0f; //This goes up to 21
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lattitude,longitude),zoomLevel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updatePlaces(JSONObject jsonObject) throws JSONException {
            JSONArray jsonArray;
            if(subPlaceId>0){
                try{
                    jsonArray= jsonObject.getJSONArray("sub_sub_places");
                }
                catch (Exception e){
                    jsonArray = new JSONArray();
                }
            }
            else if (placeId>0){
                try{
                    jsonArray= jsonObject.getJSONArray("sub_places");
                }
                catch (Exception e){
                    jsonArray = new JSONArray();
                }
            }
            else if(mapId>0){
                try{
                    jsonArray= jsonObject.getJSONArray("places");
                }
                catch (Exception e){
                    jsonArray = new JSONArray();
                }
            }
            else {
                jsonArray = new JSONArray();
            }
            mapFragAdapter.updateMapJson(jsonArray);
            mapFragAdapter.notifyDataSetChanged();
    }

    protected Marker createMarker(double latitude, double longitude, String title) {
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title));
    }

}
