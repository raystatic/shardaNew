package adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shardatech.shardauniversity.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import databaseTable.MapTable;

/**
 * Created by sharda on 9/18/2017.
 */

public class MapFragAdapter2  extends RecyclerViewCursorAdapter<MapFragAdapter2.ViewHolder> {
    Context mContext;
    Cursor mCursor;
    double latitudeLoc = 0.0;
    double longitudeLoc = 0.0;

    LocationClickListener locationClickListener;

    public interface LocationClickListener{
        void locationClickCallback(int id, double latitude, double longitude, int categoryId, String mapName, String add, String details, String phoneNumber);
    }

    public MapFragAdapter2(Context context, Cursor cursor, LocationClickListener locationClickListener) {
        super(context, cursor);
        this.mCursor=cursor;
        this.mContext=context;
        lstHolders = new ArrayList<>();
        this.locationClickListener = locationClickListener;
    }

    private final List<ViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    private Runnable updateDistanceRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                for (ViewHolder holder : lstHolders) {
                    holder.updateDistance();
                }
            }
            notifyDataSetChanged();
        }
    };


    public void updateLocation(Location location){
        this.latitudeLoc=location.getLatitude();
        this.longitudeLoc=location.getLongitude();
        if (latitudeLoc>0.0){
            mHandler.post(updateDistanceRunnable);
        }
    }

    public void updateCurrentLocationViaLatLng(double latitude, double longitude){
        this.latitudeLoc = latitude;
        this.longitudeLoc = longitude;
        if (latitudeLoc>0.0){
            mHandler.post(updateDistanceRunnable);
        }
    }



    @Override
    public void onBindViewHolder(MapFragAdapter2.ViewHolder viewHolder, Cursor cursor) {
        viewHolder.setData(cursor);
        synchronized (lstHolders) {
            lstHolders.add(viewHolder);
        }
    }

    @Override
    public MapFragAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_map_list, parent, false);
        MapFragAdapter2.ViewHolder vh = new MapFragAdapter2.ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvMapMainPlaceCategory,tvMapPlaceName,tvMapDistance ;
        public ImageView ivMapCategoryImage;
        Cursor cursor;
        String imageUrl,placeName,placeCategory;
        double latitude, longitude;
        int categoryId, placeId;
        String address,details,phone;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            this.ivMapCategoryImage     = (ImageView) itemView.findViewById(R.id.iv_map_category_image);
            this.tvMapMainPlaceCategory = (TextView) itemView.findViewById(R.id.tv_map_main_place_category);
            this.tvMapPlaceName         = (TextView) itemView.findViewById(R.id.tv_map_place_name);
            this.tvMapDistance          = (TextView) itemView.findViewById(R.id.tv_map_distance);
        }

        public void updateDistance(){
            getDistanceInfo(latitudeLoc,longitudeLoc, latitude, longitude, tvMapDistance);
        }

        public void setData(Cursor cursor) {
            this.cursor         = cursor;
            this.placeName      = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_NAME));
            this.address      = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_ADD));
            this.details      = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_DETAILS));
            this.imageUrl       = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_IMAGE_URL));
            this.phone      = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_PHONE));
            this.categoryId = cursor.getInt(cursor.getColumnIndex(MapTable.MAP_PLACE_CATEGORY));
            this.placeId = cursor.getInt(cursor.getColumnIndex(MapTable.MAP_PLACE_ID));
            this.placeCategory  = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_CATEGORY_NAME));
            this.latitude       = cursor.getDouble(cursor.getColumnIndex(MapTable.MAP_PLACE_LATTITUDE));
            this.longitude       = cursor.getDouble(cursor.getColumnIndex(MapTable.MAP_PLACE_LONGITUDE));
            //     this.placeDistanve  = cursor.getString(cursor.getColumnIndex(MapTable.));

            if (imageUrl.length()==0){
                imageUrl="test";
            }
            Picasso.with(mContext)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_bg_icon)
                   // .transform(new CircleTransform())
                    .fit()
                    .into(ivMapCategoryImage);
            tvMapMainPlaceCategory.setText(placeCategory);
            tvMapPlaceName.setText(placeName);
        }

        @Override
        public void onClick(View v) {
            locationClickListener.locationClickCallback(placeId, latitude,longitude,categoryId,placeName,address,details,phone);
        }
    }

    private double getDistanceInfo(double lat1, double lng1, double lat2, double lng2, final TextView textView) {
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
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, "An error occured, Please come back after sometime", Toast.LENGTH_SHORT).show();
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
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                textView.setText("Distance is " + dist+" KM");
                            }
                        });
                    } catch (JSONException paramResponse) {
                        paramResponse.printStackTrace();
                    }

                }
            });

        }
        catch (Exception e){
            textView.setVisibility(View.GONE);
        }
        return dist;
    }

}
